-- Creazione del database (se non esiste)
CREATE DATABASE IF NOT EXISTS afam_db;
USE afam_db;

CREATE TABLE IF NOT EXISTS studenti (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        codice_fiscale VARCHAR(16) UNIQUE NOT NULL,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,

    -- Autenticazione Mista
    password VARCHAR(150) NULL,
    provider_autenticazione VARCHAR(20) DEFAULT 'LOCAL',

    -- Dati di Profilo
    corso_di_studi VARCHAR(150),
    data_di_nascita DATE,
    biografia TEXT,
    link_personale VARCHAR(255)
    );

-- Tabella licenze
CREATE TABLE IF NOT EXISTS licenze (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       nome VARCHAR(255) NOT NULL,
    descrizione TEXT NOT NULL
    );

-- Licenze di default
INSERT INTO licenze (nome, descrizione)
SELECT 'Tutti i diritti riservati', 'Nessun utilizzo consentito senza autorizzazione.'
    WHERE NOT EXISTS (SELECT 1 FROM licenze WHERE nome = 'Tutti i diritti riservati');

INSERT INTO licenze (nome, descrizione)
SELECT 'CC BY', 'Utilizzo consentito con attribuzione dell’autore.'
    WHERE NOT EXISTS (SELECT 1 FROM licenze WHERE nome = 'CC BY');

INSERT INTO licenze (nome, descrizione)
SELECT 'CC BY-SA', 'Utilizzo consentito con attribuzione e stessa licenza.'
    WHERE NOT EXISTS (SELECT 1 FROM licenze WHERE nome = 'CC BY-SA');

INSERT INTO licenze (nome, descrizione)
SELECT 'CC BY-NC', 'Utilizzo consentito con attribuzione, solo per scopi non commerciali.'
    WHERE NOT EXISTS (SELECT 1 FROM licenze WHERE nome = 'CC BY-NC');

-- Tabella Portfoli
CREATE TABLE IF NOT EXISTS portfoli (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        studente_id BIGINT NOT NULL,
                                        titolo VARCHAR(200) NOT NULL,
    descrizione TEXT,
    download_abilitato BOOLEAN DEFAULT TRUE,
    visite_totali INT DEFAULT 0,
    licenza_id BIGINT,
    FOREIGN KEY (studente_id) REFERENCES studenti(id) ON DELETE CASCADE,
    FOREIGN KEY (licenza_id) REFERENCES licenze(id) ON DELETE SET NULL
    );

-- Tabella Sezioni
CREATE TABLE IF NOT EXISTS sezioni (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       portfolio_id BIGINT NOT NULL,
                                       titolo VARCHAR(200) NOT NULL,
    corpo_testo TEXT,
    is_pubblica BOOLEAN DEFAULT TRUE,
    ordine_visualizzazione INT DEFAULT 0,
    FOREIGN KEY (portfolio_id) REFERENCES portfoli(id) ON DELETE CASCADE
    );

-- Tabella Allegati
CREATE TABLE IF NOT EXISTS allegati (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        sezione_id BIGINT NOT NULL,
                                        nome_file VARCHAR(255) NOT NULL,
    titolo_opera VARCHAR(200) NOT NULL,
    descrizione_breve TEXT,
    autore_opera VARCHAR(250),
    tipo_file VARCHAR(50),
    percorso_risorsa VARCHAR(500) NOT NULL,
    data_creazione DATE,
    FOREIGN KEY (sezione_id) REFERENCES sezioni(id) ON DELETE CASCADE
    );

-- Tabella Link Condivisi
CREATE TABLE IF NOT EXISTS link_condivisi (
                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              portfolio_id BIGINT NOT NULL,
                                              nome_riferimento VARCHAR(100),
    token_url VARCHAR(255) UNIQUE NOT NULL,
    is_attivo BOOLEAN DEFAULT TRUE,
    data_scadenza DATETIME,
    visite_link INT DEFAULT 0,
    FOREIGN KEY (portfolio_id) REFERENCES portfoli(id) ON DELETE CASCADE
    );

-- Tabella Statistiche Accesso
CREATE TABLE IF NOT EXISTS statistiche_accessi (
                                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                   link_condiviso_id BIGINT NOT NULL,
                                                   data_ora_accesso DATETIME DEFAULT CURRENT_TIMESTAMP,

                                                   nome_visitatore VARCHAR(100),
    cognome_visitatore VARCHAR(100),
    ruolo_visitatore VARCHAR(50) DEFAULT 'Anonimo',

    FOREIGN KEY (link_condiviso_id) REFERENCES link_condivisi(id) ON DELETE CASCADE
    );

-- Tabella Codici OTP
CREATE TABLE IF NOT EXISTS codici_otp (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          studente_id BIGINT NOT NULL,
                                          codice VARCHAR(6) NOT NULL,
    scadenza DATETIME NOT NULL,
    FOREIGN KEY (studente_id) REFERENCES studenti(id) ON DELETE CASCADE
    );

