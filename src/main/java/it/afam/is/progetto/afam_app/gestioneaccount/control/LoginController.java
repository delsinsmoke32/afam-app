package it.afam.is.progetto.afam_app.gestioneaccount.control;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.boundary.EmailBoundary;
import it.afam.is.progetto.afam_app.entity.CodiceOTPEntity;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.FormLoginBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.FormOTPBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.LoginBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PaginaPersonaleBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupSuccessoBoundary;

public class LoginController {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final AutenticazioneBoundary autenticazioneBoundary;
    private final DBMSBoundary dbmsBoundary;
    private final EmailBoundary emailBoundary;

    private StudenteEntity studente;
    private String codiceOTP;
    private LocalDateTime scadenzaOTP;

    public LoginController(
            AutenticazioneBoundary autenticazioneBoundary,
            DBMSBoundary dbmsBoundary,
            EmailBoundary emailBoundary
    ) {
        this.autenticazioneBoundary = autenticazioneBoundary;
        this.dbmsBoundary = dbmsBoundary;
        this.emailBoundary = emailBoundary;

        LoginBoundary loginBoundary = new LoginBoundary();
        loginBoundary.mostraLogin();

        FormLoginBoundary formLoginBoundary = new FormLoginBoundary(this);
        formLoginBoundary.MostraFormLogin();
    }

    public void mandaCredenziale(String mail, String password) {
        boolean credenzialiValide = verificaCredenziali(mail, password);

        if (!credenzialiValide) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Inserisci mail e password.");
            autenticazioneBoundary.MostraAutenticazione();
            return;
        }

        studente = dbmsBoundary.verificaEsistenzaCredenziali(mail, password);

        if (studente != null) {
            scadenzaOTP = LocalDateTime.now().plusMinutes(3);

            // generaCodiceOTP(studente_id, scadenza)
            CodiceOTPEntity codiceOTPEntity = generaCodiceOTP(studente.getId(), scadenzaOTP);

            // salvaOTP(codiceOTP)
            dbmsBoundary.salvaOTP(codiceOTPEntity);

            // InviaEmail(destinatario, oggetto, corpo)
            emailBoundary.InviaEmail(
                    studente.getEmail(),
                    "Codice OTP AFAM",
                    "Il tuo codice OTP è: " + codiceOTP
            );

            PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();
            popupSuccessoBoundary.mostraSuccesso();

            FormOTPBoundary formOTPBoundary = new FormOTPBoundary(this);
            formOTPBoundary.mostraFormOTP();
        } else {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Credenziali errate.");
            autenticazioneBoundary.MostraAutenticazione();
        }
    }

    public boolean verificaCredenziali(String mail, String password) {
        return mail != null
                && password != null
                && !mail.trim().isEmpty()
                && !password.trim().isEmpty();
    }

    public CodiceOTPEntity generaCodiceOTP(Long studente_id, LocalDateTime scadenza) {
        int codice = RANDOM.nextInt(1_000_000);
        codiceOTP = String.format("%06d", codice);

        return CodiceOTPEntity.builder()
                .studente(studente)
                .codice(codiceOTP)
                .scadenza(scadenza)
                .build();
    }

    public void mandaOTP(String OTP) {
        // ControllaOTP(OTP, codiceOTP)
        boolean otpCorretto = ControllaOTP(OTP, codiceOTP);

        if (otpCorretto) {
            Sessione sessione = new Sessione();

            // login(studente)
            sessione.login(studente);

            PaginaPersonaleBoundary paginaPersonaleBoundary =
                    new PaginaPersonaleBoundary(autenticazioneBoundary, dbmsBoundary);

            // mostraPaginaPersonale(studente)
            paginaPersonaleBoundary.mostraPaginaPersonale(studente);
        } else {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("OTP errato o scaduto.");
            autenticazioneBoundary.MostraAutenticazione();
        }
    }

    public boolean ControllaOTP(String OTP, String codiceOTP) {
        return OTP != null
                && codiceOTP != null
                && OTP.trim().equals(codiceOTP)
                && scadenzaOTP != null
                && LocalDateTime.now().isBefore(scadenzaOTP);
    }
}