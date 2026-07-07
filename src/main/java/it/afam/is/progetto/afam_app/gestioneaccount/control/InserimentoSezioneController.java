package it.afam.is.progetto.afam_app.gestioneaccount.control;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.FormInserimentoSezioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupSuccessoBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.VisualizzaPortfolioBoundary;

public class InserimentoSezioneController {

    private final VisualizzaPortfolioBoundary visualizzaPortfolioBoundary;
    private final DBMSBoundary dbmsBoundary;

    private FormInserimentoSezioneBoundary formInserimentoSezioneBoundary;
    private Long portfolio_id;

    public InserimentoSezioneController(
            VisualizzaPortfolioBoundary visualizzaPortfolioBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.visualizzaPortfolioBoundary = visualizzaPortfolioBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void avviaInserimentoSezione(Long portfolio_id) {
        this.portfolio_id = portfolio_id;

        // <<create>> FormInserimentoSezioneBoundary
        formInserimentoSezioneBoundary = new FormInserimentoSezioneBoundary(this);

        // mostraForm()
        formInserimentoSezioneBoundary.mostraForm();
    }

    public void mandaCampi(String titolo, String corpo_testo) {
        // verificaCampiPieni(titolo, corpo_testo)
        boolean verificaCampiPieni = verificaCampiPieni(titolo, corpo_testo);

        if (formInserimentoSezioneBoundary != null) {
            // <<destroy>> FormInserimentoSezioneBoundary
            formInserimentoSezioneBoundary.dispose();
            formInserimentoSezioneBoundary = null;
        }

        // alt [verificaCampiPieni = false]
        if (!verificaCampiPieni) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Titolo e corpo testo obbligatori.");

            // cliccaOK() gestito dal popup

            // mostraPortfolio()
            visualizzaPortfolioBoundary.mostraPortfolio();
            return;
        }

        PortfolioEntity portfolio = dbmsBoundary.cercaPortfolio(portfolio_id);

        if (portfolio == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Portfolio non trovato.");

            visualizzaPortfolioBoundary.mostraPortfolio();
            return;
        }

        // <<create>> SezioneEntity
        SezioneEntity sezione = SezioneEntity.builder()
                .titolo(titolo.trim())
                .corpoTesto(corpo_testo.trim())
                .portfolio(portfolio)
                .build();

        // inserisciSezione(SezioneEntity)
        dbmsBoundary.inserisciSezione(sezione);

        PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();

        // mostraPopup(testo)
        popupSuccessoBoundary.mostraPopup("SezioneEntity inserita con successo.");

        // cliccaOK() gestito dal popup

        // mostraPortfolioAggiungiSezione(SezioneEntity)
        visualizzaPortfolioBoundary.mostraPortfolioAggiungiSezione(sezione);
    }

    public boolean verificaCampiPieni(String titolo, String corpo_testo) {
        return titolo != null
                && corpo_testo != null
                && !titolo.trim().isEmpty()
                && !corpo_testo.trim().isEmpty();
    }
}
