package it.afam.is.progetto.afam_app.controller;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.EmailBoundary;
import it.afam.is.progetto.afam_app.entity.CodiceOTPEntity;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.repository.StudenteRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    private StudenteRepository studenteRepository;

    @Autowired
    private DBMSBoundary dbmsBoundary;

    @Autowired
    private EmailBoundary emailBoundary;

    @GetMapping("/utenti")
    public List<StudenteEntity> getUtenti() {
        return studenteRepository.findAll();
    }

    public static class CredenzialiLoginDTO {
        public String email;
        public String password;
    }

    public static class OtpDTO {
        public String email;
        public String otp;
    }

    public static class LoginResponseDTO {
        public boolean successo;
        public String messaggioErrore;

        public LoginResponseDTO(boolean successo, String messaggioErrore) {
            this.successo = successo;
            this.messaggioErrore = messaggioErrore;
        }
    }

    @PostMapping("/verifica-credenziali")
    public ResponseEntity<LoginResponseDTO> verificaCredenziali(@RequestBody CredenzialiLoginDTO credenziali) {

        if (isCredenzialiEmpty(credenziali)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponseDTO(false, "Tutti i campi obbligatori devono essere compilati."));
        }

        StudenteEntity studente = dbmsBoundary.verificaEsistenzaCredenziali(
                credenziali.email,
                credenziali.password
        );

        if (studente == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDTO(false, "Email o password errati."));
        }

        String codice = generaCodiceOtp();
        LocalDateTime scadenza = LocalDateTime.now().plusMinutes(3);

        CodiceOTPEntity codiceOTPEntity = CodiceOTPEntity.builder()
                .studente(studente)
                .codice(codice)
                .scadenza(scadenza)
                .build();

        dbmsBoundary.salvaOTP(codiceOTPEntity);

        emailBoundary.InviaEmail(
                studente.getEmail(),
                "Codice OTP AFAM",
                "Il tuo codice OTP è: " + codice
        );

        return ResponseEntity.ok(new LoginResponseDTO(true, null));
    }

    @PostMapping("/verifica-otp")
    public ResponseEntity<LoginResponseDTO> verificaOtp(@RequestBody OtpDTO otpDto) {

        StudenteEntity studente = dbmsBoundary.trovaPerEmail(otpDto.email);

        if (studente != null && dbmsBoundary.trovaOtpValido(studente.getId(), otpDto.otp)) {
            return ResponseEntity.ok(new LoginResponseDTO(true, null));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO(false, "Codice OTP errato o scaduto."));
    }

    private boolean isCredenzialiEmpty(CredenzialiLoginDTO credenziali) {
        return credenziali == null
                || credenziali.email == null
                || credenziali.email.isBlank()
                || credenziali.password == null
                || credenziali.password.isBlank();
    }

    private String generaCodiceOtp() {
        int codice = RANDOM.nextInt(1_000_000);
        return String.format("%06d", codice);
    }
}

