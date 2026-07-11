package it.afam.is.progetto.afam_app.entity;

public class Sessione {

    // 1. L'unica istanza statica permessa
    private static Sessione istanza;

    // 2. I dati salvati nella sessione (ora con la lettera minuscola)
    private StudenteEntity studente;

    // 3. Costruttore privato: blocca definitivamente "new Sessione()" da fuori!
    private Sessione() {
    }

    // 4. Metodo per accedere all'unica sessione valida
    public static Sessione getInstance() {
        if (istanza == null) {
            istanza = new Sessione();
        }
        return istanza;
    }

    // --- METODI DI ISTANZA (Aggiornati) ---

    public void login(StudenteEntity studente) {
        this.studente = studente;
    }

    public void logout() {
        this.studente = null;
    }

    public StudenteEntity getStudente() {
        return this.studente;
    }

    // --- METODI STATICI (Mantenuti per retrocompatibilità) ---
    // Usano getInstance() sotto il cofano, così il resto dell'app non si rompe

    public static StudenteEntity getStudenteCorrente() {
        return getInstance().getStudente();
    }

    public static Long getStudenteId() {
        StudenteEntity studenteAttuale = getInstance().getStudente();

        if (studenteAttuale == null) {
            return null;
        }

        return studenteAttuale.getId();
    }
}