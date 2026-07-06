package it.afam.is.progetto.afam_app.entity;

public class Sessione {

    private static Studente studente;

    public void login(Studente studente) {
        Sessione.studente = studente;
    }

    public void logout() {
        Sessione.studente = null;
    }

    public Studente getStudente() {
        return studente;
    }

    public static Studente getStudenteCorrente() {
        return studente;
    }

    public static Long getStudenteId() {
        if (studente == null) {
            return null;
        }

        return studente.getId();
    }
}