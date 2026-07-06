package it.afam.is.progetto.afam_app.gestioneaccount.control;

import java.util.Map;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.FormModDatiBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.GestioneProfiloBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;

public class ModDatiPersController {

    private final GestioneProfiloBoundary gestioneProfiloBoundary;
    private final DBMSBoundary dbmsBoundary;

    private FormModDatiBoundary formModDatiBoundary;

    public ModDatiPersController(
            GestioneProfiloBoundary gestioneProfiloBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.gestioneProfiloBoundary = gestioneProfiloBoundary;
        this.dbmsBoundary = dbmsBoundary;

        // <<create>> FormModDatiBoundary
        formModDatiBoundary = new FormModDatiBoundary(this);

        // mostraForm()
        formModDatiBoundary.mostraForm();
    }

    public void mandaDati(Map<String, String> dati) {
        // <<destroy>> FormModDatiBoundary
        if (formModDatiBoundary != null) {
            formModDatiBoundary.dispose();
            formModDatiBoundary = null;
        }

        // verificaDati(dati)
        boolean datiIsEmpty = verificaDati(dati);

        // alt : dati isEmpty
        // [False]
        if (!datiIsEmpty) {

            // validaDati(dati)
            boolean datiIsValid = validaDati(dati);

            // alt : dati isValid
            // [True]
            if (datiIsValid) {

                Long studente_id = Sessione.getStudenteId();

                // aggiornaDati(studente_id, dati)
                dbmsBoundary.aggiornaDati(studente_id, dati);

                // mostraGestioneProfilo()
                gestioneProfiloBoundary.mostraGestioneProfilo();

            } else {
                // [False]
                // <<create>> PopupErroreBoundary
                PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

                // mostraPopup(testo)
                popupErroreBoundary.mostraPopup("Dati personali non validi.");

                // Studente -> PopupErroreBoundary: cliccaOK()
                // gestito dal popup

                // mostraGestioneProfilo()
                gestioneProfiloBoundary.mostraGestioneProfilo();
            }

        } else {
            // ramo dati vuoti
            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("I dati personali non possono essere vuoti.");

            // Studente -> PopupErroreBoundary: cliccaOK()
            // gestito dal popup

            // mostraGestioneProfilo()
            gestioneProfiloBoundary.mostraGestioneProfilo();
        }
    }

    public boolean verificaDati(Map<String, String> dati) {
        if (dati == null) {
            return true;
        }

        return isBlank(dati.get("nome"))
                && isBlank(dati.get("cognome"))
                && isBlank(dati.get("CdS"))
                && isBlank(dati.get("bio"));
    }

    public boolean validaDati(Map<String, String> dati) {
        if (dati == null) {
            return false;
        }

        if (isBlank(dati.get("nome"))) {
            return false;
        }

        if (isBlank(dati.get("cognome"))) {
            return false;
        }

        if (isBlank(dati.get("CdS"))) {
            return false;
        }

        return true;
    }

    private boolean isBlank(String valore) {
        return valore == null || valore.trim().isEmpty();
    }
}