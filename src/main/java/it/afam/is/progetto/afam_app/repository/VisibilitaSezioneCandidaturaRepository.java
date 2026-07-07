package it.afam.is.progetto.afam_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.afam.is.progetto.afam_app.entity.VisibilitaSezioneCandidaturaEntity;

public interface VisibilitaSezioneCandidaturaRepository
        extends JpaRepository<VisibilitaSezioneCandidaturaEntity, Long> {

    List<VisibilitaSezioneCandidaturaEntity> findByLinkCondivisoId(Long linkCondivisoId);

    void deleteByLinkCondivisoId(Long linkCondivisoId);
}