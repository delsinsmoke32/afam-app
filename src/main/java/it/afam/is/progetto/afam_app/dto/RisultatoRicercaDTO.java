package it.afam.is.progetto.afam_app.dto;

import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RisultatoRicercaDTO {
    private StudenteEntity studente;
    private PortfolioEntity portfolio;
}
