package it.afam.is.progetto.afam_app.gestioneaccount.control;

import org.springframework.stereotype.Service;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.dto.CredenzialiRegistrazione;

@Service
public class RegistrazioneControl {

    private final DBMSBoundary dbmsBoundary;

    public RegistrazioneControl(DBMSBoundary dbmsBoundary) {
        this.dbmsBoundary = dbmsBoundary;
    }

    public String inserisciCredenziali(CredenzialiRegistrazione credenziali) {

        if (credenziali == null) {
            return "Credenziali non valide.";
        }

        if (campoVuoto(credenziali.getNome())
                || campoVuoto(credenziali.getCognome())
                || campoVuoto(credenziali.getEmail())
                || campoVuoto(credenziali.getPassword())
                || campoVuoto(credenziali.getCodiceFiscale())) {
            return "Compila tutti i campi obbligatori.";
        }

        if (!emailValida(credenziali.getEmail())) {
            return "Email non valida.";
        }

        if (credenziali.getCodiceFiscale().trim().length() != 16) {
            return "Il codice fiscale deve contenere 16 caratteri.";
        }

        if (dbmsBoundary.esisteEmail(credenziali.getEmail())) {
            return "Email già registrata.";
        }

        if (dbmsBoundary.esisteCodiceFiscale(credenziali.getCodiceFiscale())) {
            return "Codice fiscale già registrato.";
        }

        dbmsBoundary.mandaCredenziali(credenziali);

        return null;
    }

    private boolean campoVuoto(String valore) {
        return valore == null || valore.trim().isEmpty();
    }

    private boolean emailValida(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}