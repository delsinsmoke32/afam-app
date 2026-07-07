package it.afam.is.progetto.afam_app.gestioneaccount.control;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.FormMetadatiFileBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.VisualizzaSezioneBoundary;

public class ModificaMetadatiController {

    private final VisualizzaSezioneBoundary visualizzaSezioneBoundary;
    private final DBMSBoundary dbmsBoundary;

    private FormMetadatiFileBoundary formMetadatiFileBoundary;
    private Long allegato_id;

    public ModificaMetadatiController(
            VisualizzaSezioneBoundary visualizzaSezioneBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.visualizzaSezioneBoundary = visualizzaSezioneBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void richiediModificaMetadati(Long allegato_id) {
        this.allegato_id = allegato_id;

        // <<create>> FormMetadatiFileBoundary
        formMetadatiFileBoundary = new FormMetadatiFileBoundary(this);

        // recuperaMetadati(allegato_id)
        AllegatoEntity metadati = dbmsBoundary.recuperaMetadati(allegato_id);

        // mostraFormMod(metadati)
        formMetadatiFileBoundary.mostraFormMod(metadati);
    }

    public void mandaDati(String titolo, String descrizione, String autori) {
        if (formMetadatiFileBoundary != null) {
            formMetadatiFileBoundary.dispose();
            formMetadatiFileBoundary = null;
        }

        // aggiornaMetadati(titolo, descrizione, autori)
        dbmsBoundary.aggiornaMetadati(allegato_id, titolo, descrizione, autori);

        // mostraSezionePrecedente()
        visualizzaSezioneBoundary.mostraSezionePrecedente();
    }
}