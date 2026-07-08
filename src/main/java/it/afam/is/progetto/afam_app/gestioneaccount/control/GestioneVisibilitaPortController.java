package it.afam.is.progetto.afam_app.gestioneaccount.control;

import java.util.List;
import java.util.Map;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.GestioneVisibilitaPortBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupSuccessoBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.VisualizzaPortfolioBoundary;

public class GestioneVisibilitaPortController {

    private final VisualizzaPortfolioBoundary visualizzaPortfolioBoundary;
    private final DBMSBoundary dbmsBoundary;

    private GestioneVisibilitaPortBoundary gestioneVisibilitaPortBoundary;

    private Long portfolio_id;

    public GestioneVisibilitaPortController(
            VisualizzaPortfolioBoundary visualizzaPortfolioBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.visualizzaPortfolioBoundary = visualizzaPortfolioBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void avviaGestioneVisibilitaSezioni(Long portfolio_id) {
        this.portfolio_id = portfolio_id;

        // <<create>> GestioneVisibilitaPortBoundary
        gestioneVisibilitaPortBoundary = new GestioneVisibilitaPortBoundary(this);

        // recuperaImpostazioniVisibilita(portfolio_id)
        List<SezioneEntity> impostazioni =
                dbmsBoundary.recuperaImpostazioniVisibilita(portfolio_id);

        // mostraStatoVisibilita(impostazioni)
        gestioneVisibilitaPortBoundary.mostraStatoVisibilita(impostazioni);
    }

    public void salvaModifiche(Map<Long, Boolean> impostazioni) {
        if (impostazioni == null || impostazioni.isEmpty()) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Nessuna impostazione di visibilità da salvare.");
            return;
        }

        // aggiornaImpostazioni(impostazioni)
        dbmsBoundary.aggiornaImpostazioni(impostazioni);

        if (gestioneVisibilitaPortBoundary != null) {
            gestioneVisibilitaPortBoundary.dispose();
            gestioneVisibilitaPortBoundary = null;
        }

        // <<create>> PopupSuccessoBoundary
        PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();

        // mostraPopup(testo)
        popupSuccessoBoundary.mostraPopup("Impostazioni di visibilità del portfolio salvate.");

        // mostraPaginaCondivisione()
        visualizzaPortfolioBoundary.mostraPaginaCondivisione();
    }
}