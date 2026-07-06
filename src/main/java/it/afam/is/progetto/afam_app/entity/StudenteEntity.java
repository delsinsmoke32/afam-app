package it.afam.is.progetto.afam_app.entity;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;

public class StudenteEntity {

    private final DBMSBoundary dbmsBoundary;

    public StudenteEntity(DBMSBoundary dbmsBoundary) {
        this.dbmsBoundary = dbmsBoundary;
    }

    public void inserisciStudente(Studente studente) {
        dbmsBoundary.insertInserisciStudente(studente);
    }
}