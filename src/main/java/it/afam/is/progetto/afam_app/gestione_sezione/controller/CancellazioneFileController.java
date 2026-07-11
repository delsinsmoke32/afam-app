package it.afam.is.progetto.afam_app.gestione_sezione.controller;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.FileStorageBoundary;
import it.afam.is.progetto.afam_app.common.PopupConfermaBoundary;
import it.afam.is.progetto.afam_app.common.PopupSuccessoBoundary;
import it.afam.is.progetto.afam_app.gestione_sezione.boundary.VisualizzaSezioneBoundary;

public class CancellazioneFileController {

    private final VisualizzaSezioneBoundary visualizzaSezioneBoundary;
    private final DBMSBoundary dbmsBoundary;
    private final FileStorageBoundary fileStorageBoundary;

    private PopupConfermaBoundary popupConfermaBoundary;
    private Long allegato_id;

    public CancellazioneFileController(
            VisualizzaSezioneBoundary visualizzaSezioneBoundary,
            DBMSBoundary dbmsBoundary,
            FileStorageBoundary fileStorageBoundary
    ) {
        this.visualizzaSezioneBoundary = visualizzaSezioneBoundary;
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = fileStorageBoundary;
    }

    public void richiediCancellazioneFile(Long allegato_id) {
        this.allegato_id = allegato_id;

        // <<create>> PopupConfermaBoundary
        popupConfermaBoundary = new PopupConfermaBoundary(
                () -> conferma(),
                () -> annulla()
        );

        // mostraPopup(testo)
        popupConfermaBoundary.mostraPopup("Sei sicuro di voler eliminare questo file?");
    }

    public void conferma() {
        if (popupConfermaBoundary != null) {
            // <<destroy>> PopupConfermaBoundary
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        // recuperaPercorsoAllegato(allegato_id)
        String pathAllegato = dbmsBoundary.recuperaPercorsoAllegato(allegato_id);

        // eliminaFile(pathAllegato)
        fileStorageBoundary.eliminaFile(pathAllegato);

        // rimuoviAllegato(allegato_id)
        dbmsBoundary.rimuoviAllegato(allegato_id);

        // <<destroy>> AllegatoEntity
        // gestito da deleteAllegato(allegato_id)

        // <<create>> PopupSuccessoBoundary
        PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();

        // mostraPopup(testo)
        popupSuccessoBoundary.mostraPopup("File eliminato con successo.");

        // cliccaOK() gestito dal popup

        // mostraSezioneRimuoviAllegato(allegato_id)
        visualizzaSezioneBoundary.mostraSezioneRimuoviAllegato(allegato_id);
    }

    public void annulla() {
        if (popupConfermaBoundary != null) {
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        visualizzaSezioneBoundary.mostraSezionePrecedente();
    }
}