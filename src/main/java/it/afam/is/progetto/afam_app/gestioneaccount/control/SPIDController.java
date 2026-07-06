package it.afam.is.progetto.afam_app.gestioneaccount.control;

import java.lang.reflect.Method;
import java.util.Map;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.entity.Studente;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.FormSPIDBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.ListaProviderBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PaginaPersonaleBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.ProviderBoundary;

public class SPIDController {

    private final AutenticazioneBoundary autenticazioneBoundary;
    private final DBMSBoundary dbmsBoundary;

    private ListaProviderBoundary listaProviderBoundary;
    private ProviderBoundary providerBoundary;
    private FormSPIDBoundary formSPIDBoundary;

    private Map<String, String> attestazione;
    private Map<String, String> dati;
    private Studente studente;

    public SPIDController(
            AutenticazioneBoundary autenticazioneBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.autenticazioneBoundary = autenticazioneBoundary;
        this.dbmsBoundary = dbmsBoundary;

        // <<create>> ListaProviderBoundary
        listaProviderBoundary = new ListaProviderBoundary(this);

        // MostraProviderSPID()
        listaProviderBoundary.MostraProviderSPID();
    }

    public void providerScelto(String provider_id) {
        // reindirizzaAProvider(provider_id)
        providerBoundary = new ProviderBoundary();
        attestazione = providerBoundary.reindirizzaAProvider(provider_id);

        // <<destroy>> ListaProviderBoundary
        if (listaProviderBoundary != null) {
            listaProviderBoundary.dispose();
            listaProviderBoundary = null;
        }

        // convalidaAttestazione(attestazione)
        boolean attestazioneIsValid = convalidaAttestazione(attestazione);

        // alt : attestazione isValid
        // [True]
        if (attestazioneIsValid) {

            // estraiDati(attestazione)
            dati = estraiDati(attestazione);

            // codiceFiscaleExists(dati.codiceFiscale)
            studente = dbmsBoundary.codiceFiscaleExists(dati.get("codiceFiscale"));

            // alt
            // [studente = NULL]
            if (studente == null) {

                // <<create>> FormSPIDBoundary
                formSPIDBoundary = new FormSPIDBoundary(this);

                // mostraForm()
                formSPIDBoundary.mostraForm();

            } else {
                // studente già presente: login diretto
                login(studente);
                mostraPaginaPersonale(studente);
            }

        } else {
            // [False]
            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Attestazione SPID/eIDAS non valida.");

            // Utente -> PopupErroreBoundary: CliccaOK()
            // gestito dal popup

            // mostraAutenticazione()
            autenticazioneBoundary.mostraAutenticazione();
        }
    }

    public boolean convalidaAttestazione(Map<String, String> attestazione) {
        if (attestazione == null) {
            return false;
        }

        return "true".equals(attestazione.get("valid"));
    }

    public Map<String, String> estraiDati(Map<String, String> attestazione) {
        return attestazione;
    }

    public void mandaDatiAgg(String CdS, String bio, String link) {
        // <<create>> StudenteEntity
        // Nel codice reale StudenteEntity = Studente
        studente = Studente.builder()
                .nome(dati.get("nome"))
                .cognome(dati.get("cognome"))
                .email(dati.get("email"))
                .password("")
                .codiceFiscale(dati.get("codiceFiscale"))
                .corsoDiStudi(CdS)
                .provider_autenticazione(dati.get("provider"))
                .build();

        setBioSeEsiste(studente, bio);
        setLinkEsternoSeEsiste(studente, link);

        // memorizzaDati(studente)
        dbmsBoundary.memorizzaDati(studente);

        // <<destroy>> FormSPIDBoundary
        if (formSPIDBoundary != null) {
            formSPIDBoundary.dispose();
            formSPIDBoundary = null;
        }

        // login(studente)
        login(studente);

        // mostraPaginaPersonale(studente)
        mostraPaginaPersonale(studente);
    }

    public void login(Studente studente) {
        Sessione sessione = new Sessione();
        sessione.login(studente);
    }

  public void mostraPaginaPersonale(Studente studente) {
    // <<create>> PaginaPersonaleBoundary
    PaginaPersonaleBoundary paginaPersonaleBoundary =
            new PaginaPersonaleBoundary(autenticazioneBoundary, dbmsBoundary);

    // mostraPaginaPersonale(studente)
    paginaPersonaleBoundary.mostraPaginaPersonale(studente);
}

    private void setBioSeEsiste(Studente studente, String bio) {
        try {
            Method metodoSetBio = Studente.class.getMethod("setBio", String.class);
            metodoSetBio.invoke(studente, bio);
        } catch (Exception ignored) {
        }
    }

    private void setLinkEsternoSeEsiste(Studente studente, String link) {
        try {
            Method metodoSetLink = Studente.class.getMethod("setLinkEsterno", String.class);
            metodoSetLink.invoke(studente, link);
        } catch (Exception ignored) {
        }
    }
}