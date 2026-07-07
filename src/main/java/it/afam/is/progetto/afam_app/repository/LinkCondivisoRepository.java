package it.afam.is.progetto.afam_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.afam.is.progetto.afam_app.entity.LinkCondivisoEntity;

public interface LinkCondivisoRepository extends JpaRepository<LinkCondivisoEntity, Long> {

    boolean existsByTokenUrl(String tokenUrl);
}