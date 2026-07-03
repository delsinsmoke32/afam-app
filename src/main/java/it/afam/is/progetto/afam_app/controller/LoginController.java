package it.afam.is.progetto.afam_app.controller;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.boundary.EmailBoundary;
import it.afam.is.progetto.afam_app.entity.Studente;
import it.afam.is.progetto.afam_app.repository.StudenteRepository;

@RestController
@RequestMapping("/api/auth")
// Permette al frontend Angular (porta 4200) di comunicare con il backend senza blocchi CORS
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private StudenteRepository studenteRepository;

    @Autowired
    private DBMSBoundary dbmsBoundary;

    @Autowired
    private EmailBoundary emailBoundary;

    // Endpoint di test per verificare che Angular o il browser leggano i dati dal DB
    @GetMapping("/utenti")
    public List<Studente> getUtenti() {
        return studenteRepository.findAll();
    }

    // DTO interno per ricevere i dati dal FormBoundary di Angular (messaggio 3: CliccaOk(mail, password))
    public static class CredenzialiLoginDTO {
        public String email;
        public String password;
    }

    // DTO interno per ricevere il codice OTP dal FormBoundary OTP (messaggio 16: CliccaOK(OTP))
    public static class OtpDTO {
        public String email;
        public String otp;
    }

    // DTO di risposta: il frontend lo usa per decidere se mostrare la PopupErroreBoundary
    // oppure procedere (mostraFormOTP / MostraHomePage)
    public static class LoginResponseDTO {
        public boolean successo;
        public String messaggioErrore;

        public LoginResponseDTO(boolean successo, String messaggioErrore) {
            this.successo = successo;
            this.messaggioErrore = messaggioErrore;
        }
    }

    // Messaggi 3/4: CliccaOk(mail, password) -> InvioCredenziali(credenziali)
    @PostMapping("/verifica-credenziali")
    public ResponseEntity<LoginResponseDTO> verificaCredenziali(@RequestBody CredenzialiLoginDTO credenziali) {

        // alt: Credenziali IsEmpty [True] (messaggio 4.1: VerificaCredenziali, self-call)
        if (isCredenzialiEmpty(credenziali)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponseDTO(false, "Tutti i campi obbligatori devono essere compilati."));
        }

        // alt: Credenziali IsEmpty [False] -> messaggio 6: VerificaEsistenzaCredenziali
        Optional<Studente> studente = dbmsBoundary.verificaEsistenzaCredenziali(credenziali.email, credenziali.password);

        // alt: Credenziali exists [False]
        if (studente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Email o password errati."));
        }

        // alt: Credenziali exists [True]
        // Messaggio 7: generaCodiceOTP()
        String codice = generaCodiceOtp();
        // Messaggio 8: calcolaScadenza(3) -> 3 minuti
        LocalDateTime scadenza = LocalDateTime.now().plusMinutes(3);
        // Messaggio 9/9.1: salvaOTP/insertSalvaOTP
        dbmsBoundary.salvaOTP(studente.get().getId(), codice, scadenza);
        // Messaggi 10/11: <<create>> EmailBoundary + InviaEmail (simulata, vedi EmailBoundary)
        emailBoundary.inviaEmail(studente.get().getEmail(), codice);

        // Messaggi 12-14 (PopupConfermaBoundary) e 15 (mostraFormOTP) sono lato frontend,
        // eseguiti in reazione a questa risposta di successo.
        return ResponseEntity.ok(new LoginResponseDTO(true, null));
    }

    // Messaggi 16/16.1: CliccaOK(OTP) -> mandaOTP(OTP)
    @PostMapping("/verifica-otp")
    public ResponseEntity<LoginResponseDTO> verificaOtp(@RequestBody OtpDTO otpDto) {
        Optional<Studente> studente = dbmsBoundary.trovaPerEmail(otpDto.email);

        // alt: OTP isRight [True]/[False] (messaggio 16.1.1: ControllaOTP, self-call)
        if (studente.isPresent() && dbmsBoundary.trovaOtpValido(studente.get().getId(), otpDto.otp)) {
            // Messaggi 17-19 (Utente_Auth = questo stesso Studente autenticato, HomePageBoundary) sono lato frontend
            return ResponseEntity.ok(new LoginResponseDTO(true, null));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO(false, "Codice OTP errato o scaduto."));
    }

    // Messaggio 4.1 del Sequence Diagram Login2FA: VerificaCredenziali (controllo campi vuoti, self-call)
    private boolean isCredenzialiEmpty(CredenzialiLoginDTO credenziali) {
        return credenziali.email == null || credenziali.email.isBlank() ||
                credenziali.password == null || credenziali.password.isBlank();
    }

    // Messaggio 7 del Sequence Diagram Login2FA: generaCodiceOTP()
    private String generaCodiceOtp() {
        int codice = RANDOM.nextInt(1_000_000);
        return String.format("%06d", codice);
    }
}
