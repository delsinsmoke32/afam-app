package it.afam.is.progetto.afam_app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.afam.is.progetto.afam_app.entity.LicenzaEntity;
import it.afam.is.progetto.afam_app.repository.LicenzaRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner inizializzaLicenze(LicenzaRepository licenzaRepository) {
        return args -> {
            if (licenzaRepository.count() > 0) {
                return;
            }

            licenzaRepository.save(new LicenzaEntity(
                    "Tutti i diritti riservati",
                    "Nessun utilizzo consentito senza autorizzazione."
            ));

            licenzaRepository.save(new LicenzaEntity(
                    "CC BY",
                    "Utilizzo consentito con attribuzione dell'autore."
            ));

            licenzaRepository.save(new LicenzaEntity(
                    "CC BY-SA",
                    "Utilizzo consentito con attribuzione e stessa licenza."
            ));

            licenzaRepository.save(new LicenzaEntity(
                    "CC BY-NC",
                    "Utilizzo consentito con attribuzione, solo per scopi non commerciali."
            ));

            System.out.println("Licenze iniziali inserite correttamente.");
        };
    }
}