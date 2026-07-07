package it.afam.is.progetto.afam_app.entity;

public class Sessione {

    private static StudenteEntity StudenteEntity;

    public void login(StudenteEntity StudenteEntity) {
        Sessione.StudenteEntity = StudenteEntity;
    }

    public void logout() {
        Sessione.StudenteEntity = null;
    }

    public StudenteEntity getStudente() {
        return StudenteEntity;
    }

    public static StudenteEntity getStudenteCorrente() {
        return StudenteEntity;
    }

    public static Long getStudenteId() {
        if (StudenteEntity == null) {
            return null;
        }

        return StudenteEntity.getId();
    }
}

