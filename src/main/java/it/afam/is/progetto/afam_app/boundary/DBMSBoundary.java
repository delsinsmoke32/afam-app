package it.afam.is.progetto.afam_app.boundary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.entity.CodiceOTPEntity;
import it.afam.is.progetto.afam_app.entity.LicenzaEntity;
import it.afam.is.progetto.afam_app.entity.LinkCondivisoEntity;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.entity.StatisticaAccessoEntity;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.entity.VisibilitaSezioneCandidaturaEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.dto.CredenzialiRegistrazione;
import it.afam.is.progetto.afam_app.repository.AllegatoRepository;
import it.afam.is.progetto.afam_app.repository.CodiceOtpRepository;
import it.afam.is.progetto.afam_app.repository.LicenzaRepository;
import it.afam.is.progetto.afam_app.repository.LinkCondivisoRepository;
import it.afam.is.progetto.afam_app.repository.PortfolioRepository;
import it.afam.is.progetto.afam_app.repository.SezioneRepository;
import it.afam.is.progetto.afam_app.repository.StatisticaAccessoRepository;
import it.afam.is.progetto.afam_app.repository.StudenteRepository;
import it.afam.is.progetto.afam_app.repository.VisibilitaSezioneCandidaturaRepository;

@Service
public class DBMSBoundary {

    @Autowired
    private StudenteRepository studenteRepository;

    @Autowired
    private CodiceOtpRepository codiceOtpRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private SezioneRepository sezioneRepository;

    @Autowired
    private AllegatoRepository allegatoRepository;

    @Autowired
    private LinkCondivisoRepository linkCondivisoRepository;

    @Autowired
    private VisibilitaSezioneCandidaturaRepository visibilitaSezioneCandidaturaRepository;

    @Autowired
    private StatisticaAccessoRepository statisticaAccessoRepository;

