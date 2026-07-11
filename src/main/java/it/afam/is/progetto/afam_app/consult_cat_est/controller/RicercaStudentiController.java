package it.afam.is.progetto.afam_app.consult_cat_est.controller;

import java.util.List;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.HomepageBoundary;
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.PaginaRicercaBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;

public class RicercaStudentiController {

    private final HomepageBoundary homepageBoundary;
    private final DBMSBoundary dbmsBoundary;

    private String ricerca;

    public RicercaStudentiController(
            HomepageBoundary homepageBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.homepageBoundary = homepageBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void setRicerca(String ricerca) {
        this.ricerca = ricerca;
    }

    public void ricercaStudente(String ricerca) {
        this.ricerca = ricerca;

        // ricercaStudente(ricerca)
        List<StudenteEntity> risultatiRicerca =
                dbmsBoundary.ricercaStudente(ricerca);

        if (risultatiRicerca != null && risultatiRicerca.size() > 0) {
            // <<create>> PaginaRicercaBoundary
            PaginaRicercaBoundary paginaRicercaBoundary =
                    new PaginaRicercaBoundary(dbmsBoundary);

            // mostraPaginaRicerca(risultati)
            paginaRicercaBoundary.mostraPaginaRicerca(risultatiRicerca);
        } else {
            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Nessuno studente trovato.");
        }
    }

    public String getRicerca() {
        return ricerca;
    }
}