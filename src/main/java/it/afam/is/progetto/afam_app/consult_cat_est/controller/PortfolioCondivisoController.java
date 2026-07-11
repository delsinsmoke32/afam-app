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

        System.out.println("DEBUG - Sto cercando sezioni per il link ID: " + link_condiviso_id);

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
        // 1. Recupera l'allegato per avere il nome suggerito (servirà per la Boundary)
        AllegatoEntity allegato = dbmsBoundary.recuperaAllegato(allegato_id);
        String nomeSuggerito = (allegato != null) ? allegato.getNomeFile() : "download_file";

        Boolean impostazioneDownload = dbmsBoundary.recuperaImpostazioneDownload(portfolio_id);

        if (Boolean.TRUE.equals(impostazioneDownload)) {
            // 2. Passiamo il nome suggerito alla Boundary
            String pathDestinazione = portfolioCondivisoBoundary.apriSelettoreSalvataggio(nomeSuggerito);

            if (pathDestinazione == null || pathDestinazione.trim().isEmpty()) {
                portfolioCondivisoBoundary.mostraPortfolio();
                return;
            }

            eseguiDownload(allegato_id, pathDestinazione);
        } else {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Download non consentito per questo portfolio.");
            portfolioCondivisoBoundary.mostraPortfolio();
        }
    }

    public void eseguiDownload(Long allegato_id, String pathDestinazione) {
        try {
            AllegatoEntity allegato = dbmsBoundary.recuperaAllegato(allegato_id);

            if (allegato == null || allegato.getPercorsoRisorsa() == null) {
                PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
                popupErroreBoundary.mostraPopup("File non trovato nel database.");
                portfolioCondivisoBoundary.mostraPortfolio();
                return;
            }

            // 1. PATH SORGENTE: Dove si trova fisicamente il file sul server/nella cartella uploads
            Path sorgente = Paths.get(allegato.getPercorsoRisorsa());

            if (!Files.exists(sorgente)) {
                PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
                popupErroreBoundary.mostraPopup("Errore: Il file originale non si trova più in " + sorgente.toAbsolutePath());
                portfolioCondivisoBoundary.mostraPortfolio();
                return;
            }

            // 2. NOME DEL FILE: Estraiamo il nome corretto
            String nomeFileFinale = "file_scaricato";
            if (allegato.getNomeFile() != null && !allegato.getNomeFile().trim().isEmpty()) {
                nomeFileFinale = allegato.getNomeFile();
            } else if (sorgente.getFileName() != null) {
                nomeFileFinale = sorgente.getFileName().toString();
            }

            // 3. PATH DESTINAZIONE
            // Dato che il JFileChooser è ora in modalità FILES_ONLY,
            // pathDestinazione è GIÀ il percorso completo del file che l'utente vuole creare.
            Path fileDestinazione = Paths.get(pathDestinazione);

            // Creiamo la cartella padre se non esiste (nel caso l'utente abbia scelto una cartella nuova)
            if (fileDestinazione.getParent() != null && !Files.exists(fileDestinazione.getParent())) {
                Files.createDirectories(fileDestinazione.getParent());
            }

            // 4. COPIA DEL FILE
            // Controlliamo se stiamo sovrascrivendo lo stesso file
            if (Files.exists(fileDestinazione) && Files.isSameFile(sorgente, fileDestinazione)) {
                PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
                popupErroreBoundary.mostraPopup("Il file è già presente in quella posizione.");
                portfolioCondivisoBoundary.mostraPortfolio();
                return;
            }

            // Eseguiamo la copia direttamente sulla destinazione finale
            Files.copy(sorgente, fileDestinazione, StandardCopyOption.REPLACE_EXISTING);

            // 5. SUCCESSO
            PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();
            popupSuccessoBoundary.mostraPopup("Download completato con successo in:\n" + fileDestinazione.toAbsolutePath());
            portfolioCondivisoBoundary.mostraPortfolio();

        } catch (Exception e) {
            e.printStackTrace();

            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Errore I/O (" + e.getClass().getSimpleName() + "):\n" + e.getMessage());
            portfolioCondivisoBoundary.mostraPortfolio();
        }
    }
}