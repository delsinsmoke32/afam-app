package it.afam.is.progetto.afam_app.gestione_profilo.controller;

import java.util.Map;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.gestione_profilo.boundary.FormModDatiBoundary;
import it.afam.is.progetto.afam_app.gestione_profilo.boundary.GestioneProfiloBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;

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
                PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
                popupErroreBoundary.mostraPopup("Dati personali non validi.");
                gestioneProfiloBoundary.mostraGestioneProfilo();
            }

        } else {
            // ramo dati vuoti
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("I dati personali non possono essere tutti vuoti.");
            gestioneProfiloBoundary.mostraGestioneProfilo();
        }
    }

    public boolean verificaDati(Map<String, String> dati) {
        if (dati == null) {
            return true;
        }

        // Modificato includendo il linkPersonale nel controllo dello stato vuoto
        return isBlank(dati.get("nome"))
                && isBlank(dati.get("cognome"))
                && isBlank(dati.get("CdS"))
                && isBlank(dati.get("linkPersonale"))
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

        // Nota: Il linkPersonale e la Bio rimangono opzionali per la validazione standard.
        // Se desideri renderli obbligatori, aggiungi qui il rispettivo controllo isBlank.

        return true;
    }

    private boolean isBlank(String valore) {
        return valore == null || valore.trim().isEmpty();
    }
}