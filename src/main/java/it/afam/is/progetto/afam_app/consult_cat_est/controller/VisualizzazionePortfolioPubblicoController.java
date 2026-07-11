package it.afam.is.progetto.afam_app.consult_cat_est.controller;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.VisualizzazioneElencoStudentiBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.HomepageBoundary;
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.PaginaRicercaBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.VisualizzazionePortfolioPubblicoBoundary;

public class VisualizzazionePortfolioPubblicoController {

    private HomepageBoundary homepageBoundary;
    private PaginaRicercaBoundary paginaRicercaBoundary;
    private VisualizzazioneElencoStudentiBoundary visualizzazioneElencoStudentiBoundary;

    private final DBMSBoundary dbmsBoundary;

    private VisualizzazionePortfolioPubblicoBoundary visualizzazionePortfolioPubblicoBoundary;

    public VisualizzazionePortfolioPubblicoController(
            HomepageBoundary homepageBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.homepageBoundary = homepageBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public VisualizzazionePortfolioPubblicoController(
            PaginaRicercaBoundary paginaRicercaBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.paginaRicercaBoundary = paginaRicercaBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public VisualizzazionePortfolioPubblicoController(
            VisualizzazioneElencoStudentiBoundary visualizzazioneElencoStudentiBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.visualizzazioneElencoStudentiBoundary = visualizzazioneElencoStudentiBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void mandaIdPortfolio(Long portfolio_id) {
        // <<create>> VisualizzazionePortfolioPubblicoBoundary
        visualizzazionePortfolioPubblicoBoundary =
                new VisualizzazionePortfolioPubblicoBoundary();

        // recuperaPortfolioPubblico(portfolio_id)
        PortfolioEntity portfolio =
                dbmsBoundary.recuperaPortfolioPubblico(portfolio_id);

        if (portfolio != null) {
            // mostraPortfolio(portfolio)
            visualizzazionePortfolioPubblicoBoundary.mostraPortfolio(portfolio);
        } else {
            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Portfolio pubblico non trovato.");
        }
    }
}