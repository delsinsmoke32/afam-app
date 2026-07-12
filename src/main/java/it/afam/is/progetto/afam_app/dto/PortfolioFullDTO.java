package it.afam.is.progetto.afam_app.dto;

import java.util.List;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioFullDTO {

    private PortfolioEntity portfolio;
    private List<SezioneEntity> sezioni;
    private List<AllegatoEntity> allegati;

}