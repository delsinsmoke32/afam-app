package it.afam.is.progetto.afam_app.gestioneaccount.control;

import java.lang.reflect.Method;
import java.util.Map;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
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
    private StudenteEntity StudenteEntity;

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
            StudenteEntity = dbmsBoundary.codiceFiscaleExists(dati.get("codiceFiscale"));

            // alt
            // [StudenteEntity = NULL]
            if (StudenteEntity == null) {

                // <<create>> FormSPIDBoundary
                formSPIDBoundary = new FormSPIDBoundary(this);

                // mostraForm()
                formSPIDBoundary.mostraForm();

            } else {
                // StudenteEntity già presente: login diretto
                login(StudenteEntity);
                mostraPaginaPersonale(StudenteEntity);
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
        // Nel codice reale StudenteEntity = StudenteEntity
        StudenteEntity = StudenteEntity.builder()
                .nome(dati.get("nome"))
                .cognome(dati.get("cognome"))
                .email(dati.get("email"))
                .password("")
                .codiceFiscale(dati.get("codiceFiscale"))
                .corsoDiStudi(CdS)
                .provider_autenticazione(dati.get("provider"))
                .build();

        setBioSeEsiste(StudenteEntity, bio);
        setLinkEsternoSeEsiste(StudenteEntity, link);

        // memorizzaDati(StudenteEntity)
        dbmsBoundary.memorizzaDati(StudenteEntity);

        // <<destroy>> FormSPIDBoundary
        if (formSPIDBoundary != null) {
            formSPIDBoundary.dispose();
            formSPIDBoundary = null;
        }

        // login(StudenteEntity)
        login(StudenteEntity);

        // mostraPaginaPersonale(StudenteEntity)
        mostraPaginaPersonale(StudenteEntity);
    }

    public void login(StudenteEntity StudenteEntity) {
        Sessione sessione = new Sessione();
        sessione.login(StudenteEntity);
    }

  public void mostraPaginaPersonale(StudenteEntity StudenteEntity) {
    // <<create>> PaginaPersonaleBoundary
    PaginaPersonaleBoundary paginaPersonaleBoundary =
            new PaginaPersonaleBoundary(autenticazioneBoundary, dbmsBoundary);

    // mostraPaginaPersonale(StudenteEntity)
    paginaPersonaleBoundary.mostraPaginaPersonale(StudenteEntity);
}

    private void setBioSeEsiste(StudenteEntity StudenteEntity, String bio) {
        try {
            Method metodoSetBio = StudenteEntity.class.getMethod("setBio", String.class);
            metodoSetBio.invoke(StudenteEntity, bio);
        } catch (Exception ignored) {
        }
    }

    private void setLinkEsternoSeEsiste(StudenteEntity StudenteEntity, String link) {
        try {
            Method metodoSetLink = StudenteEntity.class.getMethod("setLinkEsterno", String.class);
            metodoSetLink.invoke(StudenteEntity, link);
        } catch (Exception ignored) {
        }
    }
}



