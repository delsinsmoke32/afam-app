# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository layout

This is a university software-engineering project ("gestionale AFAM" — a portfolio management system for
art/music academy students). It is **not a single git repo**: the top-level directory and `afam-backend/`
are plain folders (no `.git`), while `afam-frontend/` is its own independent git repository. Keep this in
mind before running `git` commands — you generally need to `cd afam-frontend` first for git operations to
work there, and there is no root-level history to inspect.

Three independent pieces, wired together only through HTTP calls and a shared Docker MySQL instance:

- `afam-backend/` — Spring Boot 3.5 / Java 21 REST API (Maven).
- `afam-frontend/` — Angular 21 standalone-component SPA (npm).
- `docker-compose.yml` + `db-init/init.sql` — local MySQL 8.0 container.

## Commands

### Database (run from repo root)

```bash
docker compose up -d          # starts MySQL 8.0 on host port 3307, container afam_mysql_db
docker compose down           # stop it (data persists in ./mysql_data on the host)
```

### Backend (`afam-backend/`)

```bash
./mvnw spring-boot:run                                   # run the API (listens on :8080 by default)
./mvnw compile                                            # compile only
./mvnw test                                                # run all tests
./mvnw test -Dtest=AfamBackendApplicationTests#contextLoads  # run a single test method
```

The backend needs the MySQL container running first (`spring.datasource.url` points at
`localhost:3307`).

### Frontend (`afam-frontend/`)

```bash
npm install
npm start        # == ng serve, http://localhost:4200
npm run build    # == ng build, output to dist/
npm test         # == ng test, runs Vitest (NOT Karma/Jasmine) via @angular/build:unit-test
```

To run a single spec file, use Vitest's own filtering, e.g. `ng test -- --project=default login`
(the Vitest runner is invoked under the hood by `@angular/build:unit-test`, so check
`ng test --help` if the filter flags change between CLI versions).

## Sequence-diagram-driven development

Features in this project are implemented directly from UML sequence diagrams (PNGs) that live outside
the repo at `/Users/claudio/Ingegneria_Software/Progetto/sequence/` (e.g. `Registrazione.png`,
`Login2FA.png`, `Recupero_Password.png`, `RegistrazioneSPID.png` — not yet all implemented). The project
requires **1:1 naming fidelity**: class names and method names in the code should match the diagram's
lifeline/message names exactly (adjusted only for language casing conventions, e.g. `VerificaCredenziali`
→ `verificaCredenziali`). Component names in particular must match the diagram's *Boundary* lifeline name,
not a generic Angular-CLI-style name — e.g. the landing page is `AutenticazioneBoundary`, not
`AuthLandingComponent`; the registration page is `RegistrazioneBoundary`, not `RegisterComponent`. Before
implementing a new diagram, read it carefully (crop/zoom into a copy under a scratch dir if any arrows or
labels are ambiguous at full-image resolution) and flag anything structurally infeasible before writing
code.

