package it.afam.is.progetto.afam_app.autenticazione.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.autenticazione.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.autenticazione.boundary.FormRegistrazioneBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.dto.CredenzialiRegistrazione;

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
            new PopupErroreBoundary().mostraPopupErrore("Compila tutti i campi obbligatori.");
            autenticazioneBoundary.mostraAutenticazione();
            return;
        }

        boolean verificaCredenziali = verificaCredenziali(credenziali);

        if (verificaCredenziali) {

            // Gestione sicura della data di nascita
            LocalDate dataNascitaParsed = null;
            try {
                if (credenziali.getDataDiNascita() != null && !credenziali.getDataDiNascita().trim().isEmpty()) {
                    dataNascitaParsed = LocalDate.parse(credenziali.getDataDiNascita().trim());
                }
            } catch (DateTimeParseException e) {
                new PopupErroreBoundary().mostraPopup("Formato data errato. Usa YYYY-MM-DD.");
                autenticazioneBoundary.mostraAutenticazione();
                return;
            }

            // <<create>> StudenteEntity
            StudenteEntity studente = StudenteEntity.builder()
                    .nome(credenziali.getNome().trim())
                    .cognome(credenziali.getCognome().trim())
                    .email(credenziali.getEmail().trim())
                    .password(credenziali.getPassword())
                    .codiceFiscale(credenziali.getCodiceFiscale().trim().toUpperCase())
                    .corsoDiStudi(credenziali.getCorsoDiStudi())
                    // Nuovi campi:
                    .dataDiNascita(dataNascitaParsed)
                    .linkPersonale(credenziali.getLinkPersonale())
                    .biografia(credenziali.getBiografia())
                    .provider_autenticazione("LOCAL")
                    .build();

            // inserisciStudente(studente)
            inserisciStudente(studente);

            autenticazioneBoundary.mostraAutenticazione();
        } else {
            new PopupErroreBoundary().mostraPopup("Credenziali non valide o studente già registrato.");
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