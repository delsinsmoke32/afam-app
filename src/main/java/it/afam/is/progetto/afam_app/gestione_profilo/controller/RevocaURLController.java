package it.afam.is.progetto.afam_app.gestione_profilo.controller;

import java.util.List;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.LinkCondivisoEntity;
import it.afam.is.progetto.afam_app.gestione_profilo.boundary.GestioneProfiloBoundary;
import it.afam.is.progetto.afam_app.gestione_profilo.boundary.ListaUrlBoundary;
import it.afam.is.progetto.afam_app.common.PopupConfermaBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;

public class RevocaURLController {

    private final GestioneProfiloBoundary gestioneProfiloBoundary;
    private final DBMSBoundary dbmsBoundary;

    private ListaUrlBoundary listaUrlBoundary;
    private PopupConfermaBoundary popupConfermaBoundary;

    private LinkCondivisoEntity URL;

    public RevocaURLController(
            GestioneProfiloBoundary gestioneProfiloBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.gestioneProfiloBoundary = gestioneProfiloBoundary;
        this.dbmsBoundary = dbmsBoundary;

        avviaRevocaURL();
    }

    public void avviaRevocaURL() {
        // cercaURLAttivi()
        List<LinkCondivisoEntity> elencoUrl = dbmsBoundary.cercaURLAttivi();

        if (elencoUrl != null && !elencoUrl.isEmpty()) {
            // <<create>> ListaUrlBoundary
            listaUrlBoundary = new ListaUrlBoundary(this);

            // mostraLista(elencoUrl)
            listaUrlBoundary.mostraLista(elencoUrl);
        } else {
            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Non ci sono URL attivi da revocare.");

            // mostraGestioneProfilo()
            gestioneProfiloBoundary.mostraGestioneProfilo();
        }
    }

    public void mandaUrl(LinkCondivisoEntity URL) {
        this.URL = URL;

        // <<create>> PopupConfermaBoundary
        popupConfermaBoundary = new PopupConfermaBoundary(
                () -> conferma(),
                () -> annulla()
        );

        // mostraPopup(testo)
        popupConfermaBoundary.mostraPopup("Vuoi revocare l'URL selezionato?");
    }

    public void conferma() {
        if (URL == null || URL.getId() == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("URL non valido.");
            return;
        }

        if (popupConfermaBoundary != null) {
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        if (listaUrlBoundary != null) {
            // <<destroy>> ListaUrlBoundary
            listaUrlBoundary.dispose();
            listaUrlBoundary = null;
        }

        Long id_url = URL.getId();

        // invalidaURL(id_url)
        dbmsBoundary.invalidaURL(id_url);

        // mostraGestioneProfilo()
        gestioneProfiloBoundary.mostraGestioneProfilo();
    }

    public void annulla() {
        if (popupConfermaBoundary != null) {
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        if (listaUrlBoundary != null) {
            // <<destroy>> ListaUrlBoundary
            listaUrlBoundary.dispose();
            listaUrlBoundary = null;
        }

        // mostraGestioneProfilo()
        gestioneProfiloBoundary.mostraGestioneProfilo();
    }
}