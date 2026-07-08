package it.afam.is.progetto.afam_app.gestioneaccount.control;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.FormDescrizioneSezioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupSuccessoBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.VisualizzaSezioneBoundary;

public class ModificaDescrizioneController {

    private final VisualizzaSezioneBoundary visualizzaSezioneBoundary;
    private final DBMSBoundary dbmsBoundary;

    private FormDescrizioneSezioneBoundary formDescrizioneSezioneBoundary;

    private Long sezione_id;

    public ModificaDescrizioneController(
            VisualizzaSezioneBoundary visualizzaSezioneBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.visualizzaSezioneBoundary = visualizzaSezioneBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void richiediModificaDescrizione(Long sezione_id) {
        this.sezione_id = sezione_id;

        // <<create>> FormDescrizioneSezioneBoundary
        formDescrizioneSezioneBoundary = new FormDescrizioneSezioneBoundary(this);

        // recuperaDescrizioneSezione(sezione_id)
        String descrizione = dbmsBoundary.recuperaDescrizioneSezione(sezione_id);

        // mostraForm(descrizione)
        formDescrizioneSezioneBoundary.mostraForm(descrizione);
    }

    public void mandaModifica(String descrizione) {
        // aggiornaDescrizione(descrizione)
        dbmsBoundary.aggiornaDescrizione(sezione_id, descrizione);

        if (formDescrizioneSezioneBoundary != null) {
            formDescrizioneSezioneBoundary.dispose();
            formDescrizioneSezioneBoundary = null;
        }

        // <<create>> PopupSuccessoBoundary
        PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();

        // mostraPopup(testo)
        popupSuccessoBoundary.mostraPopup("Descrizione sezione modificata correttamente.");

        // mostraSezionePrecedente()
        visualizzaSezioneBoundary.mostraSezionePrecedente();
    }
}