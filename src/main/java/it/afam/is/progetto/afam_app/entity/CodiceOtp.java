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

// Non presente nel diagramma db-init/init.sql: tabella necessaria per realizzare
// il messaggio 9 (salvaOTP) del Sequence Diagram Login2FA.
@Entity
@Table(name = "codici_otp")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CodiceOtp {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 6)
    private String codice;

    @Column(nullable = false)
    private LocalDateTime scadenza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studente_id", nullable = false)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Studente studente;
}
