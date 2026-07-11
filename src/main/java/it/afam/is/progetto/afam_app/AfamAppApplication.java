package it.afam.is.progetto.afam_app;

import javax.swing.SwingUtilities;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// Assicurati che l'import punti correttamente al tuo nuovo package della Homepage
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.HomepageBoundary;

@SpringBootApplication
public class AfamAppApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AfamAppApplication.class);
        app.setHeadless(false); // Necessario per far funzionare Swing in Spring Boot
        app.run(args);
    }

    @Bean
    CommandLineRunner avviaInterfacciaSwing(HomepageBoundary homepageBoundary) {
        // Richiama il metodo che mostra la home
        return args -> SwingUtilities.invokeLater(homepageBoundary::mostraHomepage);
    }
}