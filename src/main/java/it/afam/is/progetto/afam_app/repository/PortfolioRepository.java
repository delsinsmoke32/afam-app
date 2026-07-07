package it.afam.is.progetto.afam_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.afam.is.progetto.afam_app.entity.PortfolioEntity;

@Repository
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long> {

    List<PortfolioEntity> findByStudenteId(Long studenteId);
}