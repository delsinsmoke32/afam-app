package it.afam.is.progetto.afam_app.gestioneaccount.control;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.HomepageBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PaginaRicercaBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.VisualizzazionePortfolioPubblicoBoundary;

public class VisualizzazionePortfolioPubblicoController {

    private HomepageBoundary homepageBoundary;
    private PaginaRicercaBoundary paginaRicercaBoundary;

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