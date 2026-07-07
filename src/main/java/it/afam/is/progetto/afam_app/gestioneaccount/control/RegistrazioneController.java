package it.afam.is.progetto.afam_app.gestioneaccount.control;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.FormRegistrazioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.dto.CredenzialiRegistrazione;

public class RegistrazioneController {

    private final AutenticazioneBoundary autenticazioneBoundary;
    private final DBMSBoundary dbmsBoundary;
    private final FormRegistrazioneBoundary formRegistrazioneBoundary;

    public RegistrazioneController(AutenticazioneBoundary autenticazioneBoundary, DBMSBoundary dbmsBoundary) {
        this.autenticazioneBoundary = autenticazioneBoundary;
        this.dbmsBoundary = dbmsBoundary;

        this.formRegistrazioneBoundary = new FormRegistrazioneBoundary(this);
        this.formRegistrazioneBoundary.mostraFormReg();
    }

    public void mandaCredenziali(CredenzialiRegistrazione credenziali) {
        if (!verificaCredenzialiNotEmpty(credenziali)) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopupErrore();

            autenticazioneBoundary.mostraAutenticazione();
            return;
        }

        boolean verificaCredenziali = verificaCredenziali(credenziali);

        if (verificaCredenziali) {
            // <<create>> StudenteEntity
            StudenteEntity studente = StudenteEntity.builder()
                    .nome(credenziali.getNome().trim())
                    .cognome(credenziali.getCognome().trim())
                    .email(credenziali.getEmail().trim())
                    .password(credenziali.getPassword())
                    .codiceFiscale(credenziali.getCodiceFiscale().trim().toUpperCase())
                    .corsoDiStudi(credenziali.getCorsoDiStudi())
                    .provider_autenticazione("LOCAL")
                    .build();

            // inserisciStudente(studente)
            inserisciStudente(studente);

            autenticazioneBoundary.mostraAutenticazione();
        } else {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Credenziali non valide o studente già registrato.");

            autenticazioneBoundary.mostraAutenticazione();
        }
    }

    public void inserisciStudente(StudenteEntity studente) {
        // insertInserisciStudente(studente)
        dbmsBoundary.insertInserisciStudente(studente);
    }

    public boolean verificaCredenzialiNotEmpty(CredenzialiRegistrazione credenziali) {
        return credenziali != null
                && !campoVuoto(credenziali.getNome())
                && !campoVuoto(credenziali.getCognome())
                && !campoVuoto(credenziali.getEmail())
                && !campoVuoto(credenziali.getPassword())
                && !campoVuoto(credenziali.getCodiceFiscale());
    }

    public boolean verificaCredenziali(CredenzialiRegistrazione credenziali) {
        return emailValida(credenziali.getEmail())
                && codiceFiscaleValido(credenziali.getCodiceFiscale())
                && !dbmsBoundary.esisteEmail(credenziali.getEmail().trim())
                && !dbmsBoundary.esisteCodiceFiscale(credenziali.getCodiceFiscale().trim().toUpperCase());
    }

    private boolean campoVuoto(String valore) {
        return valore == null || valore.trim().isEmpty();
    }

    private boolean emailValida(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean codiceFiscaleValido(String codiceFiscale) {
        return codiceFiscale != null && codiceFiscale.trim().length() == 16;
    }
}


