package it.afam.is.progetto.afam_app.gestioneaccount.control;

import java.time.LocalDateTime;
import java.util.UUID;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.CondivisioneCandidaturaBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.FormCondivisioneBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupConfermaBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.boundary.PopupErroreBoundary;

public class GenerazioneURLController {

    private final CondivisioneCandidaturaBoundary condivisioneCandidaturaBoundary;
    private final DBMSBoundary dbmsBoundary;

    private FormCondivisioneBoundary formCondivisioneBoundary;
    private PopupConfermaBoundary popupConfermaBoundary;

    private Long portfolio_id;
    private Long link_condiviso_id;
    private String urlGenerato;

    public GenerazioneURLController(
            CondivisioneCandidaturaBoundary condivisioneCandidaturaBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.condivisioneCandidaturaBoundary = condivisioneCandidaturaBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void mandaIdPortfolio(Long portfolio_id) {
        this.portfolio_id = portfolio_id;

        // <<create>> FormCondivisioneBoundary
        formCondivisioneBoundary = new FormCondivisioneBoundary(this);

        // mostraForm()
        formCondivisioneBoundary.mostraForm();
    }

    public void mandaNome(String nome) {
        if (formCondivisioneBoundary != null) {
            formCondivisioneBoundary.dispose();
            formCondivisioneBoundary = null;
        }

        if (nome == null || nome.trim().isEmpty()) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Nome condivisione non valido.");
            condivisioneCandidaturaBoundary.mostraPaginaCondivisione();
            return;
        }

        // generaToken()
        String token = generaToken();

        // verificaToken(token)
        boolean esistenzaToken = dbmsBoundary.verificaToken(token);

        // loop: finché il token esiste già nel DBMS
        while (esistenzaToken) {
            token = generaToken();
            esistenzaToken = dbmsBoundary.verificaToken(token);
        }

        // dataScadenza = oggi + 2 settimane
        LocalDateTime dataScadenza = LocalDateTime.now().plusWeeks(2);

        // salvaURL(token, nomeCond, dataScadenza, portfolio_id)
        link_condiviso_id = dbmsBoundary.salvaURL(token, nome.trim(), dataScadenza, portfolio_id);

        if (link_condiviso_id == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Errore durante la generazione dell'URL.");
            condivisioneCandidaturaBoundary.mostraPaginaCondivisione();
            return;
        }

        urlGenerato = costruisciURL(token);

        // <<create>> PopupConfermaBoundary
        popupConfermaBoundary = new PopupConfermaBoundary(
                () -> conferma(),
                () -> annulla()
        );

        // mostraPopup(testo)
        popupConfermaBoundary.mostraPopup("URL generato correttamente. Vuoi gestire manualmente la visibilità delle sezioni?");
    }

    public String generaToken() {
        return UUID.randomUUID().toString();
    }

    private String costruisciURL(String token) {
        return "http://localhost:8080/portfolio/condiviso/" + token;
    }

    public void conferma() {
        if (popupConfermaBoundary != null) {
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        // <<create>> GestioneVisibilitaController
        GestioneVisibilitaController gestioneVisibilitaController =
                new GestioneVisibilitaController(this, dbmsBoundary);

        // conferma()
        // mandaId(portfolio_id)
        gestioneVisibilitaController.conferma(link_condiviso_id, portfolio_id);
    }

    public void annulla() {
        if (popupConfermaBoundary != null) {
            popupConfermaBoundary.dispose();
            popupConfermaBoundary = null;
        }

        // <<create>> GestioneVisibilitaController
        GestioneVisibilitaController gestioneVisibilitaController =
                new GestioneVisibilitaController(this, dbmsBoundary);

        // annulla()
        gestioneVisibilitaController.annulla(link_condiviso_id, portfolio_id);
    }

    public void mostraUrlDopoGestioneVisibilita() {
        // mostraUrlGenerato(URL)
        condivisioneCandidaturaBoundary.mostraUrlGenerato(urlGenerato);
    }
}