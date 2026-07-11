package it.afam.is.progetto.afam_app.gestione_sezione.controller;

import java.util.List;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.FileStorageBoundary;
import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestione_portfolio.boundary.VisualizzaPortfolioBoundary;
import it.afam.is.progetto.afam_app.gestione_sezione.boundary.VisualizzaSezioneBoundary;

public class VisualizzaSezioneController {

    private final VisualizzaPortfolioBoundary visualizzaPortfolioBoundary;
    private final DBMSBoundary dbmsBoundary;
    private final FileStorageBoundary fileStorageBoundary;

    private VisualizzaSezioneBoundary visualizzaSezioneBoundary;

    public VisualizzaSezioneController(
            VisualizzaPortfolioBoundary visualizzaPortfolioBoundary,
            DBMSBoundary dbmsBoundary,
            FileStorageBoundary fileStorageBoundary
    ) {
        this.visualizzaPortfolioBoundary = visualizzaPortfolioBoundary;
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = fileStorageBoundary;
    }

    public void richiediVisualizzazione(Long sezione_id) {
        // <<create>> VisualizzaSezioneBoundary
        visualizzaSezioneBoundary =
                new VisualizzaSezioneBoundary(dbmsBoundary, fileStorageBoundary, sezione_id);

        SezioneEntity sezione = dbmsBoundary.cercaSezione(sezione_id);

        // recuperaAllegati(sezione_id)
        List<AllegatoEntity> listaAllegati = dbmsBoundary.recuperaAllegati(sezione_id);

        // alt [sezione trovata]
        if (sezione != null) {
            // mostraSezioneInit(listaAllegati)
            visualizzaSezioneBoundary.mostraSezioneInit(listaAllegati);
        } else {
            // [sezione non trovata]
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Sezione non trovata.");

            // cliccaOK() gestito dal popup

            // mostraPortfolio()
            visualizzaPortfolioBoundary.mostraPortfolio();
        }
    }
}