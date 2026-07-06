package it.afam.is.progetto.afam_app.gestioneaccount.control;

import java.time.LocalDateTime;
import java.util.Random;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.boundary.EmailBoundary;
import it.afam.is.progetto.afam_app.entity.CodiceOTPEntity;
import it.afam.is.progetto.afam_app.entity.Studente;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.FormRecPwdBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupSuccessoBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PwdRecBoundary;

public class PwdRecController {

    private final AutenticazioneBoundary autenticazioneBoundary;
    private final DBMSBoundary dbmsBoundary;
    private final EmailBoundary emailBoundary;

    private PwdRecBoundary pwdRecBoundary;
    private FormRecPwdBoundary formRecPwdBoundary;

    private Studente studente;
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

    public void richiediReset(String mail) {
        // verificaEsistenza(mail)
        studente = dbmsBoundary.verificaEsistenza(mail);

        // alt
        // [studente != null]
        if (studente != null) {

            Long studente_id = studente.getId();
            LocalDateTime scadenza = LocalDateTime.now().plusMinutes(5);

            // generaCodiceOTP(studente_id, scadenza)
            codiceOTP = generaCodiceOTP(studente_id, scadenza);

            // salvaOTP(codiceOTP)
            dbmsBoundary.salvaOTP(codiceOTP);

            String destinatario = mail;
            String oggetto = "Recupero password AFAM";
            String corpo = "Il tuo codice OTP per recuperare la password è: " + codiceOTP.getCodice();

            // InviaEmail(destinatario, oggetto, corpo)
            emailBoundary.InviaEmail(destinatario, oggetto, corpo);

            // <<create>> PopupSuccessoBoundary
            PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();

            // mostraPopup(testo)
            popupSuccessoBoundary.mostraPopup("Codice OTP inviato via email.");

            // Utente -> PopupSuccessoBoundary: cliccaOK()
            // gestito dal popup

            // mostraFormOTP()
            pwdRecBoundary.mostraFormOTP();

        } else {
            // [else]
            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Email non trovata.");

            // Utente -> PopupErroreBoundary: CliccaOK()
            // gestito dal popup

            // mostraAutenticazione()
            autenticazioneBoundary.mostraAutenticazione();
        }
    }

    public CodiceOTPEntity generaCodiceOTP(Long studente_id, LocalDateTime scadenza) {
        String codice = String.format("%06d", new Random().nextInt(999999));

        return new CodiceOTPEntity(studente, codice, scadenza);
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

            // Utente -> PopupErroreBoundary: cliccaOK()
            // gestito dal popup

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

            // Utente -> PopupErroreBoundary: cliccaOK()
            // gestito dal popup

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