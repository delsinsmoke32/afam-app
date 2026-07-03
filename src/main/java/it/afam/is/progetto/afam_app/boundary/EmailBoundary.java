package it.afam.is.progetto.afam_app.boundary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailBoundary {

    private static final Logger log = LoggerFactory.getLogger(EmailBoundary.class);

    // Messaggio 11 del Sequence Diagram Login2FA: InviaEmail(userEmail, codice)
    // Invio email reale fuori scope per ora: si registra solo il codice a log.
    public void inviaEmail(String userEmail, String codice) {
        log.info("=== EMAIL (simulata) a {}: il tuo codice OTP e' {} ===", userEmail, codice);
    }
}
