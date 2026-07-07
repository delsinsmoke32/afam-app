package it.afam.is.progetto.afam_app.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.afam.is.progetto.afam_app.entity.CodiceOTPEntity;

@Repository
public interface CodiceOtpRepository extends JpaRepository<CodiceOTPEntity, Long> {

    // Messaggio 16.1.1 del Sequence Diagram Login2FA: ControllaOTP
    Optional<CodiceOTPEntity> findTopByStudenteIdAndCodiceAndScadenzaAfterOrderByIdDesc(
            Long studenteId,
            String codice,
            LocalDateTime adesso
    );

    void deleteByStudenteId(Long studenteId);
}


