package it.afam.is.progetto.afam_app.gestioneaccount.dto;

public class CredenzialiRegistrazione {

    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String codiceFiscale;
    private String corsoDiStudi;

    public CredenzialiRegistrazione(
            String nome,
            String cognome,
            String email,
            String password,
            String codiceFiscale,
            String corsoDiStudi
    ) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.codiceFiscale = codiceFiscale;
        this.corsoDiStudi = corsoDiStudi;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getCorsoDiStudi() {
        return corsoDiStudi;
    }
}