    @Autowired
    private LicenzaRepository licenzaRepository;

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
        insertOTP(codiceOTP);
    }

    public void insertOTP(CodiceOTPEntity codiceOTP) {
        if (codiceOTP == null) {
            return;
        }

        codiceOtpRepository.save(codiceOTP);
    }

    public void insertSalvaOTP(CodiceOTPEntity codiceOTP) {
        insertOTP(codiceOTP);
    }

    public void insertInserisciStudente(StudenteEntity studente) {
        if (studente == null) {
            return;
        }

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
        insertMemorizzaDati(studente);
    }

    public void insertMemorizzaDati(StudenteEntity studente) {
        if (studente == null) {
            return;
        }

        studenteRepository.save(studente);
    }

    public void creaPortfolio(PortfolioEntity portfolio) {
        queryCreazionePortfolio(portfolio);
    }

    public void queryCreazionePortfolio(PortfolioEntity portfolio) {
        if (portfolio == null) {
            return;
        }

        portfolioRepository.save(portfolio);
    }

    public void inserisciSezione(SezioneEntity sezione) {
        queryInserimentoSezione(sezione);
    }

    public void queryInserimentoSezione(SezioneEntity sezione) {
        if (sezione == null) {
            return;
        }

        sezioneRepository.save(sezione);
    }

    public PortfolioEntity cercaPortfolio(Long portfolio_id) {
        if (portfolio_id == null) {
            return null;
        }

        return portfolioRepository.findById(portfolio_id).orElse(null);
    }

    public List<PortfolioEntity> recuperaListaPortfoli(Long studente_id) {
        return queryListaPortfoli(studente_id);
    }

    public List<PortfolioEntity> queryListaPortfoli(Long studente_id) {
        if (studente_id == null) {
            return null;
        }

        return portfolioRepository.findByStudenteId(studente_id);
    }

    public PortfolioEntity recuperaPortfolio(Long portfolio_id) {
        if (portfolio_id == null) {
            return null;
        }

        return portfolioRepository.findById(portfolio_id).orElse(null);
    }

    public List<SezioneEntity> recuperaSezioniPortfolio(Long portfolio_id) {
        return queryRecuperaSezioniPortfolio(portfolio_id);
    }

    public List<SezioneEntity> queryRecuperaSezioniPortfolio(Long portfolio_id) {
        if (portfolio_id == null) {
            return null;
        }

        return sezioneRepository.findByPortfolioId(portfolio_id);
    }

    public SezioneEntity cercaSezione(Long sezione_id) {
        if (sezione_id == null) {
            return null;
        }

        return sezioneRepository.findById(sezione_id).orElse(null);
    }

    public void salvaFile(AllegatoEntity allegato) {
        insertFile(allegato);
    }

    public void insertFile(AllegatoEntity allegato) {
        if (allegato == null) {
            return;
        }

        allegatoRepository.save(allegato);
    }

    public List<AllegatoEntity> recuperaAllegati(Long sezione_id) {
        return queryAllegati(sezione_id);
    }

    public List<AllegatoEntity> queryAllegati(Long sezione_id) {
        if (sezione_id == null) {
            return null;
        }

        return allegatoRepository.findBySezioneId(sezione_id);
    }

    public AllegatoEntity recuperaMetadati(Long allegato_id) {
        if (allegato_id == null) {
            return null;
        }

        return allegatoRepository.findById(allegato_id).orElse(null);
    }

    public void aggiornaMetadati(Long allegato_id, String titolo, String descrizione, String autori) {
        updateMetadati(allegato_id, titolo, descrizione, autori);
    }

    public void updateMetadati(Long allegato_id, String titolo, String descrizione, String autori) {
        if (allegato_id == null) {
            return;
        }

        allegatoRepository.findById(allegato_id).ifPresent(allegato -> {
            allegato.setTitoloOpera(titolo);
            allegato.setDescrizioneBreve(descrizione);
            allegato.setAutoreOpera(autori);

            allegatoRepository.save(allegato);
        });
    }

    public String recuperaPercorsoAllegato(Long allegato_id) {
        return queryPercorsoAllegato(allegato_id);
    }

    public String queryPercorsoAllegato(Long allegato_id) {
        if (allegato_id == null) {
            return null;
        }

        return allegatoRepository.findById(allegato_id)
                .map(AllegatoEntity::getPercorsoRisorsa)
                .orElse(null);
    }

    public void rimuoviAllegato(Long allegato_id) {
        deleteAllegato(allegato_id);
    }

    public void deleteAllegato(Long allegato_id) {
        if (allegato_id == null) {
            return;
        }

        allegatoRepository.deleteById(allegato_id);
    }

    public List<String> recuperaPathAllegatiSezione(Long sezione_id) {
        return queryPathSezione(sezione_id);
    }

    public List<String> queryPathSezione(Long sezione_id) {
        if (sezione_id == null) {
            return null;
        }

        List<AllegatoEntity> allegati = allegatoRepository.findBySezioneId(sezione_id);
        List<String> pathAllegati = new ArrayList<>();

        for (AllegatoEntity allegato : allegati) {
            pathAllegati.add(allegato.getPercorsoRisorsa());
        }

        return pathAllegati;
    }

    public void eliminaSezione(Long idSezione, Long idPortfolio) {
        queryEliminaSezione(idSezione, idPortfolio);
    }

    public void queryEliminaSezione(Long idSezione, Long idPortfolio) {
        if (idSezione == null) {
            return;
        }

        sezioneRepository.deleteById(idSezione);
    }

    public List<PortfolioEntity> recuperaPortfoliStudente(Long idStudente) {
        return queryRecuperaPortfoliStudente(idStudente);
    }

    public List<PortfolioEntity> queryRecuperaPortfoliStudente(Long studente_id) {
        if (studente_id == null) {
            return null;
        }

        return portfolioRepository.findByStudenteId(studente_id);
    }

    public List<String> recuperaPathAllegati(Long portfolio_id) {
        return queryRecuperaPathAllegati(portfolio_id);
    }

    public List<String> queryRecuperaPathAllegati(Long portfolio_id) {
        if (portfolio_id == null) {
            return null;
        }

        List<SezioneEntity> sezioni = sezioneRepository.findByPortfolioId(portfolio_id);
        List<String> pathAllegati = new ArrayList<>();

        for (SezioneEntity sezione : sezioni) {
            List<AllegatoEntity> allegati = allegatoRepository.findBySezioneId(sezione.getId());

            for (AllegatoEntity allegato : allegati) {
                pathAllegati.add(allegato.getPercorsoRisorsa());
            }
        }

        return pathAllegati;
    }

    public void eliminaPortfolio(Long idPortfolio, Long idStudente) {
        queryEliminaPortfolio(idPortfolio, idStudente);
    }

    public void queryEliminaPortfolio(Long idPortfolio, Long idStudente) {
        if (idPortfolio == null || idStudente == null) {
            return;
        }

        PortfolioEntity portfolio = portfolioRepository.findById(idPortfolio).orElse(null);

        if (portfolio == null) {
            return;
        }

        if (portfolio.getStudente() == null || portfolio.getStudente().getId() == null) {
            return;
        }

        if (!portfolio.getStudente().getId().equals(idStudente)) {
            return;
        }

        portfolioRepository.delete(portfolio);
    }

    public boolean verificaToken(String token) {
        return linkCondivisoRepository.existsByTokenUrl(token);
    }

    public Long salvaURL(String token, String nomeCond, LocalDateTime dataScadenza, Long portfolio_id) {
        return insertUrl(token, nomeCond, dataScadenza, portfolio_id);
    }

    public Long insertUrl(String token, String nomeCond, LocalDateTime dataScadenza, Long portfolio_id) {
        PortfolioEntity portfolio = recuperaPortfolio(portfolio_id);

        if (portfolio == null) {
            return null;
        }

        LinkCondivisoEntity linkCondiviso = LinkCondivisoEntity.builder()
                .tokenUrl(token)
                .nomeRiferimento(nomeCond)
                .dataScadenza(dataScadenza)
                .portfolio(portfolio)
                .isAttivo(true)
                .visiteLink(0)
                .build();

        LinkCondivisoEntity salvato = linkCondivisoRepository.save(linkCondiviso);

        return salvato.getId();
    }

    public void aggiornaImpostazioni(Map<Long, Boolean> impostazioni, Long link_condiviso_id) {
        updateImpostazioni(impostazioni, link_condiviso_id);
    }

    @Transactional
    public void updateImpostazioni(Map<Long, Boolean> impostazioni, Long link_condiviso_id) {
        if (impostazioni == null || link_condiviso_id == null) {
            return;
        }

        LinkCondivisoEntity linkCondiviso = linkCondivisoRepository.findById(link_condiviso_id).orElse(null);

        if (linkCondiviso == null) {
            return;
        }

        visibilitaSezioneCandidaturaRepository.deleteByLinkCondivisoId(link_condiviso_id);

        for (Map.Entry<Long, Boolean> entry : impostazioni.entrySet()) {
            Long sezione_id = entry.getKey();
            Boolean visibile = entry.getValue();

            SezioneEntity sezione = cercaSezione(sezione_id);

            if (sezione != null) {
                VisibilitaSezioneCandidaturaEntity visibilita =
                        VisibilitaSezioneCandidaturaEntity.builder()
                                .linkCondiviso(linkCondiviso)
                                .sezione(sezione)
                                .visibile(Boolean.TRUE.equals(visibile))
                                .build();

                visibilitaSezioneCandidaturaRepository.save(visibilita);
            }
        }
    }

    public LinkCondivisoEntity verificaLink(String URL) {
        return queryVerificaLink(URL);
    }

    public LinkCondivisoEntity queryVerificaLink(String URL) {
        if (URL == null || URL.trim().isEmpty()) {
            return null;
        }

        String token = estraiTokenDaURL(URL);

        if (token == null || token.trim().isEmpty()) {
            return null;
        }

        LinkCondivisoEntity linkCondiviso =
                linkCondivisoRepository.findByTokenUrl(token).orElse(null);

        if (linkCondiviso == null) {
            return null;
        }

        if (!Boolean.TRUE.equals(linkCondiviso.getIsAttivo())) {
            return null;
        }

        if (linkCondiviso.getDataScadenza() != null
                && linkCondiviso.getDataScadenza().isBefore(LocalDateTime.now())) {
            return null;
        }

        return linkCondiviso;
    }

    private String estraiTokenDaURL(String URL) {
        String valore = URL.trim();

        int ultimoSlash = valore.lastIndexOf("/");

        if (ultimoSlash >= 0 && ultimoSlash < valore.length() - 1) {
            return valore.substring(ultimoSlash + 1);
        }

        return valore;
    }

    public void registraAccesso(
            String nome,
            String cognome,
            String ruolo,
            LocalDateTime currentTime,
            Long link_condiviso_id
    ) {
        insertAccesso(nome, cognome, ruolo, currentTime, link_condiviso_id);
    }

    public void insertAccesso(
            String nome,
            String cognome,
            String ruolo,
            LocalDateTime currentTime,
            Long link_condiviso_id
    ) {
        if (link_condiviso_id == null) {
            return;
        }

        LinkCondivisoEntity linkCondiviso =
                linkCondivisoRepository.findById(link_condiviso_id).orElse(null);

        if (linkCondiviso == null) {
            return;
        }

        StatisticaAccessoEntity accesso = StatisticaAccessoEntity.builder()
                .nome(nome)
                .cognome(cognome)
                .ruolo(ruolo)
                .dataOraAccesso(currentTime)
                .linkCondiviso(linkCondiviso)
                .build();

        statisticaAccessoRepository.save(accesso);

        Integer visiteAttuali = linkCondiviso.getVisiteLink();

        if (visiteAttuali == null) {
            visiteAttuali = 0;
        }

        linkCondiviso.setVisiteLink(visiteAttuali + 1);
        linkCondivisoRepository.save(linkCondiviso);
    }

    @Transactional(readOnly = true)
    public PortfolioEntity recuperaPortfolioCondiviso(Long portfolio_id, Long link_condiviso_id) {
        return queryPortfolioCondiviso(portfolio_id, link_condiviso_id);
    }

    @Transactional(readOnly = true)
    public PortfolioEntity queryPortfolioCondiviso(Long portfolio_id, Long link_condiviso_id) {
        if (portfolio_id == null || link_condiviso_id == null) {
            return null;
        }

        LinkCondivisoEntity linkCondiviso =
                linkCondivisoRepository.findById(link_condiviso_id).orElse(null);

        if (linkCondiviso == null || linkCondiviso.getPortfolio() == null) {
            return null;
        }

        Long idPortfolioDelLink = linkCondiviso.getPortfolio().getId();

        if (!portfolio_id.equals(idPortfolioDelLink)) {
            return null;
        }

        return portfolioRepository.findById(portfolio_id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<SezioneEntity> recuperaSezioniVisibiliCondivisione(Long link_condiviso_id) {
        if (link_condiviso_id == null) {
            return new ArrayList<>();
        }

        List<VisibilitaSezioneCandidaturaEntity> impostazioni =
                visibilitaSezioneCandidaturaRepository.findByLinkCondivisoId(link_condiviso_id);

        if (impostazioni == null || impostazioni.isEmpty()) {
            return new ArrayList<>();
        }

        List<SezioneEntity> sezioniVisibili = new ArrayList<>();

        for (VisibilitaSezioneCandidaturaEntity impostazione : impostazioni) {
            if (!Boolean.TRUE.equals(impostazione.getVisibile())) {
                continue;
            }

            SezioneEntity sezioneProxy = impostazione.getSezione();

            if (sezioneProxy == null || sezioneProxy.getId() == null) {
                continue;
            }

            SezioneEntity sezioneVera =
                    sezioneRepository.findById(sezioneProxy.getId()).orElse(null);

            if (sezioneVera != null) {
                sezioniVisibili.add(sezioneVera);
            }
        }

        return sezioniVisibili;
    }

    public Boolean recuperaImpostazioneDownload(Long portfolio_id) {
        return queryRecuperaImpostazioneDownload(portfolio_id);
    }

    public Boolean queryRecuperaImpostazioneDownload(Long portfolio_id) {
        if (portfolio_id == null) {
            return false;
        }

        return portfolioRepository.findById(portfolio_id)
                .map(PortfolioEntity::getDownloadAbilitato)
                .orElse(false);
    }

    public AllegatoEntity recuperaAllegato(Long allegato_id) {
        return queryRecuperaAllegato(allegato_id);
    }

    public AllegatoEntity queryRecuperaAllegato(Long allegato_id) {
        if (allegato_id == null) {
            return null;
        }

        return allegatoRepository.findById(allegato_id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AllegatoEntity> recuperaAllegatiSezioniVisibili(Long link_condiviso_id) {
        if (link_condiviso_id == null) {
            return new ArrayList<>();
        }

        List<VisibilitaSezioneCandidaturaEntity> impostazioni =
                visibilitaSezioneCandidaturaRepository.findByLinkCondivisoId(link_condiviso_id);

        if (impostazioni == null || impostazioni.isEmpty()) {
            return new ArrayList<>();
        }

        List<AllegatoEntity> allegatiVisibili = new ArrayList<>();

        for (VisibilitaSezioneCandidaturaEntity impostazione : impostazioni) {
            if (!Boolean.TRUE.equals(impostazione.getVisibile())) {
                continue;
            }

            SezioneEntity sezioneProxy = impostazione.getSezione();

            if (sezioneProxy == null || sezioneProxy.getId() == null) {
                continue;
            }

            List<AllegatoEntity> allegatiSezione =
                    allegatoRepository.findBySezioneId(sezioneProxy.getId());

            if (allegatiSezione != null) {
                allegatiVisibili.addAll(allegatiSezione);
            }
        }

        return allegatiVisibili;
    }

    public void salvaImpostazione(Long portfolio_id, Boolean impostazione) {
        salvaImpostazioneDownload(portfolio_id, impostazione);
    }

    public void salvaImpostazioneDownload(Long portfolio_id, Boolean impostazione) {
        if (portfolio_id == null || impostazione == null) {
            return;
        }

        portfolioRepository.findById(portfolio_id).ifPresent(portfolio -> {
            portfolio.setDownloadAbilitato(impostazione);
            portfolioRepository.save(portfolio);
        });
    }

    @Transactional(readOnly = true)
    public Long recuperaPortfolioIdDaLink(Long link_condiviso_id) {
        if (link_condiviso_id == null) {
            return null;
        }

        LinkCondivisoEntity linkCondiviso =
                linkCondivisoRepository.findById(link_condiviso_id).orElse(null);

        if (linkCondiviso == null || linkCondiviso.getPortfolio() == null) {
            return null;
        }

        return linkCondiviso.getPortfolio().getId();
    }

    @Transactional(readOnly = true)
    public List<LinkCondivisoEntity> cercaURLAttivi() {
        return queryURLAttivi();
    }

    @Transactional(readOnly = true)
    public List<LinkCondivisoEntity> queryURLAttivi() {
        Long studente_id = Sessione.getStudenteId();

        if (studente_id == null) {
            return null;
        }

        List<LinkCondivisoEntity> tuttiLink = linkCondivisoRepository.findAll();

        if (tuttiLink == null || tuttiLink.isEmpty()) {
            return null;
        }

        List<LinkCondivisoEntity> elencoUrl = new ArrayList<>();

        for (LinkCondivisoEntity link : tuttiLink) {
            if (link == null) {
                continue;
            }

            if (!Boolean.TRUE.equals(link.getIsAttivo())) {
                continue;
            }

            if (link.getPortfolio() == null || link.getPortfolio().getStudente() == null) {
                continue;
            }

            Long idStudentePortfolio = link.getPortfolio().getStudente().getId();

            if (studente_id.equals(idStudentePortfolio)) {
                elencoUrl.add(link);
            }
        }

        if (elencoUrl.isEmpty()) {
            return null;
        }

        return elencoUrl;
    }

    public void invalidaURL(Long id_url) {
        updateInvalidaURL(id_url);
    }

    public void updateInvalidaURL(Long id_url) {
        if (id_url == null) {
            return;
        }

        linkCondivisoRepository.findById(id_url).ifPresent(linkCondiviso -> {
            linkCondiviso.setIsAttivo(false);
            linkCondivisoRepository.save(linkCondiviso);
        });
    }

    @Transactional(readOnly = true)
    public List<StatisticaAccessoEntity> getDatiStatistici(Long studente_id) {
        return queryDatiStatistici(studente_id);
    }

    @Transactional(readOnly = true)
    public List<StatisticaAccessoEntity> queryDatiStatistici(Long studente_id) {
        if (studente_id == null) {
            return new ArrayList<>();
        }

        List<StatisticaAccessoEntity> tutteStatistiche = statisticaAccessoRepository.findAll();

        if (tutteStatistiche == null || tutteStatistiche.isEmpty()) {
            return new ArrayList<>();
        }

        List<StatisticaAccessoEntity> dati = new ArrayList<>();

        for (StatisticaAccessoEntity statistica : tutteStatistiche) {
            if (statistica == null || statistica.getLinkCondiviso() == null) {
                continue;
            }

            LinkCondivisoEntity linkCondiviso = statistica.getLinkCondiviso();

            if (linkCondiviso.getPortfolio() == null
                    || linkCondiviso.getPortfolio().getStudente() == null
                    || linkCondiviso.getPortfolio().getStudente().getId() == null) {
                continue;
            }

            Long idStudentePortfolio = linkCondiviso.getPortfolio().getStudente().getId();

            if (studente_id.equals(idStudentePortfolio)) {
                dati.add(statistica);
            }
        }

        return dati;
    }

    @Transactional(readOnly = true)
    public List<LicenzaEntity> recuperaLicenze() {
        return queryRecuperaLicenze();
    }

    @Transactional(readOnly = true)
    public List<LicenzaEntity> queryRecuperaLicenze() {
        List<LicenzaEntity> listaLicenze = licenzaRepository.findAll();

        if (listaLicenze == null || listaLicenze.isEmpty()) {
            return new ArrayList<>();
        }

        return listaLicenze;
    }

    @Transactional(readOnly = true)
    public LicenzaEntity recuperaLicenza(Long portfolio_id) {
        return queryRecuperaLicenza(portfolio_id);
    }

    @Transactional(readOnly = true)
    public LicenzaEntity queryRecuperaLicenza(Long portfolio_id) {
        if (portfolio_id == null) {
            return null;
        }

        PortfolioEntity portfolio = portfolioRepository.findById(portfolio_id).orElse(null);

        if (portfolio == null) {
            return null;
        }

        return portfolio.getLicenza();
    }

    public void salvaLicenzaPortfolio(Long portfolio_id, Long licenza_id) {
        querySalvaLicenzaPortfolio(portfolio_id, licenza_id);
    }

    public void querySalvaLicenzaPortfolio(Long portfolio_id, Long licenza_id) {
        if (portfolio_id == null || licenza_id == null) {
            return;
        }

        PortfolioEntity portfolio = portfolioRepository.findById(portfolio_id).orElse(null);
        LicenzaEntity licenza = licenzaRepository.findById(licenza_id).orElse(null);

        if (portfolio == null || licenza == null) {
            return;
        }

        portfolio.setLicenza(licenza);
        portfolioRepository.save(portfolio);
    }

    @Transactional(readOnly = true)
public List<SezioneEntity> recuperaImpostazioniVisibilita(Long portfolio_id) {
    return recuperaImpostazioniVisibilitaPortfolio(portfolio_id);
}

@Transactional(readOnly = true)
public List<SezioneEntity> recuperaImpostazioniVisibilitaPortfolio(Long portfolio_id) {
    if (portfolio_id == null) {
        return new ArrayList<>();
    }

    List<SezioneEntity> impostazioniVisibilita =
            sezioneRepository.findByPortfolioId(portfolio_id);

    if (impostazioniVisibilita == null || impostazioniVisibilita.isEmpty()) {
        return new ArrayList<>();
    }

    return impostazioniVisibilita;
}

public void aggiornaImpostazioni(Map<Long, Boolean> impostazioni) {
    updateImpostazioni(impostazioni);
}

public void updateImpostazioni(Map<Long, Boolean> impostazioni) {
    if (impostazioni == null || impostazioni.isEmpty()) {
        return;
    }

    for (Map.Entry<Long, Boolean> entry : impostazioni.entrySet()) {
        Long sezione_id = entry.getKey();
        Boolean nuovoStato = entry.getValue();

        if (sezione_id == null || nuovoStato == null) {
            continue;
        }

        sezioneRepository.findById(sezione_id).ifPresent(sezione -> {
            sezione.setIsPubblica(nuovoStato);
            sezioneRepository.save(sezione);
        });
    }
}

public String recuperaDescrizioneSezione(Long sezione_id) {
    return recuperaDescrizione(sezione_id);
}

public String recuperaDescrizione(Long sezione_id) {
    if (sezione_id == null) {
        return "";
    }

    return sezioneRepository.findById(sezione_id)
            .map(SezioneEntity::getCorpoTesto)
            .orElse("");
}

public void aggiornaDescrizione(Long sezione_id, String descrizione) {
    aggiornaDescrizioneSezione(sezione_id, descrizione);
}

public void aggiornaDescrizioneSezione(Long sezione_id, String descrizione) {
    if (sezione_id == null) {
        return;
    }

    sezioneRepository.findById(sezione_id).ifPresent(sezione -> {
        sezione.setCorpoTesto(descrizione);
        sezioneRepository.save(sezione);
    });
}

public void aggiornaOrdineSezioni(List<Long> nuovoOrdine, Long portfolio_id) {
    queryAggiornaOrdineSezioni(nuovoOrdine, portfolio_id);
}

public void queryAggiornaOrdineSezioni(List<Long> nuovoOrdine, Long idPortfolio) {
    if (nuovoOrdine == null || nuovoOrdine.isEmpty() || idPortfolio == null) {
        return;
    }

    for (int posizione = 0; posizione < nuovoOrdine.size(); posizione++) {
        Long sezione_id = nuovoOrdine.get(posizione);

        if (sezione_id == null) {
            continue;
        }

        SezioneEntity sezione = sezioneRepository.findById(sezione_id).orElse(null);

        if (sezione == null || sezione.getPortfolio() == null || sezione.getPortfolio().getId() == null) {
            continue;
        }

        if (!idPortfolio.equals(sezione.getPortfolio().getId())) {
            continue;
        }

        sezione.setOrdineVisualizzazione(posizione);
        sezioneRepository.save(sezione);
    }
}

public List<StudenteEntity> ricercaStudente(String ricerca) {
    return queryRicercaStudente(ricerca);
}

public List<StudenteEntity> queryRicercaStudente(String ricerca) {
    List<StudenteEntity> risultatiRicerca = new ArrayList<>();

    if (ricerca == null || ricerca.trim().isEmpty()) {
        return risultatiRicerca;
    }

    String filtro = ricerca.trim().toLowerCase();

    List<StudenteEntity> studenti = studenteRepository.findAll();

    if (studenti == null || studenti.isEmpty()) {
        return risultatiRicerca;
    }

    for (StudenteEntity studente : studenti) {
        if (studente == null) {
            continue;
        }

        String nome = studente.getNome() != null ? studente.getNome().toLowerCase() : "";
        String cognome = studente.getCognome() != null ? studente.getCognome().toLowerCase() : "";
        String corsoDiStudi = studente.getCorsoDiStudi() != null ? studente.getCorsoDiStudi().toLowerCase() : "";

        if (nome.contains(filtro)
                || cognome.contains(filtro)
                || corsoDiStudi.contains(filtro)
                || (nome + " " + cognome).contains(filtro)) {
            risultatiRicerca.add(studente);
        }
    }

    return risultatiRicerca;
}

public List<StudenteEntity> getElencoStudenti() {
    return queryGetElencoStudenti();
}

public List<StudenteEntity> queryGetElencoStudenti() {
    List<StudenteEntity> elencoStudenti = studenteRepository.findAll();

    if (elencoStudenti == null || elencoStudenti.isEmpty()) {
        return new ArrayList<>();
    }

    return elencoStudenti;
}

@Transactional(readOnly = true)
public PortfolioEntity recuperaPortfolioPubblico(Long portfolio_id) {
    return queryRecuperaPortfolioPubblico(portfolio_id);
}

@Transactional(readOnly = true)
public PortfolioEntity queryRecuperaPortfolioPubblico(Long portfolio_id) {
    if (portfolio_id == null) {
        return null;
    }

    PortfolioEntity portfolio = portfolioRepository.findById(portfolio_id).orElse(null);

    if (portfolio == null) {
        return null;
    }

    List<SezioneEntity> sezioniPortfolio = sezioneRepository.findByPortfolioId(portfolio_id);
    List<SezioneEntity> sezioniPubbliche = new ArrayList<>();

    if (sezioniPortfolio != null) {
        for (SezioneEntity sezione : sezioniPortfolio) {
            if (sezione != null && Boolean.TRUE.equals(sezione.getIsPubblica())) {
                sezioniPubbliche.add(sezione);
            }
        }
    }

    portfolio.setSezioni(sezioniPubbliche);

    return portfolio;
}

public boolean richiestaDBMS() {
    return queryRichiestaDBMS();
}

public boolean queryRichiestaDBMS() {
    return tentaRiconnessione();
}

public boolean tentaRiconnessione() {
    try {
        studenteRepository.count();
        return true;
    } catch (Exception e) {
        return false;
    }
}
}