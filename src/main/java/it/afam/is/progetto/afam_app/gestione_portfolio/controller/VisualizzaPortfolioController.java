package it.afam.is.progetto.afam_app.gestione_portfolio.controller;

import java.util.List;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.GestionePortfolioBoundary;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.ListaPortfolioBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.VisualizzaPortfolioBoundary;

public class VisualizzaPortfolioController {

    private final GestionePortfolioBoundary gestionePortfolioBoundary;
    private final DBMSBoundary dbmsBoundary;

    private ListaPortfolioBoundary listaPortfolioBoundary;

    public VisualizzaPortfolioController(
            GestionePortfolioBoundary gestionePortfolioBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.gestionePortfolioBoundary = gestionePortfolioBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void recuperaListaPortfoli(Long studente_id) {
        List<PortfolioEntity> listaPortfoli = dbmsBoundary.recuperaListaPortfoli(studente_id);

        if (listaPortfoli != null && !listaPortfoli.isEmpty()) {
            // <<create>> ListaPortfolioBoundary
            listaPortfolioBoundary = new ListaPortfolioBoundary(this);

            // mostraListaPortfoli(listaPortfoli)
            listaPortfolioBoundary.mostraListaPortfoli(listaPortfoli);
        } else {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Nessun portfolio trovato.");

            gestionePortfolioBoundary.mostraGestionePortfolio();
        }
    }

    public void recuperaPortfolio(Long portfolio_id) {
        PortfolioEntity portfolio = dbmsBoundary.recuperaPortfolio(portfolio_id);

        if (listaPortfolioBoundary != null) {
            // <<destroy>> ListaPortfolioBoundary
            listaPortfolioBoundary.dispose();
            listaPortfolioBoundary = null;
        }

        List<SezioneEntity> sezioniPortfolio = dbmsBoundary.recuperaSezioniPortfolio(portfolio_id);

        VisualizzaPortfolioBoundary visualizzaPortfolioBoundary =
                new VisualizzaPortfolioBoundary(dbmsBoundary, portfolio_id);

        visualizzaPortfolioBoundary.mostraPortfolioInit(portfolio, sezioniPortfolio);
    }
}