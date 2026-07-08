package it.afam.is.progetto.afam_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.afam.is.progetto.afam_app.entity.StatisticaAccessoEntity;

public interface StatisticaAccessoRepository extends JpaRepository<StatisticaAccessoEntity, Long> {

    List<StatisticaAccessoEntity> findByLinkCondivisoId(Long linkCondivisoId);
}