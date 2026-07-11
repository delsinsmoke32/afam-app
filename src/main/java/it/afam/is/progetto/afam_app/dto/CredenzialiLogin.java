package it.afam.is.progetto.afam_app.dto;

public class CredenzialiLogin {

    private String email;
    private String password;

    public CredenzialiLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}



