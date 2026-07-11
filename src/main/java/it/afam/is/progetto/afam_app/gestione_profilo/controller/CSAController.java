package it.afam.is.progetto.afam_app.gestione_profilo.controller;

import java.util.List;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.entity.StatisticaAccessoEntity;
import it.afam.is.progetto.afam_app.gestione_profilo.boundary.GestioneProfiloBoundary;
import it.afam.is.progetto.afam_app.gestione_profilo.boundary.ListaStatisticheBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;

public class CSAController {

    private final GestioneProfiloBoundary gestioneProfiloBoundary;
    private final DBMSBoundary dbmsBoundary;

    private ListaStatisticheBoundary listaStatisticheBoundary;

    public CSAController(
            GestioneProfiloBoundary gestioneProfiloBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.gestioneProfiloBoundary = gestioneProfiloBoundary;
        this.dbmsBoundary = dbmsBoundary;

        // recuperaDati()
        recuperaDati();
    }

    public void recuperaDati() {
        Long studente_id = Sessione.getStudenteId();

        // getDatiStatistici(studente_id)
        List<StatisticaAccessoEntity> dati =
                dbmsBoundary.getDatiStatistici(studente_id);

        if (dati != null && !dati.isEmpty()) {
            // <<create>> ListaStatisticheBoundary
            listaStatisticheBoundary = new ListaStatisticheBoundary(this);

            // mostraLista(dati)
            listaStatisticheBoundary.mostraLista(dati);
        } else {
            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Non ci sono statistiche di accesso disponibili.");
        }
    }

    public void cliccaEsci() {
        if (listaStatisticheBoundary != null) {
            listaStatisticheBoundary.dispose();
            listaStatisticheBoundary = null;
        }

        // mostraGestioneProfilo()
        gestioneProfiloBoundary.mostraGestioneProfilo();
    }
}