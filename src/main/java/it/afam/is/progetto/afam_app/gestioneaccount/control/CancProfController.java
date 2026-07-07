package it.afam.is.progetto.afam_app.gestioneaccount.control;

import javax.swing.JPasswordField;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.FormCancellazioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.GestioneProfiloBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupConfermaBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupSuccessoBoundary;

public class CancProfController {

    private final GestioneProfiloBoundary gestioneProfiloBoundary;
    private final AutenticazioneBoundary autenticazioneBoundary;
    private final DBMSBoundary dbmsBoundary;

    private PopupConfermaBoundary popupConfermaBoundary;
    private FormCancellazioneBoundary formCancellazioneBoundary;

    public CancProfController(
            GestioneProfiloBoundary gestioneProfiloBoundary,
            AutenticazioneBoundary autenticazioneBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.gestioneProfiloBoundary = gestioneProfiloBoundary;
        this.autenticazioneBoundary = autenticazioneBoundary;
        this.dbmsBoundary = dbmsBoundary;

        // <<create>> PopupConfermaBoundary
        popupConfermaBoundary = new PopupConfermaBoundary(this);

        // mostraPopup(testo)
        popupConfermaBoundary.mostraPopup("Sei sicuro di voler eliminare il tuo account?");
    }

    public void conferma() {
        // <<destroy>> PopupConfermaBoundary
        if (popupConfermaBoundary != null) {
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        // <<create>> FormCancellazioneBoundary
        formCancellazioneBoundary = new FormCancellazioneBoundary(this);

        // mostraForm(campoPassword:JPasswordField)
        JPasswordField campoPassword = new JPasswordField();
        formCancellazioneBoundary.mostraForm(campoPassword);
    }

    public void mandaPassword(String password) {
        // <<destroy>> FormCancellazioneBoundary
        if (formCancellazioneBoundary != null) {
            formCancellazioneBoundary.dispose();
            formCancellazioneBoundary = null;
        }

        Long studente_id = Sessione.getStudenteId();

        // checkPassword(studente_id, password)
        StudenteEntity StudenteEntity = dbmsBoundary.checkPassword(studente_id, password);

        // alt
        // [StudenteEntity != null]
        if (StudenteEntity != null) {

            // cancellaStudente(studente_id)
            dbmsBoundary.cancellaStudente(studente_id);

            // <<destroy>> StudenteEntity
            // Nel codice reale StudenteEntity è StudenteEntity: viene eliminato dal DB.

            // logout()
            Sessione sessione = new Sessione();
            sessione.logout();

            // <<create>> PopupSuccessoBoundary
            PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();

            // mostraPopup(testo)
            popupSuccessoBoundary.mostraPopup("Account eliminato con successo.");

            // StudenteEntity -> PopupSuccessoBoundary: cliccaOK()
            // gestito dal popup

            // mostraAutenticazione()
            autenticazioneBoundary.mostraAutenticazione();

        } else {
            // [else]

            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Password errata. Account non eliminato.");

            // StudenteEntity -> PopupErroreBoundary: cliccaOK()
            // gestito dal popup

            // mostraGestioneProfilo()
            gestioneProfiloBoundary.mostraGestioneProfilo();
        }
    }
    
    public void annulla() {
    // <<destroy>> PopupConfermaBoundary
    if (popupConfermaBoundary != null) {
        popupConfermaBoundary.dispose();
        popupConfermaBoundary = null;
    }

    // mostraGestioneProfilo()
    gestioneProfiloBoundary.mostraGestioneProfilo();
}
}

