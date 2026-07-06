package it.afam.is.progetto.afam_app.gestioneaccount.control;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.entity.Studente;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;

public class GenericoController {

    private final AutenticazioneBoundary autenticazioneBoundary;
    private final DBMSBoundary dbmsBoundary;

    public GenericoController(
            AutenticazioneBoundary autenticazioneBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.autenticazioneBoundary = autenticazioneBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void azioneRiservata() {
        if (rilevaTokenScaduto()) {
            Sessione sessione = new Sessione();
            Studente studente = sessione.getStudente();

            Long studente_id = null;

            if (studente != null) {
                studente_id = studente.getId();
            }

            dbmsBoundary.invalidaToken(studente_id);

            sessione.logout();

            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Sessione scaduta. Effettua nuovamente il login.");

            autenticazioneBoundary.mostraAutenticazione();
        }
    }

    public boolean rilevaTokenScaduto() {
        return true;
    }
}