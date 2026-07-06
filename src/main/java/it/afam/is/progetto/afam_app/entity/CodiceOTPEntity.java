package it.afam.is.progetto.afam_app.entity;

import java.time.LocalDateTime;

public class CodiceOTPEntity {

    private final Studente studente;
    private final String codice;
    private final LocalDateTime scadenza;

    public CodiceOTPEntity(Studente studente, String codice, LocalDateTime scadenza) {
        this.studente = studente;
        this.codice = codice;
        this.scadenza = scadenza;
    }

    public Studente getStudente() {
        return studente;
    }

    public String getCodice() {
        return codice;
    }

    public LocalDateTime getScadenza() {
        return scadenza;
    }
}