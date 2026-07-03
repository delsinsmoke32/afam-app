package it.afam.is.progetto.afam_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.afam.is.progetto.afam_app.entity.Studente;

@Repository
public interface StudenteRepository extends JpaRepository<Studente, Long> {

    // Metodo magico di Spring per controllare se esiste già il CF
    boolean existsByCodiceFiscale(String codiceFiscale);

    // Utile per il futuro
    boolean existsByEmail(String email);

    // Messaggio 6.1 del Sequence Diagram Login2FA: queryVerificaEsistenzaCredenziali
    Optional<Studente> findByEmailAndPassword(String email, String password);

    Optional<Studente> findByEmail(String email);
}