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

        Long studente_id = Sessione.getStudenteId();

        // Recupero i dati correnti dal database in formato mappa
        // NOTA BENE: Se nel tuo DBMSBoundary questo metodo si chiama in un altro modo,
        // ricordati di rinominarlo di conseguenza!
        Map<String, String> datiCorrenti = dbmsBoundary.recuperaMappaDatiStudente(studente_id);

        // mostraForm() passandogli i dati correnti per precompilare i campi
        formModDatiBoundary.mostraForm(datiCorrenti);
    }

    public void mandaDati(Map<String, String> dati) {
        // <<destroy>> FormModDatiBoundary
        if (formModDatiBoundary != null) {
            formModDatiBoundary.dispose();
            formModDatiBoundary = null;
        }

        boolean datiIsEmpty = verificaDati(dati);

        if (!datiIsEmpty) {

            boolean datiIsValid = validaDati(dati);

            if (datiIsValid) {
                Long studente_id = Sessione.getStudenteId();

                // aggiornaDati(studente_id, dati)
                dbmsBoundary.aggiornaDati(studente_id, dati);

                gestioneProfiloBoundary.mostraGestioneProfilo();
            } else {
                PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
                popupErroreBoundary.mostraPopup("Dati personali non validi.");
                gestioneProfiloBoundary.mostraGestioneProfilo();
            }

        } else {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("I dati personali non possono essere tutti vuoti.");
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

        return true;
    }

    private boolean isBlank(String valore) {
        return valore == null || valore.trim().isEmpty();
    }
}