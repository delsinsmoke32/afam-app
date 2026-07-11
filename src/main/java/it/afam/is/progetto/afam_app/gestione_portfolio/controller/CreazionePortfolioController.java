package it.afam.is.progetto.afam_app.gestione_portfolio.controller;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.FormCreazionePortfolioBoundary;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.GestionePortfolioBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.common.PopupSuccessoBoundary;

public class CreazionePortfolioController {

    private final GestionePortfolioBoundary gestionePortfolioBoundary;
    private final DBMSBoundary dbmsBoundary;

    private FormCreazionePortfolioBoundary formCreazionePortfolioBoundary;

    public CreazionePortfolioController(
            GestionePortfolioBoundary gestionePortfolioBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.gestionePortfolioBoundary = gestionePortfolioBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void avviaCreazionePortfolio() {
        // <<create>> FormCreazionePortfolioBoundary
        formCreazionePortfolioBoundary = new FormCreazionePortfolioBoundary(this);

        // mostraForm()
        formCreazionePortfolioBoundary.mostraForm();
    }

    public void mandaDati(String titolo, String descrizione) {
        if (formCreazionePortfolioBoundary != null) {
            // <<destroy>> FormCreazionePortfolioBoundary
            formCreazionePortfolioBoundary.dispose();
            formCreazionePortfolioBoundary = null;
        }

        boolean datiValidi = verificaDati(titolo, descrizione);

        // alt [datiValidi = false]
        if (!datiValidi) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Titolo e descrizione non validi.");

            // cliccaOK() gestito dal popup

            // mostraGestionePortfolio()
            gestionePortfolioBoundary.mostraGestionePortfolio();
            return;
        }

        StudenteEntity studente = Sessione.getStudenteCorrente();

        if (studente == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Nessuno studente in sessione.");

            gestionePortfolioBoundary.mostraGestionePortfolio();
            return;
        }

        // <<create>> PortfolioEntity
        PortfolioEntity portfolio = PortfolioEntity.builder()
                .titolo(titolo.trim())
                .descrizione(descrizione.trim())
                .studente(studente)
                .build();

        // creaPortfolio(PortfolioEntity)
        dbmsBoundary.creaPortfolio(portfolio);

        PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();
        popupSuccessoBoundary.mostraPopup("PortfolioEntity creato con successo.");

        // cliccaOK() gestito dal popup

        // mostraGestionePortfolio()
        gestionePortfolioBoundary.mostraGestionePortfolio();
    }

    public boolean verificaDati(String titolo, String descrizione) {
        return titolo != null
                && descrizione != null
                && !titolo.trim().isEmpty()
                && !descrizione.trim().isEmpty();
    }
}

