package it.afam.is.progetto.afam_app.gestioneaccount.control;

import java.util.Comparator;
import java.util.List;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.ListaSezioniOrdinamentoBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.VisualizzaPortfolioBoundary;

public class OrdinamentoSezioniController {

    private final VisualizzaPortfolioBoundary visualizzaPortfolioBoundary;
    private final DBMSBoundary dbmsBoundary;

    private ListaSezioniOrdinamentoBoundary listaSezioniOrdinamentoBoundary;

    private Long portfolio_id;

    public OrdinamentoSezioniController(
            VisualizzaPortfolioBoundary visualizzaPortfolioBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.visualizzaPortfolioBoundary = visualizzaPortfolioBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void avviaOrdinamentoSezioni(Long idPortfolio) {
        this.portfolio_id = idPortfolio;

        // recuperaSezioniPortfolio(portfolio_id)
        List<SezioneEntity> listaSezioni = dbmsBoundary.recuperaSezioniPortfolio(portfolio_id);

        if (listaSezioni != null && !listaSezioni.isEmpty()) {
            listaSezioni.sort(Comparator.comparing(
                    sezione -> sezione.getOrdineVisualizzazione() != null
                            ? sezione.getOrdineVisualizzazione()
                            : 0
            ));

            // <<create>> ListaSezioniOrdinamentoBoundary
            listaSezioniOrdinamentoBoundary = new ListaSezioniOrdinamentoBoundary(this);

            // mostraListaSezioni(listaSezioni)
            listaSezioniOrdinamentoBoundary.mostraListaSezioni(listaSezioni);
        } else {
            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Nessuna sezione disponibile da ordinare.");

            // mostraPortfolio()
            visualizzaPortfolioBoundary.mostraPortfolio();
        }
    }

    public void confermaOrdinamentoSezioni(List<Long> nuovoOrdine, Long portfolio_id) {
        if (listaSezioniOrdinamentoBoundary != null) {
            // <<destroy>> ListaSezioniOrdinamentoBoundary
            listaSezioniOrdinamentoBoundary.dispose();
            listaSezioniOrdinamentoBoundary = null;
        }

        // aggiornaOrdineSezioni(nuovoOrdine, portfolio_id)
        dbmsBoundary.aggiornaOrdineSezioni(nuovoOrdine, portfolio_id);

        // mostraPortfolio()
        visualizzaPortfolioBoundary.mostraPortfolio();
    }

    public Long getPortfolio_id() {
        return portfolio_id;
    }
}