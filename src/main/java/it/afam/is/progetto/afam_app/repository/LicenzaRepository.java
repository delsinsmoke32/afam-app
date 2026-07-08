package it.afam.is.progetto.afam_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.afam.is.progetto.afam_app.entity.LicenzaEntity;

public interface LicenzaRepository extends JpaRepository<LicenzaEntity, Long> {
}