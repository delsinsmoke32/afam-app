package it.afam.is.progetto.afam_app.gestione_portfolio.controller;

import java.util.List;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.FileStorageBoundary;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.ListaSezioniBoundary;
import it.afam.is.progetto.afam_app.common.PopupConfermaBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.VisualizzaPortfolioBoundary;

public class CancellaSezioneController {

    private final VisualizzaPortfolioBoundary visualizzaPortfolioBoundary;
    private final DBMSBoundary dbmsBoundary;
    private final FileStorageBoundary fileStorageBoundary;

    private ListaSezioniBoundary listaSezioniBoundary;
    private PopupConfermaBoundary popupConfermaBoundary;

    private Long portfolio_id;
    private Long sezione_id;

    public CancellaSezioneController(
            VisualizzaPortfolioBoundary visualizzaPortfolioBoundary,
            DBMSBoundary dbmsBoundary,
            FileStorageBoundary fileStorageBoundary
    ) {
        this.visualizzaPortfolioBoundary = visualizzaPortfolioBoundary;
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = fileStorageBoundary;
    }

    public void avviaCancellazioneSezione(Long portfolio_id) {
        this.portfolio_id = portfolio_id;

        List<SezioneEntity> listaSezioni = dbmsBoundary.recuperaSezioniPortfolio(portfolio_id);

        if (listaSezioni != null && !listaSezioni.isEmpty()) {
            listaSezioniBoundary = new ListaSezioniBoundary(this);
            listaSezioniBoundary.mostraListaSezioni(listaSezioni);
        } else {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Nessuna sezione da eliminare.");

            visualizzaPortfolioBoundary.ricaricaPortfolio();
        }
    }

    public void cancellaSezione(Long sezione_id) {
        this.sezione_id = sezione_id;

        if (listaSezioniBoundary != null) {
            listaSezioniBoundary.dispose();
            listaSezioniBoundary = null;
        }

        popupConfermaBoundary = new PopupConfermaBoundary(
                () -> conferma(),
                () -> annulla()
        );

        popupConfermaBoundary.mostraPopup("Sei sicuro di voler eliminare questa sezione?");
    }

    public void conferma() {
        if (popupConfermaBoundary != null) {
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        List<String> pathAllegati = dbmsBoundary.recuperaPathAllegatiSezione(sezione_id);

        fileStorageBoundary.eliminaFileMultipli(pathAllegati);

        dbmsBoundary.eliminaSezione(sezione_id, portfolio_id);

        visualizzaPortfolioBoundary.mostraPortfolioRimuoviSezione(sezione_id);
    }

    public void annulla() {
        if (popupConfermaBoundary != null) {
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        visualizzaPortfolioBoundary.ricaricaPortfolio();
    }
}