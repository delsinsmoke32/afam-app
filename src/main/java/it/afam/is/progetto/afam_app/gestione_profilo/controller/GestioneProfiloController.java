package it.afam.is.progetto.afam_app.gestione_profilo.controller;

import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.autenticazione.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.common.PopupSuccessoBoundary;

public class GestioneProfiloController {

    private final AutenticazioneBoundary autenticazioneBoundary;

    public GestioneProfiloController(AutenticazioneBoundary autenticazioneBoundary) {
        this.autenticazioneBoundary = autenticazioneBoundary;
    }

    public void logout() {
        Sessione.getInstance().logout();

        PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();
        popupSuccessoBoundary.mostraPopup("Logout effettuato con successo.");

        autenticazioneBoundary.mostraAutenticazione();
    }
}



