package it.afam.is.progetto.afam_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.afam.is.progetto.afam_app.entity.SezioneEntity;

@Repository
public interface SezioneRepository extends JpaRepository<SezioneEntity, Long> {

    List<SezioneEntity> findByPortfolioId(Long portfolioId);
}