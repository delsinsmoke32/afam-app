package it.afam.is.progetto.afam_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.afam.is.progetto.afam_app.entity.StudenteEntity;

@Repository
public interface StudenteRepository extends JpaRepository<StudenteEntity, Long> {

    boolean existsByCodiceFiscale(String codiceFiscale);

    boolean existsByEmail(String email);

    Optional<StudenteEntity> findByEmailAndPassword(String email, String password);

    Optional<StudenteEntity> findByEmail(String email);

    Optional<StudenteEntity> findByCodiceFiscale(String codiceFiscale);
}



