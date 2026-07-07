package it.afam.is.progetto.afam_app.boundary;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.afam.is.progetto.afam_app.entity.CodiceOTPEntity;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.dto.CredenzialiRegistrazione;
import it.afam.is.progetto.afam_app.repository.CodiceOtpRepository;
import it.afam.is.progetto.afam_app.repository.StudenteRepository;

@Service
public class DBMSBoundary {

    @Autowired
    private StudenteRepository studenteRepository;

    @Autowired
    private CodiceOtpRepository codiceOtpRepository;

    public void inserisciCredenziali(
            String nome,
            String cognome,
            String email,
            String password,
            String corsoDiStudi,
            String codiceFiscale
    ) {
        StudenteEntity nuovoStudente = StudenteEntity.builder()
                .nome(nome)
                .cognome(cognome)
                .email(email)
                .password(password)
                .corsoDiStudi(corsoDiStudi)
                .codiceFiscale(codiceFiscale)
                .provider_autenticazione("LOCAL")
                .build();

        studenteRepository.save(nuovoStudente);
    }

    public boolean esisteCodiceFiscale(String codiceFiscale) {
        return studenteRepository.existsByCodiceFiscale(codiceFiscale);
    }

    public boolean esisteEmail(String email) {
        return studenteRepository.existsByEmail(email);
    }

    public StudenteEntity verificaEsistenzaCredenziali(String mail, String password) {
        return queryVerificaEsistenzaCredenziali(mail, password);
    }

    public StudenteEntity queryVerificaEsistenzaCredenziali(String mail, String password) {
        Optional<StudenteEntity> studente = studenteRepository.findByEmailAndPassword(mail.trim(), password);
        return studente.orElse(null);
    }

    public void salvaOTP(CodiceOTPEntity codiceOTP) {
        // insertOTP(codiceOTP)
        insertOTP(codiceOTP);
    }

    public void insertOTP(CodiceOTPEntity codiceOTP) {
        if (codiceOTP == null) {
            return;
        }

        codiceOtpRepository.save(codiceOTP);
    }

    public void insertSalvaOTP(CodiceOTPEntity codiceOTP) {
        // insertOTP(codiceOTP)
        insertOTP(codiceOTP);
    }

    public void insertInserisciStudente(StudenteEntity studente) {
        studenteRepository.save(studente);
    }

    public void mandaCredenziali(CredenzialiRegistrazione credenziali) {
        StudenteEntity nuovoStudente = StudenteEntity.builder()
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

    public StudenteEntity trovaPerEmail(String email) {
        return studenteRepository.findByEmail(email).orElse(null);
    }

    public boolean trovaOtpValido(Long studenteId, String codice) {
        return codiceOtpRepository
                .findTopByStudenteIdAndCodiceAndScadenzaAfterOrderByIdDesc(
                        studenteId,
                        codice,
                        LocalDateTime.now()
                )
                .isPresent();
    }

    public void invalidaToken(Long studente_id) {
        System.out.println("Token invalidato per studente_id: " + studente_id);
    }

    public boolean cercaPassword(Long studente_id, String vecchia_pwd) {
        return queryPassword(studente_id, vecchia_pwd);
    }

    public boolean queryPassword(Long studente_id, String vecchia_pwd) {
        if (studente_id == null || vecchia_pwd == null) {
            return false;
        }

        return studenteRepository.findById(studente_id)
                .filter(studente -> vecchia_pwd.equals(studente.getPassword()))
                .isPresent();
    }

    public void inserisciPassword(Long studente_id, String nuova_pwd) {
        insertPassword(studente_id, nuova_pwd);
    }

    public void insertPassword(Long studente_id, String nuova_pwd) {
        if (studente_id == null || nuova_pwd == null) {
            return;
        }

        studenteRepository.findById(studente_id).ifPresent(studente -> {
            studente.setPassword(nuova_pwd);
            studenteRepository.save(studente);
        });
    }

    public void aggiornaDati(Long studente_id, Map<String, String> dati) {
        // insertDati(studente_id, dati)
        insertDati(studente_id, dati);
    }

    public void insertDati(Long studente_id, Map<String, String> dati) {
        if (studente_id == null || dati == null) {
            return;
        }

        studenteRepository.findById(studente_id).ifPresent(studente -> {
            studente.setNome(dati.get("nome"));
            studente.setCognome(dati.get("cognome"));
            studente.setCorsoDiStudi(dati.get("CdS"));
            studente.setBiografia(dati.get("bio"));

            studenteRepository.save(studente);
        });
    }

    public StudenteEntity checkPassword(Long studente_id, String password) {
        return queryCheckPassword(studente_id, password);
    }

    public StudenteEntity queryCheckPassword(Long studente_id, String password) {
        if (studente_id == null || password == null) {
            return null;
        }

        return studenteRepository.findById(studente_id)
                .filter(studente -> password.equals(studente.getPassword()))
                .orElse(null);
    }

    public void cancellaStudente(Long studente_id) {
        // deleteStudente(studente_id)
        deleteStudente(studente_id);
    }

    public void deleteStudente(Long studente_id) {
        if (studente_id == null) {
            return;
        }

        studenteRepository.deleteById(studente_id);
    }

    public StudenteEntity verificaEsistenza(String mail) {
        return queryVerificaEsistenza(mail);
    }

    public StudenteEntity queryVerificaEsistenza(String mail) {
        if (mail == null) {
            return null;
        }

        return studenteRepository.findByEmail(mail.trim()).orElse(null);
    }

    public void salvaPwd(Long studente_id, String password) {
        // insertPwd(userID, password)
        insertPwd(studente_id, password);
    }

    public void insertPwd(Long studente_id, String password) {
        if (studente_id == null || password == null) {
            return;
        }

        studenteRepository.findById(studente_id).ifPresent(studente -> {
            studente.setPassword(password);
            studenteRepository.save(studente);
        });
    }

    public StudenteEntity codiceFiscaleExists(String codiceFiscale) {
        return queryCodiceFiscaleExists(codiceFiscale);
    }

    public StudenteEntity queryCodiceFiscaleExists(String codiceFiscale) {
        if (codiceFiscale == null) {
            return null;
        }

        return studenteRepository.findByCodiceFiscale(codiceFiscale.trim().toUpperCase())
                .orElse(null);
    }

    public void memorizzaDati(StudenteEntity studente) {
        // insertMemorizzaDati(studente)
        insertMemorizzaDati(studente);
    }

    public void insertMemorizzaDati(StudenteEntity studente) {
        if (studente == null) {
            return;
        }

        studenteRepository.save(studente);
    }
}