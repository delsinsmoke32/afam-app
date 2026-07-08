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

-- Se la tabella portfoli esisteva già con ref_licenza, questa parte serve per sistemare DB vecchi.
-- Eseguila manualmente solo se ti serve.
-- ALTER TABLE portfoli ADD COLUMN licenza_id BIGINT;
-- ALTER TABLE portfoli ADD CONSTRAINT fk_portfoli_licenza
-- FOREIGN KEY (licenza_id) REFERENCES licenze(id) ON DELETE SET NULL;

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
CREATE TABLE IF NOT EXISTS visibilita_link_sezioni (
    link_condiviso_id BIGINT NOT NULL,
    sezione_id BIGINT NOT NULL,
    is_visibile BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (link_condiviso_id, sezione_id),
    FOREIGN KEY (link_condiviso_id) REFERENCES link_condivisi(id) ON DELETE CASCADE,
    FOREIGN KEY (sezione_id) REFERENCES sezioni(id) ON DELETE CASCADE
);