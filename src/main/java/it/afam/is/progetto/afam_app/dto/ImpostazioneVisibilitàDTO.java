package it.afam.is.progetto.afam_app.dto;

import java.util.List;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpostazioneVisibilitàDTO {

    private Long link_condiviso_id;
    private List<SezioneEntity> sezioni;

}