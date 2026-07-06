package it.afam.is.progetto.afam_app.boundary;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.afam.is.progetto.afam_app.entity.CodiceOtp;
import it.afam.is.progetto.afam_app.entity.Studente;
import it.afam.is.progetto.afam_app.gestioneaccount.dto.CredenzialiRegistrazione;
import it.afam.is.progetto.afam_app.repository.CodiceOtpRepository;
import it.afam.is.progetto.afam_app.repository.StudenteRepository;

@Service
public class DBMSBoundary {

    @Autowired
    private StudenteRepository studenteRepository;

    @Autowired
    private CodiceOtpRepository codiceOtpRepository;

    // Messaggio 13.1 del Sequence Diagram: inserisciCredenziali
    // nome/cognome non compaiono nel diagramma ma sono NOT NULL su Studente (vedi RegistrazioneController)
    public void inserisciCredenziali(String nome, String cognome, String email, String password, String corsoDiStudi, String codiceFiscale) {

        Studente nuovoStudente = Studente.builder()
                .nome(nome)
                .cognome(cognome)
                .email(email)
                .password(password) // Per l'MVP va bene in chiaro
                .corsoDiStudi(corsoDiStudi)
                .codiceFiscale(codiceFiscale)
                .build();

        studenteRepository.save(nuovoStudente);
    }
    
    // Verifica per il blocco "isValid" (messaggio 12)
    public boolean esisteCodiceFiscale(String codiceFiscale) {
        return studenteRepository.existsByCodiceFiscale(codiceFiscale);
    }

    // Non presente nel diagramma: email è UNIQUE su Studente quanto codiceFiscale, senza questo
    // controllo un'email duplicata farebbe fallire l'insert con un 500 invece di un errore gestito.
    public boolean esisteEmail(String email) {
        return studenteRepository.existsByEmail(email);
    }

    // Messaggio 6/6.1 del Sequence Diagram Login2FA: VerificaEsistenzaCredenziali / queryVerificaEsistenzaCredenziali
    public Optional<Studente> verificaEsistenzaCredenziali(String email, String password) {
        return studenteRepository.findByEmailAndPassword(email, password);
    }

    public Optional<Studente> trovaPerEmail(String email) {
        return studenteRepository.findByEmail(email);
    }

    // Messaggio 9/9.1 del Sequence Diagram Login2FA: salvaOTP / insertSalvaOTP
    public void salvaOTP(Long studenteId, String codice, LocalDateTime scadenza) {
        // Non presente nel diagramma: elimina eventuali OTP precedenti dello stesso studente
        // prima di salvarne uno nuovo, altrimenti codici_otp cresce senza mai ripulirsi.
        codiceOtpRepository.deleteByStudenteId(studenteId);

        CodiceOtp codiceOtp = CodiceOtp.builder()
                .studente(studenteRepository.getReferenceById(studenteId))
                .codice(codice)
                .scadenza(scadenza)
                .build();

        codiceOtpRepository.save(codiceOtp);
    }

    // Supporto al messaggio 16.1.1 del Sequence Diagram Login2FA: ControllaOTP (self-call su LoginController)
    public boolean trovaOtpValido(Long studenteId, String codice) {
        return codiceOtpRepository
                .findTopByStudenteIdAndCodiceAndScadenzaAfterOrderByIdDesc(studenteId, codice, LocalDateTime.now())
                .isPresent();
    }

    public void mandaCredenziali(CredenzialiRegistrazione credenziali) {
    Studente nuovoStudente = Studente.builder()
            .nome(credenziali.getNome().trim())
            .cognome(credenziali.getCognome().trim())
            .email(credenziali.getEmail().trim())
            .password(credenziali.getPassword())
            .codiceFiscale(credenziali.getCodiceFiscale().trim().toUpperCase())
            .corsoDiStudi(credenziali.getCorsoDiStudi())
            .provider_autenticazione("LOCAL")
            .build();

    studenteRepository.save(nuovoStudente);
}
}