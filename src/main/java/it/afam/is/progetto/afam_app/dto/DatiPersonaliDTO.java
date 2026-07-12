package it.afam.is.progetto.afam_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatiPersonaliDTO {

    private String nome;
    private String cognome;
    private String linkPersonale;
    private String biografia;
    private String cdS; // Corso di Studi

}