package it.afam.is.progetto.afam_app.autenticazione.controller;

import java.time.LocalDateTime;
import java.util.Random;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.EmailBoundary;
import it.afam.is.progetto.afam_app.entity.CodiceOTPEntity;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.autenticazione.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.autenticazione.boundary.FormRecPwdBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.common.PopupSuccessoBoundary;
import it.afam.is.progetto.afam_app.autenticazione.boundary.PwdRecBoundary;

public class PwdRecController {

    private final AutenticazioneBoundary autenticazioneBoundary;
    private final DBMSBoundary dbmsBoundary;
    private final EmailBoundary emailBoundary;

    private PwdRecBoundary pwdRecBoundary;
    private FormRecPwdBoundary formRecPwdBoundary;

    private StudenteEntity studente;
    private CodiceOTPEntity codiceOTP;

    public PwdRecController(
            AutenticazioneBoundary autenticazioneBoundary,
            DBMSBoundary dbmsBoundary,
            EmailBoundary emailBoundary
    ) {
        this.autenticazioneBoundary = autenticazioneBoundary;
        this.dbmsBoundary = dbmsBoundary;
        this.emailBoundary = emailBoundary;

        // <<create>> PwdRecBoundary
        pwdRecBoundary = new PwdRecBoundary(this);

        // mostraPwdRec()
        pwdRecBoundary.mostraPwdRec();
    }

    // Estratto di PwdRecController.java - Metodo richiediReset
    public void richiediReset(String mail) {
        studente = dbmsBoundary.verificaEsistenza(mail);

        if (studente != null) {
            LocalDateTime scadenza = LocalDateTime.now().plusMinutes(5);
            codiceOTP = generaCodiceOTP(studente.getId(), scadenza);
            dbmsBoundary.salvaOTP(codiceOTP);

            String destinatario = mail;
            String oggetto = "Recupero password AFAM";
            // Assicuriamoci di passare la stringa del codice
            String corpo = "Il tuo codice OTP per recuperare la password è: " + codiceOTP.getCodice();

            // Chiamata conforme al Sequence Diagram
            emailBoundary.InviaEmail(destinatario, oggetto, corpo);

            PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();
            popupSuccessoBoundary.mostraPopup("Codice OTP inviato via email.");
            pwdRecBoundary.mostraFormOTP();
        } else {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Email non trovata.");
            autenticazioneBoundary.mostraAutenticazione();
        }
    }

    public CodiceOTPEntity generaCodiceOTP(Long studente_id, LocalDateTime scadenza) {
        String codice = String.format("%06d", new Random().nextInt(999999));

        return CodiceOTPEntity.builder()
                .studente(studente)
                .codice(codice)
                .scadenza(scadenza)
                .build();
    }

    public void mandaOTP(String OTP) {
        // controllaOTP(OTP, codiceOTP)
        boolean otpCorretto = controllaOTP(OTP, codiceOTP);

        // alt
        // [OTP == codiceOTP.codice]
        if (otpCorretto) {

            // <<create>> FormRecPwdBoundary
            formRecPwdBoundary = new FormRecPwdBoundary(this);

            // mostraFormRecPwd()
            formRecPwdBoundary.mostraFormRecPwd();

        } else {
            // [else]
            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Codice OTP errato.");

            // mostraAutenticazione()
            autenticazioneBoundary.mostraAutenticazione();
        }
    }

    public boolean controllaOTP(String OTP, CodiceOTPEntity codiceOTP) {
        if (OTP == null || codiceOTP == null || codiceOTP.getCodice() == null) {
            return false;
        }

        return OTP.equals(codiceOTP.getCodice());
    }

    public void mandaPwd(String password, String conf_password) {
        // controllaPwd(password, conf_password)
        boolean pwdCorretta = controllaPwd(password, conf_password);

        // alt
        // [password = conf_password && password != ""]
        if (pwdCorretta) {

            Long studente_id = studente.getId();

            // salvaPwd(userID, password)
            dbmsBoundary.salvaPwd(studente_id, password);

            // mostraAutenticazione()
            autenticazioneBoundary.mostraAutenticazione();

        } else {
            // [else]
            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Le password non coincidono o sono vuote.");

            // mostraAutenticazione()
            autenticazioneBoundary.mostraAutenticazione();
        }
    }

    public boolean controllaPwd(String password, String conf_password) {
        if (password == null || conf_password == null) {
            return false;
        }

        return password.equals(conf_password) && !password.trim().isEmpty();
    }
}

