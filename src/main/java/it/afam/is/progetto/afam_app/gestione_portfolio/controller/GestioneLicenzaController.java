package it.afam.is.progetto.afam_app.gestione_portfolio.controller;

import java.util.List;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.LicenzaEntity;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.ListaGestioneLicenzaBoundary;
import it.afam.is.progetto.afam_app.common.PopupConfermaBoundary;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.VisualizzaPortfolioBoundary;

public class GestioneLicenzaController {

    private final VisualizzaPortfolioBoundary visualizzaPortfolioBoundary;
    private final DBMSBoundary dbmsBoundary;

    private ListaGestioneLicenzaBoundary listaGestioneLicenzaBoundary;
    private PopupConfermaBoundary popupConfermaBoundary;

    private Long portfolio_id;
    private Long licenza_id;

    public GestioneLicenzaController(
            VisualizzaPortfolioBoundary visualizzaPortfolioBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.visualizzaPortfolioBoundary = visualizzaPortfolioBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void avviaGestioneLicenza(Long portfolio_id) {
        this.portfolio_id = portfolio_id;

        // recuperaLicenze()
        List<LicenzaEntity> listaLicenze = dbmsBoundary.recuperaLicenze();

        // recuperaLicenza(portfolio_id)
        LicenzaEntity licenza = dbmsBoundary.recuperaLicenza(portfolio_id);

        // <<create>> ListaGestioneLicenzaBoundary
        listaGestioneLicenzaBoundary = new ListaGestioneLicenzaBoundary(this);

        // mostraLicenzeDisponibili(listaLicenze)
        listaGestioneLicenzaBoundary.mostraLicenzeDisponibili(listaLicenze);

        if (licenza != null) {
            // evidenziaLicenzaImpostata(licenza)
            listaGestioneLicenzaBoundary.evidenziaLicenzaImpostata(licenza);
        }
    }

    public void scegliLicenza(Long licenza_id) {
        this.licenza_id = licenza_id;

        // <<create>> PopupConfermaBoundary
        popupConfermaBoundary = new PopupConfermaBoundary(
                () -> conferma(),
                () -> annulla()
        );

        // mostraPopup(testo)
        popupConfermaBoundary.mostraPopup("Vuoi impostare la licenza selezionata?");
    }

    public void conferma() {
        if (popupConfermaBoundary != null) {
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        if (listaGestioneLicenzaBoundary != null) {
            listaGestioneLicenzaBoundary.dispose();
            listaGestioneLicenzaBoundary = null;
        }

        // salvaLicenzaPortfolio(portfolio_id, licenza_id)
        dbmsBoundary.salvaLicenzaPortfolio(portfolio_id, licenza_id);

        // mostraPortfolio()
        visualizzaPortfolioBoundary.mostraPortfolio();
    }

    public void annulla() {
        if (popupConfermaBoundary != null) {
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        if (listaGestioneLicenzaBoundary != null) {
            listaGestioneLicenzaBoundary.dispose();
            listaGestioneLicenzaBoundary = null;
        }

        // mostraPortfolio()
        visualizzaPortfolioBoundary.mostraPortfolio();
    }
}