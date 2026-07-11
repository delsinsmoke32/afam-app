package it.afam.is.progetto.afam_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
        name = "visibilita_sezioni_candidatura",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"link_condiviso_id", "sezione_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisibilitaSezioneCandidaturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "is_visibile", nullable = false)
    private Boolean visibile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_condiviso_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private LinkCondivisoEntity linkCondiviso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sezione_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SezioneEntity sezione;
}