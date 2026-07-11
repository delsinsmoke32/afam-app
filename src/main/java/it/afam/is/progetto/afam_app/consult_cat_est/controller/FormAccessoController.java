package it.afam.is.progetto.afam_app.consult_cat_est.controller;

import java.time.LocalDateTime;

import it.afam.is.progetto.afam_app.consult_cat_est.boundary.FormAccessoBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;

public class FormAccessoController {

    private final PortfolioCondivisoController portfolioCondivisoController;
    private final FormAccessoBoundary formAccessoBoundary;

    public FormAccessoController(
            PortfolioCondivisoController portfolioCondivisoController,
            FormAccessoBoundary formAccessoBoundary
    ) {
        this.portfolioCondivisoController = portfolioCondivisoController;
        this.formAccessoBoundary = formAccessoBoundary;
    }

    public void mostraFormAccesso() {
        formAccessoBoundary.mostraFormAccesso();
    }

    public void mandaCampi(String nome, String cognome, String ruolo) {
        boolean verificaCampi = verificaCampi(nome, cognome, ruolo);

        // loop [finché !verificaCampi()]
        if (!verificaCampi) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Compila tutti i campi.");

            mostraFormAccesso();
            return;
        }

        LocalDateTime currentTime = getCurrentTime();

        // registraAccesso(nome, cognome, ruolo, currentTime)
        portfolioCondivisoController.registraAccesso(nome, cognome, ruolo, currentTime);

        // finito()
        portfolioCondivisoController.finito();

        formAccessoBoundary.dispose();
    }

    public boolean verificaCampi(String nome, String cognome, String ruolo) {
        return nome != null && !nome.trim().isEmpty()
                && cognome != null && !cognome.trim().isEmpty()
                && ruolo != null && !ruolo.trim().isEmpty();
    }

    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}