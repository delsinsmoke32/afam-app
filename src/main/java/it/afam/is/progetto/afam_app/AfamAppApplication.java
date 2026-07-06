package it.afam.is.progetto.afam_app;

import javax.swing.SwingUtilities;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import it.afam.is.progetto.afam_app.gestioneaccount.boundary.AutenticazioneBoundary;

@SpringBootApplication
public class AfamAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AfamAppApplication.class, args);
    }

    @Bean
    CommandLineRunner avviaInterfacciaSwing(AutenticazioneBoundary autenticazioneBoundary) {
        return args -> SwingUtilities.invokeLater(autenticazioneBoundary::mostraPaginaAuth);
    }
}