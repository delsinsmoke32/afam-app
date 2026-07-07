package it.afam.is.progetto.afam_app.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
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
@Table(name = "portfoli")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Builder.Default
    private Boolean downloadAbilitato = true;

    @Builder.Default
    private Integer visiteTotali = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studente_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private StudenteEntity studente;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<SezioneEntity> sezioni; 
}   