**The recurring translation problem:** every diagram so far draws one continuous, blocking
Controller/Boundary object that both talks to `DBMSBoundary` (server-only) and creates/shows UI popups or
navigates (client-only). That is not literally realizable across the Angular↔Spring Boot network boundary.
The established pattern (used in both Registrazione and Login2FA, carry it forward) is **"one backend call
per user-submitted form, thin frontend reacting to the response"**: all self-validation, DB checks, and
persistence for one form submission happen inside a single Spring REST endpoint; anything on the diagram
that is inherently UI (popup create/show/destroy, page navigation) is realized in the Angular component
reacting to that endpoint's response, not literally mirrored as separate backend messages. A flow with two
separate user-submitted forms (e.g. Login2FA's credentials-then-OTP) needs two correlated backend calls
(currently correlated by email — there's no session/token layer yet).

**Gaps between diagrams and the entity model:** sequence diagrams don't enumerate every attribute, so
they've twice omitted fields the JPA entities actually require (`Studente.nome`/`cognome` missing from
Registrazione's `InserisciCredenziali`; no OTP table existed anywhere for Login2FA's `salvaOTP`). When a
diagram's message doesn't carry a field the entity needs, or a DB `UNIQUE` constraint isn't checked
anywhere in the diagram (e.g. `Studente.email` alongside `codiceFiscale`), add what's needed to avoid an
unhandled 500 and note the departure in a comment — don't silently invent unrelated behavior.

## Backend architecture

The backend follows an explicit **Entity-Control-Boundary (ECB)** structure driven by a UML sequence
diagram from the course project (see the numbered comments in the code, e.g. `// Messaggio 13.1 del
Sequence Diagram`, which map directly to sequence-diagram message numbers — keep that traceability when
adding new boundary/control methods):

- `entity/` — JPA `@Entity` classes (Lombok `@Data @Builder`), one per `db-init/init.sql` table.
- `repository/` — Spring Data `JpaRepository` interfaces.
- `boundary/` — service-layer classes (e.g. `DBMSBoundary`) that sit between controllers and
  repositories and implement the "boundary" objects from the sequence diagrams.
- `controller/` — `@RestController`s exposed under `/api/...`, all annotated
  `@CrossOrigin(origins = "http://localhost:4200")` to allow the Angular dev server.

Entity relationship graph (all `@ManyToOne`/`@OneToMany`, cascade `ALL`, fetch `LAZY`, with
`@ToString.Exclude @EqualsAndHashCode.Exclude` on the relationship fields to avoid Lombok
recursion/N+1 issues):

```
Studente 1──N Portfolio 1──N Sezione 1──N Allegato
    │             │
    │             └──N LinkCondiviso 1──N StatisticaAccesso
    └──N CodiceOtp
```

`CodiceOtp` (added for Login2FA) isn't in `db-init/init.sql` — it exists only as a JPA entity, generated
into the DB by Hibernate like everything else (see below).

**Schema source of truth is Hibernate, not `db-init/init.sql`.** `application.properties` sets
`spring.jpa.hibernate.ddl-auto=create-drop`, so every backend startup drops and regenerates the schema
from the `entity/` classes against the `gestionale_afam` database. `db-init/init.sql` creates a
differently-named database (`afam_db`) via the MySQL container's `docker-entrypoint-initdb.d`
mechanism — it documents an earlier/reference version of the schema but is **not** what the running
app actually uses. If you change a table shape, edit the `entity/` class; treat `init.sql` as
historical documentation unless asked to reconcile the two.

`AfamBackendApplication` also acts as a `CommandLineRunner` that wipes and reseeds sample data on every
boot — useful for demoing against a clean DB, but it means data doesn't persist across restarts.

## Frontend architecture

Angular 21 with standalone components only (no `NgModule`s) — see `app.config.ts` for the root providers
(`provideRouter`, `provideHttpClient`) and `app.routes.ts` for routing.

**This app is zoneless** — `zone.js` is not a dependency at all (check `package.json`/`node_modules`, not
just for a `provideZonelessChangeDetection()` call). This means plain component-field mutations made
inside an RxJS `.subscribe()` callback (e.g. after an HTTP response) will **not** trigger a re-render.
Any state that changes asynchronously after the initial render must be a `signal()`, read as `foo()` in
the template, not `foo`.

Route-level components are named after their sequence-diagram *Boundary* lifeline, not generic
Angular-CLI names (see "Sequence-diagram-driven development" above):

- `components/autenticazione/` → `AutenticazioneBoundary` — the auth landing page (route `/auth`).
- `components/registrazione/` → `RegistrazioneBoundary` — registration form (route `/registrati`); also
  absorbs the diagram's separate `FormBoundary` (folded into the same component, not a distinct class).
- `components/login/` → `LoginBoundary` — login form (route `/login`); multi-step (credentials → OTP)
  driven by a `signal<'credenziali' | 'conferma' | 'otp'>`, also absorbs its `FormBoundary`.
- `components/home-page/` → `HomePageBoundary` — post-login landing (route `/home`).
- `components/popup-errore/` → `PopupErroreBoundary` — generic error popup, reused across flows.
- `components/popup-conferma/` → `PopupConfermaBoundary` — generic confirmation popup.
- `components/password-recovery/` → `PasswordRecoveryComponent` — not yet implemented against a diagram,
  still the CLI-generated shell/name; rename once `Recupero_Password.png` is implemented.

`services/` holds the frontend-side Controller classes from the diagrams (`AutenticazioneController`,
`RegistrazioneController`, `LoginController` — note these share a name with, but are separate from, their
Spring Boot backend counterparts in `afam-backend`, split across the network boundary as described above).

**Popup components must position `:host` itself**, not a wrapper `<div class="overlay">` inside the
template — a child with `position: fixed` doesn't contribute to its parent's layout size under *any*
display mode, so a wrapper-based popup silently renders with a zero-size host (invisible to visibility
checks, though it still paints on screen). Put `position: fixed; inset: 0; ...` directly on `:host` in the
component's CSS.

Styling is Tailwind CSS v4 via PostCSS (`.postcssrc.json`), not the Angular Material/SCSS defaults —
though most components so far use plain CSS rather than Tailwind utility classes. Prettier
(`.prettierrc`) enforces single quotes, 100-char width, and the Angular parser for `*.html` templates.
