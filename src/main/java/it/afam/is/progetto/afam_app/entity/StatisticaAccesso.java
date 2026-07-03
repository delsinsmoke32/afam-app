package it.afam.is.progetto.afam_app.entity;

import java.time.LocalDateTime; // Importa tutto quello che serve per JPA

import jakarta.persistence.Entity; // Importa le annotazioni di Lombok
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "statistiche_accessi")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class StatisticaAccesso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataOraAccesso;
    private String indirizzoIp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_condiviso_id", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private LinkCondiviso linkCondiviso;

    @PrePersist
    protected void onCreate() { this.dataOraAccesso = LocalDateTime.now(); }
}
