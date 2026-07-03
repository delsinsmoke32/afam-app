package it.afam.is.progetto.afam_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.afam.is.progetto.afam_app.entity.Studente;
import it.afam.is.progetto.afam_app.repository.StudenteRepository;

import java.util.List;

@SpringBootApplication
public class AfamAppApplication implements CommandLineRunner {

	@Autowired
	private StudenteRepository studenteRepository;

	public static void main(String[] args) {
		SpringApplication.run(AfamAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("=== SVUOTAMENTO E POPOLAMENTO DB IN CORSO ===");

		// 1. Pulizia esplicita di sicurezza
		studenteRepository.deleteAll();

		// 2. Creazione dati di test realistici per il sistema AFAM
		Studente studente1 = Studente.builder()
				.nome("Claudio")
				.cognome("Rossi")
				.email("claudio.rossi@afam.it")
				.password("password123")
				.codiceFiscale("RSSCLD05A01H501U")
				.build();

		Studente studente2 = Studente.builder()
				.nome("Elena")
				.cognome("Bianchi")
				.email("elena.bianchi@afam.it")
				.password("password123")
				.codiceFiscale("BNCLNE05A01H501V")
				.build();

		Studente professore = Studente.builder()
				.nome("Marco")
				.cognome("Verdi")
				.email("marco.verdi@afam.it")
				.password("password123")
				.codiceFiscale("VRDMRC05A01H501W")
				.build();

		// 3. Salvataggio di massa nel DB di Docker
		studenteRepository.saveAll(List.of(studente1, studente2, professore));

		System.out.println("=== DB POPOLATO CON SUCCESSO! Record attuali: " + studenteRepository.count() + " ===");
	}
}