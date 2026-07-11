package it.afam.is.progetto.afam_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;

@RestController
@RequestMapping("/api/registrazione")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrazioneController {

    @Autowired
    private DBMSBoundary dbmsBoundary;

    // DTO interno per ricevere i dati dal FormBoundary di Angular (messaggio 4: CliccaOK(credenziali))
    // Nota: nome/cognome non compaiono nel diagramma (InserisciCredenziali porta solo Mail, Password,
    // CorsoDiStudi, CodiceFiscale), ma sono NOT NULL su StudenteEntity: aggiunti qui per rendere l'inserimento
    // eseguibile, deviazione esplicita concordata rispetto al diagramma.
    public static class CredenzialiDTO {
        public String nome;
        public String cognome;
        public String email;
        public String password;
        public String corsoDiStudi;
        public String codiceFiscale;
    }

    // DTO di risposta: il frontend lo usa per decidere se mostrare la PopupErroreBoundary
    // (messaggi 6-11 / 15-20) oppure eseguire mostraAutenticazione() (messaggio 14)
    public static class RegistrazioneResponseDTO {
        public boolean successo;
        public String messaggioErrore;

        public RegistrazioneResponseDTO(boolean successo, String messaggioErrore) {
            this.successo = successo;
            this.messaggioErrore = messaggioErrore;
        }
    }

    // Messaggi 4/5: CliccaOK(credenziali) -> VerificaCredenziali(credenziali)
    @PostMapping("/verifica-e-inserisci")
    public ResponseEntity<RegistrazioneResponseDTO> verificaCredenziali(@RequestBody CredenzialiDTO credenziali) {

        // PRIMO BLOCCO ALT: Credenziali isEmpty
        if (isCredenzialiEmpty(credenziali)) {
            // [True] -> Ritorna errore al frontend per creare la PopupErroreBoundary
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegistrazioneResponseDTO(false, "Tutti i campi obbligatori devono essere compilati."));
        }

        // SECONDO BLOCCO ALT: Credenziali isValid (CF o email già esistenti)
        if (isCodiceFiscaleValid(credenziali) && isEmailValid(credenziali)) {
            // [True] -> Messaggio 13: InserisciCredenziali
            dbmsBoundary.inserisciCredenziali(
                credenziali.nome,
                credenziali.cognome,
                credenziali.email,
                credenziali.password,
                credenziali.corsoDiStudi,
                credenziali.codiceFiscale
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(new RegistrazioneResponseDTO(true, null));
        } else {
            // [False] -> Ritorna errore al frontend per creare la PopupErroreBoundary
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RegistrazioneResponseDTO(false, "Utente già registrato con questo Codice Fiscale o questa Email."));
        }
    }

    // Messaggio 5.1 del Sequence Diagram: VerificaCredenziali (controllo campi vuoti, self-call)
    private boolean isCredenzialiEmpty(CredenzialiDTO credenziali) {
        return credenziali.nome == null || credenziali.nome.isBlank() ||
                credenziali.cognome == null || credenziali.cognome.isBlank() ||
                credenziali.email == null || credenziali.email.isBlank() ||
                credenziali.password == null || credenziali.password.isBlank() ||
                credenziali.codiceFiscale == null || credenziali.codiceFiscale.isBlank();
    }

    // Messaggio 12 del Sequence Diagram: VerificaCredenziali (controllo unicità Codice Fiscale, self-call)
    private boolean isCodiceFiscaleValid(CredenzialiDTO credenziali) {
        return !dbmsBoundary.esisteCodiceFiscale(credenziali.codiceFiscale);
    }

    // Non presente nel diagramma: email è UNIQUE su StudenteEntity quanto codiceFiscale (vedi DBMSBoundary.esisteEmail)
    private boolean isEmailValid(CredenzialiDTO credenziali) {
        return !dbmsBoundary.esisteEmail(credenziali.email);
    }
}



