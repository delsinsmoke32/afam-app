package it.afam.is.progetto.afam_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.afam.is.progetto.afam_app.entity.AllegatoEntity;

@Repository
public interface AllegatoRepository extends JpaRepository<AllegatoEntity, Long> {

    List<AllegatoEntity> findBySezioneId(Long sezioneId);
}