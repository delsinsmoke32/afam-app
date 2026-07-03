package it.afam.is.progetto.afam_app.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.afam.is.progetto.afam_app.entity.CodiceOtp;

@Repository
public interface CodiceOtpRepository extends JpaRepository<CodiceOtp, Long> {

    // Messaggio 16.1.1 del Sequence Diagram Login2FA: ControllaOTP (verifica codice non scaduto)
    Optional<CodiceOtp> findTopByStudenteIdAndCodiceAndScadenzaAfterOrderByIdDesc(
            Long studenteId, String codice, LocalDateTime adesso);

    // Non presente nel diagramma: senza questo, ogni login riuscito accumula una riga nuova
    // in codici_otp senza mai ripulire le precedenti (già scadute/inutilizzabili).
    void deleteByStudenteId(Long studenteId);
}
