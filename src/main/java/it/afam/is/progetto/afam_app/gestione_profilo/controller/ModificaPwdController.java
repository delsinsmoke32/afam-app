package it.afam.is.progetto.afam_app.gestione_profilo.controller;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.gestione_profilo.boundary.FormModPwdBoundary;
import it.afam.is.progetto.afam_app.gestione_profilo.boundary.GestioneProfiloBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.common.PopupSuccessoBoundary;

public class ModificaPwdController {

    private final GestioneProfiloBoundary gestioneProfiloBoundary;
    private final DBMSBoundary dbmsBoundary;
    private final FormModPwdBoundary formModPwdBoundary;

    public ModificaPwdController(
            GestioneProfiloBoundary gestioneProfiloBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.gestioneProfiloBoundary = gestioneProfiloBoundary;
        this.dbmsBoundary = dbmsBoundary;

        this.formModPwdBoundary = new FormModPwdBoundary(this);
        this.formModPwdBoundary.mostraForm();
    }

    public void mandaDati(String vecchia_pwd, String nuova_pwd, String conf_pwd) {
        formModPwdBoundary.chiudiForm();

        Sessione sessione = new Sessione();
        StudenteEntity StudenteEntity = sessione.getStudente();

        if (StudenteEntity == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Sessione non valida.");
            return;
        }

        Long studente_id = StudenteEntity.getId();

        boolean vecchiaPwdEsiste = cercaPassword(studente_id, vecchia_pwd);

        if (vecchiaPwdEsiste) {
            boolean uguaglianza = controllaUguaglianza(nuova_pwd, conf_pwd);
            boolean req = controllaReq(nuova_pwd);

            if (uguaglianza && req) {
                dbmsBoundary.inserisciPassword(studente_id, nuova_pwd);

                PopupSuccessoBoundary popupSuccessoBoundary = new PopupSuccessoBoundary();
                popupSuccessoBoundary.mostraPopup("Password modificata con successo.");

                gestioneProfiloBoundary.mostraGestioneProfilo();
            } else {
                PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
                popupErroreBoundary.mostraPopup("Le password non coincidono o non rispettano i requisiti.");
            }
        } else {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Vecchia password errata.");
        }
    }

    public boolean cercaPassword(Long studente_id, String vecchia_pwd) {
        return dbmsBoundary.queryPassword(studente_id, vecchia_pwd);
    }

    public boolean controllaUguaglianza(String nuova_pwd, String conf_pwd) {
        return nuova_pwd != null
                && conf_pwd != null
                && nuova_pwd.equals(conf_pwd);
    }

    public boolean controllaReq(String nuova_pwd) {
        return nuova_pwd != null
                && nuova_pwd.length() >= 8;
    }
}



