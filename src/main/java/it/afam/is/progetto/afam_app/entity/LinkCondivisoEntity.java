package it.afam.is.progetto.afam_app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
@Table(name = "link_condivisi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkCondivisoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeRiferimento;

    @Column(nullable = false, unique = true)
    private String tokenUrl;

    @Builder.Default
    private Boolean isAttivo = true;

    private LocalDateTime dataScadenza;

    @Builder.Default
    private Integer visiteLink = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PortfolioEntity portfolio;
}