-- Tabella di Override Visibilità per Link Condiviso
CREATE TABLE IF NOT EXISTS visibilita_sezioni_candidatura (
                                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                       link_condiviso_id BIGINT NOT NULL,
                                                       sezione_id BIGINT NOT NULL,
                                                       is_visibile BOOLEAN NOT NULL DEFAULT TRUE,
                                                       UNIQUE(link_condiviso_id, sezione_id),
    FOREIGN KEY (link_condiviso_id) REFERENCES link_condivisi(id) ON DELETE CASCADE,
    FOREIGN KEY (sezione_id) REFERENCES sezioni(id) ON DELETE CASCADE
    );

-- =================================================================================
-- INSERIMENTO DATI MOCK PER TESTING E DEMO
-- Nota: Usiamo INSERT IGNORE così se riavvii il container non dà errore sui duplicati
-- =================================================================================

-- 1. Inserimento Studenti
-- La password qui è impostata a 'password123' in chiaro. Se usi BCrypt nell'app, dovresti sostituirla con l'hash generato!
INSERT IGNORE INTO studenti (id, codice_fiscale, nome, cognome, email, password, provider_autenticazione, corso_di_studi, data_di_nascita, biografia, link_personale) VALUES
(1, 'RSSMRA80A01H501Z', 'Mario', 'Rossi', 'mario.rossi@studenti.afam.it', 'password123', 'LOCAL', 'Graphic Design', '1998-05-14', 'Appassionato di illustrazione digitale e grafica editoriale.', 'https://mariorossidesign.it'),
(2, 'BNCGLI90B45F205X', 'Giulia', 'Bianchi', 'giulia.bianchi@studenti.afam.it', 'password123', 'LOCAL', 'Pianoforte', '2001-08-22', 'Pianista classica, mi interesso anche alla composizione contemporanea.', 'https://giuliabianchimusic.com');

-- 2. Inserimento Portfoli (ID 2 = CC BY dalle licenze di default)
INSERT IGNORE INTO portfoli (id, studente_id, titolo, descrizione, download_abilitato, visite_totali, licenza_id) VALUES
(1, 1, 'Progetti Grafici 2024', 'Raccolta dei miei lavori principali tra locandine e branding.', TRUE, 15, 2),
(2, 2, 'Repertorio Musicale e Saggi', 'Registrazioni audio dei saggi di fine anno e concerti dal vivo.', FALSE, 5, 1);

-- 3. Inserimento Sezioni
INSERT IGNORE INTO sezioni (id, portfolio_id, titolo, corpo_testo, is_pubblica, ordine_visualizzazione) VALUES
(1, 1, 'Locandine e Poster', 'Progetti realizzati per eventi teatrali e musicali locali.', TRUE, 1),
(2, 1, 'Identità Visiva', 'Costruzione di loghi e manuali di brand identity.', TRUE, 2),
(3, 2, 'Esibizioni Live', 'Tracce registrate al conservatorio.', TRUE, 1);

-- 4. Inserimento Allegati
-- IMPORTANTE: I path in 'percorso_risorsa' dovrebbero esistere per permettere l'anteprima/download.
-- Per testare l'anteprima immagini metti dei veri file .jpg o .png in questi percorsi nel tuo PC/Container!
INSERT IGNORE INTO allegati (id, sezione_id, nome_file, titolo_opera, descrizione_breve, autore_opera, tipo_file, percorso_risorsa, data_creazione) VALUES
(1, 1, 'Locandina_Teatro.pdf', 'Amleto - Teatro Massimo', 'Locandina per la stagione 2023', 'Mario Rossi', 'application/pdf', 'uploads/locandina_teatro.pdf', '2023-10-01'),
(2, 1, 'Evento_Musicale.jpg', 'Jazz Festival', 'Poster in formato A3', 'Mario Rossi', 'image/jpeg', 'uploads/evento_musicale.jpg', '2024-01-15'),
(3, 2, 'Logo_Aziendale.png', 'Logo per StartUp', 'Versione vettoriale rasterizzata', 'Mario Rossi', 'image/png', 'uploads/logo_aziendale.png', '2024-02-20'),
(4, 3, 'Sonata_Beethoven.mp3', 'Sonata al Chiaro di Luna', 'Beethoven, esecuzione live', 'Giulia Bianchi', 'audio/mpeg', 'uploads/sonata.mp3', '2023-12-20');

-- 5. Inserimento Link Condivisi (Per testare la parte di condivisione)
INSERT IGNORE INTO link_condivisi (id, portfolio_id, nome_riferimento, token_url, is_attivo, data_scadenza, visite_link) VALUES
(1, 1, 'Condivisione per Docente Tesi', '550e8400-e29b-41d4-a716-446655440000', TRUE, '2026-12-31 23:59:59', 2);

-- 6. Inserimento Statistiche Accessi (Simula chi ha visitato il link)
INSERT IGNORE INTO statistiche_accessi (id, link_condiviso_id, data_ora_accesso, nome_visitatore, cognome_visitatore, ruolo_visitatore) VALUES
(1, 1, '2024-05-10 14:30:00', 'Giuseppe', 'Verdi', 'Docente');

-- 7. Visibilità Link Sezioni (Gestisce cosa vede il prof. Verdi nel link condiviso)
INSERT IGNORE INTO visibilita_sezioni_candidatura (id, link_condiviso_id, sezione_id, is_visibile) VALUES
(1, 1, 1, TRUE),
(2, 1, 2, FALSE); -- Ad esempio, in questo link la sezione 'Identità Visiva' viene nascosta