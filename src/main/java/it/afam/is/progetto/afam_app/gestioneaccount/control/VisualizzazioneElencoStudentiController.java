package it.afam.is.progetto.afam_app.gestioneaccount.control;

import java.util.List;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.HomepageBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.VisualizzazioneElencoStudentiBoundary;

public class VisualizzazioneElencoStudentiController {

    private final HomepageBoundary homepageBoundary;
    private final DBMSBoundary dbmsBoundary;

    public VisualizzazioneElencoStudentiController(
            HomepageBoundary homepageBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.homepageBoundary = homepageBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void recuperaElencoStudenti() {
        // getElencoStudenti()
        List<StudenteEntity> elencoStudenti = dbmsBoundary.getElencoStudenti();

        if (elencoStudenti != null && !elencoStudenti.isEmpty()) {
            // <<create>> VisualizzazioneElencoStudentiBoundary
            VisualizzazioneElencoStudentiBoundary visualizzazioneElencoStudentiBoundary =
                    new VisualizzazioneElencoStudentiBoundary();

            // mostraElencoStudenti(elencoStudenti)
            visualizzazioneElencoStudentiBoundary.mostraElencoStudenti(elencoStudenti);
        } else {
            // <<create>> PopupErroreBoundary
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

            // mostraPopup(testo)
            popupErroreBoundary.mostraPopup("Nessuno studente disponibile.");
        }
    }
}