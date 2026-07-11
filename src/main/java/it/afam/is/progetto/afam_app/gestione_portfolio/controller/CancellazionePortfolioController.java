package it.afam.is.progetto.afam_app.gestione_portfolio.controller;

import java.util.List;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.FileStorageBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.GestionePortfolioBoundary;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.ListaPortfolioBoundary;
import it.afam.is.progetto.afam_app.common.PopupConfermaBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.common.PopupSuccessoBoundary;

public class CancellazionePortfolioController {

    private final GestionePortfolioBoundary gestionePortfolioBoundary;
    private final DBMSBoundary dbmsBoundary;
    private final FileStorageBoundary fileStorageBoundary;

    private ListaPortfolioBoundary listaPortfolioBoundary;
    private PopupConfermaBoundary popupConfermaBoundary;

    private Long idStudente;
    private Long portfolio_id;

    public CancellazionePortfolioController(
            GestionePortfolioBoundary gestionePortfolioBoundary,
            DBMSBoundary dbmsBoundary,
            FileStorageBoundary fileStorageBoundary
    ) {
        this.gestionePortfolioBoundary = gestionePortfolioBoundary;
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = fileStorageBoundary;
    }

    public void recuperaPortfoliStudente(Long idStudente) {
        this.idStudente = idStudente;

        // recuperaPortfoliStudente(idStudente)
        List<PortfolioEntity> listaPortfoli = dbmsBoundary.recuperaPortfoliStudente(idStudente);

        // alt [listaPortfoli != null]
        if (listaPortfoli != null && !listaPortfoli.isEmpty()) {
            // <<create>> ListaPortfolioBoundary
            listaPortfolioBoundary = new ListaPortfolioBoundary(this);

            // mostraListaPortfoli(listaPortfoli)
            listaPortfolioBoundary.mostraListaPortfoli(listaPortfoli);
        } else {
            // [else]
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Nessun portfolio da eliminare.");

            // mostraGestionePortfolio()
            gestionePortfolioBoundary.mostraGestionePortfolio();
        }
    }

    public void avviaCancellazionePortfolio() {
        Long idStudenteCorrente = Sessione.getStudenteId();

        if (idStudenteCorrente == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Studente non autenticato.");

            gestionePortfolioBoundary.mostraGestionePortfolio();
            return;
        }

        recuperaPortfoliStudente(idStudenteCorrente);
    }

    public void cancellaPortfolio(Long portfolio_id) {
        this.portfolio_id = portfolio_id;

        if (listaPortfolioBoundary != null) {
            // <<destroy>> ListaPortfolioBoundary
            listaPortfolioBoundary.dispose();
            listaPortfolioBoundary = null;
        }

        // <<create>> PopupConfermaBoundary
        popupConfermaBoundary = new PopupConfermaBoundary(
                () -> conferma(),
                () -> annulla()
        );

        // mostraPopup(testo)
        popupConfermaBoundary.mostraPopup("Sei sicuro di voler eliminare questo portfolio?");
    }

    public void conferma() {
        if (popupConfermaBoundary != null) {
            // <<destroy>> PopupConfermaBoundary
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        // recuperaPathAllegati(portfolio_id)
        List<String> pathAllegati = dbmsBoundary.recuperaPathAllegati(portfolio_id);

        // eliminaFileMultipli(pathAllegati)
        fileStorageBoundary.eliminaFileMultipli(pathAllegati);

        // eliminaPortfolio(idPortfolio, idStudente)
        dbmsBoundary.eliminaPortfolio(portfolio_id, idStudente);

        // <<destroy>> PortfolioEntity
        // gestito da queryEliminaPortfolio(idPortfolio, idStudente)

        // <<create>> PopupSuccessoBoundary
        PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();

        // mostraPopup(testo)
        popupSuccessoBoundary.mostraPopup("Portfolio eliminato con successo.");

        // mostraGestionePortfolio()
        gestionePortfolioBoundary.mostraGestionePortfolio();
    }

    public void annulla() {
        if (popupConfermaBoundary != null) {
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        gestionePortfolioBoundary.mostraGestionePortfolio();
    }
}