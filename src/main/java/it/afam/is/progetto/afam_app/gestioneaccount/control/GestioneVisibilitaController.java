package it.afam.is.progetto.afam_app.gestioneaccount.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.GestioneVisibilitaBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupSuccessoBoundary;

public class GestioneVisibilitaController {

    private final GenerazioneURLController generazioneURLController;
    private final DBMSBoundary dbmsBoundary;

    private GestioneVisibilitaBoundary gestioneVisibilitaBoundary;

    private Long link_condiviso_id;
    private Long portfolio_id;

    public GestioneVisibilitaController(
            GenerazioneURLController generazioneURLController,
            DBMSBoundary dbmsBoundary
    ) {
        this.generazioneURLController = generazioneURLController;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void conferma(Long link_condiviso_id, Long portfolio_id) {
        this.link_condiviso_id = link_condiviso_id;
        this.portfolio_id = portfolio_id;

        // mandaId(portfolio_id)
        mandaId(portfolio_id);
    }

    public void mandaId(Long portfolio_id) {
        this.portfolio_id = portfolio_id;

        // <<create>> GestioneVisibilitaBoundary
        gestioneVisibilitaBoundary = new GestioneVisibilitaBoundary(this);

        // recuperaSezioniPortfolio(portfolio_id)
        List<SezioneEntity> listaSezioni = dbmsBoundary.recuperaSezioniPortfolio(portfolio_id);

        // mostraStatoVisibilita(listaSezioni)
        gestioneVisibilitaBoundary.mostraStatoVisibilita(listaSezioni);
    }

    public void salvaModifiche(Map<Long, Boolean> impostazioni) {
        if (impostazioni == null || impostazioni.isEmpty()) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Nessuna sezione da salvare.");

            if (gestioneVisibilitaBoundary != null) {
                gestioneVisibilitaBoundary.dispose();
                gestioneVisibilitaBoundary = null;
            }

            generazioneURLController.mostraUrlDopoGestioneVisibilita();
            return;
        }

        // aggiornaImpostazioni(impostazioni, link_condiviso_id)
        dbmsBoundary.aggiornaImpostazioni(impostazioni, link_condiviso_id);

        // <<create>> PopupSuccessoBoundary
        // mostraPopup(testo)
        JOptionPane.showMessageDialog(
                gestioneVisibilitaBoundary,
                "Impostazioni di visibilità salvate.",
                "Successo",
                JOptionPane.INFORMATION_MESSAGE
        );

        // <<destroy>> GestioneVisibilitaBoundary
        if (gestioneVisibilitaBoundary != null) {
            gestioneVisibilitaBoundary.dispose();
            gestioneVisibilitaBoundary = null;
        }

        // mostraUrlGenerato(URL)
        generazioneURLController.mostraUrlDopoGestioneVisibilita();
    }

    public void annulla(Long link_condiviso_id, Long portfolio_id) {
        this.link_condiviso_id = link_condiviso_id;
        this.portfolio_id = portfolio_id;

        // mandaId(portfolio_id)
        // recuperaSezioniPortfolio(portfolio_id)
        List<SezioneEntity> listaSezioni = dbmsBoundary.recuperaSezioniPortfolio(portfolio_id);

        // settaPubbliche(listaSezioni)
        Map<Long, Boolean> impostazioni = settaPubbliche(listaSezioni);

        // aggiornaImpostazioni(impostazioni, link_condiviso_id)
        dbmsBoundary.aggiornaImpostazioni(impostazioni, link_condiviso_id);

        // <<create>> PopupSuccessoBoundary
        PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();

        // mostraPopup(testo)
        popupSuccessoBoundary.mostraPopup("Tutte le sezioni sono state rese visibili per la candidatura.");

        // mostraUrlGenerato(URL)
        generazioneURLController.mostraUrlDopoGestioneVisibilita();
    }

    public Map<Long, Boolean> settaPubbliche(List<SezioneEntity> listaSezioni) {
        Map<Long, Boolean> impostazioni = new HashMap<>();

        if (listaSezioni == null || listaSezioni.isEmpty()) {
            return impostazioni;
        }

        for (SezioneEntity sezione : listaSezioni) {
            if (sezione != null && sezione.getId() != null) {
                impostazioni.put(sezione.getId(), true);
            }
        }

        return impostazioni;
    }
}