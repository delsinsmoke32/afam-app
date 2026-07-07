package it.afam.is.progetto.afam_app.gestioneaccount.control;

import java.util.List;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.GestionePortfolioBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.ListaPortfoliBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.VisualizzaPortfolioBoundary;

public class VisualizzaPortfolioController {

    private final GestionePortfolioBoundary gestionePortfolioBoundary;
    private final DBMSBoundary dbmsBoundary;

    private ListaPortfoliBoundary listaPortfoliBoundary;

    public VisualizzaPortfolioController(
            GestionePortfolioBoundary gestionePortfolioBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.gestionePortfolioBoundary = gestionePortfolioBoundary;
        this.dbmsBoundary = dbmsBoundary;

        Long studente_id = Sessione.getStudenteId();

        // recuperaListaPortfoli(studente_id)
        List<PortfolioEntity> listaPortfoli = dbmsBoundary.recuperaListaPortfoli(studente_id);

        // alt [listaPortfoli != null]
        if (listaPortfoli != null && !listaPortfoli.isEmpty()) {
            // <<create>> ListaPortfoliBoundary
            listaPortfoliBoundary = new ListaPortfoliBoundary(this);

            // mostraListaPortfoli(listaPortfoli)
            listaPortfoliBoundary.mostraListaPortfoli(listaPortfoli);
        } else {
            // [else]
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Nessun portfolio trovato.");

            // cliccaOK() gestito dal popup

            // mostraGestionePortfolio()
            gestionePortfolioBoundary.mostraGestionePortfolio();
        }
    }

    public void recuperaPortfolio(Long portfolio_id) {
        PortfolioEntity portfolio = dbmsBoundary.recuperaPortfolio(portfolio_id);

        if (listaPortfoliBoundary != null) {
            // <<destroy>> ListaPortfoliBoundary
            listaPortfoliBoundary.dispose();
            listaPortfoliBoundary = null;
        }

        if (portfolio == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Portfolio non trovato.");

            gestionePortfolioBoundary.mostraGestionePortfolio();
            return;
        }

        // recuperaSezioniPortfolio(portfolio_id)
        List<SezioneEntity> sezioniPortfolio = dbmsBoundary.recuperaSezioniPortfolio(portfolio_id);

        // <<create>> VisualizzaPortfolioBoundary
        VisualizzaPortfolioBoundary visualizzaPortfolioBoundary =
                new VisualizzaPortfolioBoundary(dbmsBoundary, portfolio_id);

        // mostraPortfolioInit(portfolio, sezioniPortfolio)
        visualizzaPortfolioBoundary.mostraPortfolioInit(portfolio, sezioniPortfolio);
    }
}