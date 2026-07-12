package it.afam.is.progetto.afam_app.gestione_portfolio.controller;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.common.PopupSuccessoBoundary;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.FormDescrizionePortBoundary;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.VisualizzaPortfolioBoundary;

public class ModificaDescrizionePortController {

    private final VisualizzaPortfolioBoundary visualizzaPortfolioBoundary;
    private final DBMSBoundary dbmsBoundary;
    private Long portfolio_id;

    public ModificaDescrizionePortController(
            VisualizzaPortfolioBoundary visualizzaPortfolioBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.visualizzaPortfolioBoundary = visualizzaPortfolioBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void richiediModificaDescrizione(Long portfolio_id) {
        this.portfolio_id = portfolio_id;

        // <<create>> FormDescrizionePortBoundary
        FormDescrizionePortBoundary formDescrizionePortBoundary =
                new FormDescrizionePortBoundary(this);

        // recuperaDescrizionePortfolio(portfolio_id)
        String descrizione = dbmsBoundary.recuperaDescrizionePortfolio(portfolio_id);

        // mostraForm(descrizione)
        formDescrizionePortBoundary.mostraForm(descrizione);
    }

    public void mandaModifica(String descrizione) {
        // aggiornaDescrizionePort(descrizione)
        // Passo anche l'id per permettere al DBMS di sapere quale record aggiornare
        dbmsBoundary.aggiornaDescrizionePort(portfolio_id, descrizione);

        // <<create>> PopupSuccessoBoundary
        PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();

        // mostraPopup(testo)
        popupSuccessoBoundary.mostraPopup("Descrizione del portfolio aggiornata con successo!");

        // mostraPortfolio()
        visualizzaPortfolioBoundary.mostraPortfolio();
    }
}