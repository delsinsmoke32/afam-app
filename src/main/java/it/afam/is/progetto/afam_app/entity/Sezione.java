package it.afam.is.progetto.afam_app.entity;

import java.util.List; // Importa tutto quello che serve per JPA

import jakarta.persistence.CascadeType; // Importa le annotazioni di Lombok
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "sezioni")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Sezione {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String corpoTesto;
    @Builder.Default
    private Boolean isPubblica = true;
    @Builder.Default
    private Boolean watermarkAttivo = false;
    @Builder.Default
    private Integer ordineVisualizzazione = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Portfolio portfolio;

    @OneToMany(mappedBy = "sezione", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<Allegato> allegati;
}
