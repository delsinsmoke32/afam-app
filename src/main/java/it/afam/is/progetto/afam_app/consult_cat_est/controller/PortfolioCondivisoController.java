package it.afam.is.progetto.afam_app.consult_cat_est.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.entity.LinkCondivisoEntity;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.FormAccessoBoundary;
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.HomepageBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.common.PopupSuccessoBoundary;
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.PortfolioCondivisoBoundary;

public class PortfolioCondivisoController {

    private final HomepageBoundary homepageBoundary;
    private final DBMSBoundary dbmsBoundary;

    private LinkCondivisoEntity linkCondiviso;
    private FormAccessoController formAccessoController;
    private PortfolioCondivisoBoundary portfolioCondivisoBoundary;

    private PortfolioEntity portfolio;
    private Long portfolio_id;
    private Long link_condiviso_id;

    public PortfolioCondivisoController(
            HomepageBoundary homepageBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.homepageBoundary = homepageBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void mandaURL(String URL) {
        // verificaLink(URL)
        linkCondiviso = dbmsBoundary.verificaLink(URL);

        if (linkCondiviso == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Link scaduto, disattivato o inesistente.");
            return;
        }

        // <<create>> FormAccessoBoundary
        FormAccessoBoundary formAccessoBoundary = new FormAccessoBoundary();

        // <<create>> FormAccessoController
        formAccessoController = new FormAccessoController(this, formAccessoBoundary);

        formAccessoBoundary.setFormAccessoController(formAccessoController);

        // mostraFormAccesso()
        formAccessoController.mostraFormAccesso();
    }

    public void registraAccesso(String nome, String cognome, String ruolo, LocalDateTime currentTime) {
        dbmsBoundary.registraAccesso(
                nome,
                cognome,
                ruolo,
                currentTime,
                linkCondiviso.getId()
        );
    }

    public void finito() {
        link_condiviso_id = linkCondiviso.getId();

        // recupera portfolio_id senza usare direttamente il lazy linkCondiviso.getPortfolio()
        portfolio_id = dbmsBoundary.recuperaPortfolioIdDaLink(link_condiviso_id);

        if (portfolio_id == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Portfolio condiviso non trovato.");
            return;
        }

        // recuperaPortfolioCondiviso(portfolio_id, link_condiviso_id)
        portfolio = dbmsBoundary.recuperaPortfolioCondiviso(portfolio_id, link_condiviso_id);

        if (portfolio == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Portfolio non disponibile.");
            return;
        }

        List<SezioneEntity> sezioniVisibili =
                dbmsBoundary.recuperaSezioniVisibiliCondivisione(link_condiviso_id);

        List<AllegatoEntity> allegatiVisibili =
                dbmsBoundary.recuperaAllegatiSezioniVisibili(link_condiviso_id);

        // <<create>> PortfolioCondivisoBoundary
        portfolioCondivisoBoundary =
                new PortfolioCondivisoBoundary(this, sezioniVisibili, allegatiVisibili);

        // mostraPortfolioCondiviso(portfolio)
        portfolioCondivisoBoundary.mostraPortfolioCondiviso(portfolio);
    }

    public void controllaDownload(Long allegato_id) {
        // recuperaImpostazioneDownload(portfolio_id)
        Boolean impostazioneDownload = dbmsBoundary.recuperaImpostazioneDownload(portfolio_id);

        if (Boolean.TRUE.equals(impostazioneDownload)) {
            // apriSelettoreSalvataggio()
            String pathDestinazione = portfolioCondivisoBoundary.apriSelettoreSalvataggio();

            if (pathDestinazione == null || pathDestinazione.trim().isEmpty()) {
                portfolioCondivisoBoundary.mostraPortfolio();
                return;
            }

            // eseguiDownload(allegato_id, pathDestinazione)
            eseguiDownload(allegato_id, pathDestinazione);
        } else {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Download non consentito per questo portfolio.");

            // mostraPortfolio()
            portfolioCondivisoBoundary.mostraPortfolio();
        }
    }

    public void eseguiDownload(Long allegato_id, String pathDestinazione) {
        try {
            AllegatoEntity allegato = dbmsBoundary.recuperaAllegato(allegato_id);

            if (allegato == null || allegato.getPercorsoRisorsa() == null) {
                PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
                popupErroreBoundary.mostraPopup("File non trovato.");
                portfolioCondivisoBoundary.mostraPortfolio();
                return;
            }

            Path sorgente = Paths.get(allegato.getPercorsoRisorsa());

            if (!Files.exists(sorgente)) {
                PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
                popupErroreBoundary.mostraPopup("File non presente nel filesystem.");
                portfolioCondivisoBoundary.mostraPortfolio();
                return;
            }

            Path cartellaDestinazione = Paths.get(pathDestinazione);
            Path fileDestinazione = cartellaDestinazione.resolve(sorgente.getFileName());

            Files.copy(sorgente, fileDestinazione, StandardCopyOption.REPLACE_EXISTING);

            PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();
            popupSuccessoBoundary.mostraPopup("Download completato con successo.");

            portfolioCondivisoBoundary.mostraPortfolio();

        } catch (Exception e) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Errore durante il download: " + e.getMessage());

            portfolioCondivisoBoundary.mostraPortfolio();
        }
    }
}