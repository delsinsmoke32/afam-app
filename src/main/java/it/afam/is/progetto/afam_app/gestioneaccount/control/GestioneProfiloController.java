package it.afam.is.progetto.afam_app.gestioneaccount.control;

import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupSuccessoBoundary;

public class GestioneProfiloController {

    private final AutenticazioneBoundary autenticazioneBoundary;

    public GestioneProfiloController(AutenticazioneBoundary autenticazioneBoundary) {
        this.autenticazioneBoundary = autenticazioneBoundary;
    }

    public void logout() {
        Sessione sessione = new Sessione();
        sessione.logout();

        PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();
        popupSuccessoBoundary.mostraPopup("Logout effettuato con successo.");

        autenticazioneBoundary.mostraAutenticazione();
    }
}



