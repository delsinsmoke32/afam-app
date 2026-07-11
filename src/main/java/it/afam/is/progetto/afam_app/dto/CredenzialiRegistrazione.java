package it.afam.is.progetto.afam_app.dto;

public class CredenzialiRegistrazione {

    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String codiceFiscale;
    private String corsoDiStudi;
    private String dataDiNascita;
    private String linkPersonale;
    private String biografia;

    public CredenzialiRegistrazione(
            String nome,
            String cognome,
            String email,
            String password,
            String codiceFiscale,
            String corsoDiStudi,
            String linkPersonale,
            String biografia,
            String dataDiNascita
    ) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.codiceFiscale = codiceFiscale;
        this.corsoDiStudi = corsoDiStudi;
        this.biografia = biografia;
        this.linkPersonale = linkPersonale;
        this.dataDiNascita = dataDiNascita;
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

    public String getLinkPersonale() { return linkPersonale; }

    public String getBiografia() { return biografia; }

    public String getDataDiNascita() { return dataDiNascita; }
}



