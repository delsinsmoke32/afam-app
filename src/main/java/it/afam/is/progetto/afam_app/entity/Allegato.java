package it.afam.is.progetto.afam_app.entity;

import java.time.LocalDate; // Importa tutto quello che serve per JPA

import jakarta.persistence.Column; // Importa le annotazioni di Lombok
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "allegati")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Allegato {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeFile;
    
    @Column(nullable = false, length = 200)
    private String titoloOpera;

    @Column(columnDefinition = "TEXT")
    private String descrizioneBreve;

    @Column(length = 150)
    private String autoreOpera;

    private String tipoFile;
    @Column(nullable = false, length = 500)
    private String percorsoRisorsa;
    private LocalDate dataCreazione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sezione_id", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Sezione sezione;
}