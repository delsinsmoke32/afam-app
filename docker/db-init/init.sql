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
    password VARCHAR(150) NULL,              -- NULL se registrato con SPID
    provider_autenticazione VARCHAR(20) DEFAULT 'LOCAL', -- Flag per capire l'origine dell'account
    
    -- Dati di Profilo (Accademici e Pubblici)
    corso_di_studi VARCHAR(150),
    data_di_nascita DATE,
    biografia TEXT,                          -- Per il profilo pubblico
    link_personale VARCHAR(255)              -- Es. Instagram, LinkedIn, ArtStation
);

-- 8. Tabella licenze
CREATE TABLE IF NOT EXISTS licenze (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descrizione TEXT NOT NULL
)

-- 2. Tabella Portfoli
CREATE TABLE IF NOT EXISTS portfoli (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    studente_id BIGINT NOT NULL,
    titolo VARCHAR(200) NOT NULL,
    descrizione TEXT,
    download_abilitato BOOLEAN DEFAULT TRUE, -- Caso d'uso IMDL (Impostazioni Download)
    visite_totali INT DEFAULT 0,             -- Contatore globale visite
    ref_licenza INT,                         -- Riferimento alla licenza
    FOREIGN KEY (studente_id) REFERENCES studenti(id) ON DELETE CASCADE,
    FOREIGN KEY (ref_licenza) REFERENCES licenze(id) ON DELETE SET NULL
);

-- 3. Tabella Sezioni
CREATE TABLE IF NOT EXISTS sezioni (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    portfolio_id BIGINT NOT NULL,
    titolo VARCHAR(200) NOT NULL,
    corpo_testo TEXT,
    is_pubblica BOOLEAN DEFAULT TRUE,        -- Filtro per il Visitatore Esterno (VISPORTP)
    ordine_visualizzazione INT DEFAULT 0,
    FOREIGN KEY (portfolio_id) REFERENCES portfoli(id) ON DELETE CASCADE
);

-- 4. Tabella Allegati (Aggiornata con metadati)
CREATE TABLE IF NOT EXISTS allegati (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sezione_id BIGINT NOT NULL,
    nome_file VARCHAR(255) NOT NULL,        -- Nome tecnico del file (es: img_001.jpg)
    titolo_opera VARCHAR(200) NOT NULL,     -- Titolo mostrato all'utente
    descrizione_breve TEXT,                 -- Breve info sull'opera
    autore_opera VARCHAR(250),              -- Autore (default allo studente)
    tipo_file VARCHAR(50),                  -- MIME type (es: 'image/jpeg', 'application/pdf')
    percorso_risorsa VARCHAR(500) NOT NULL, -- Path su storage
    data_creazione DATE,                    -- Per ordinamento cronologico nel portfolio
    FOREIGN KEY (sezione_id) REFERENCES sezioni(id) ON DELETE CASCADE
);

-- 5. Tabella Link Condivisi (Candidature e URL Univoci)
CREATE TABLE IF NOT EXISTS link_condivisi (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    portfolio_id BIGINT NOT NULL,
    nome_riferimento VARCHAR(100),           -- es. "Candidatura per Azienda X"
    token_url VARCHAR(255) UNIQUE NOT NULL,  -- La stringa alfanumerica dell'URL
    is_attivo BOOLEAN DEFAULT TRUE,          -- Caso d'uso RECURL (Revoca URL)
    data_scadenza DATETIME,
    visite_link INT DEFAULT 0,               -- Quante volte è stato aperto QUESTO link
    FOREIGN KEY (portfolio_id) REFERENCES portfoli(id) ON DELETE CASCADE
);

-- 6. Tabella Statistiche Accesso (Caso d'uso RSA)
-- 6. Tabella Statistiche Accesso (Caso d'uso RSA)
CREATE TABLE IF NOT EXISTS statistiche_accessi (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    link_condiviso_id BIGINT NOT NULL,
    data_ora_accesso DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    -- Nuovi campi aggiunti per soddisfare il Caso d'uso RSA
    nome_visitatore VARCHAR(100),            -- Nome (se fornito o se loggato)
    cognome_visitatore VARCHAR(100),         -- Cognome
    ruolo_visitatore VARCHAR(50) DEFAULT 'Anonimo', -- Es: 'Docente', 'Azienda', 'Ospite', 'Studente'
    
    FOREIGN KEY (link_condiviso_id) REFERENCES link_condivisi(id) ON DELETE CASCADE
);

-- 7. Tabella Codici OTP (Caso d'uso Login2FA)
CREATE TABLE IF NOT EXISTS codici_otp (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    studente_id BIGINT NOT NULL,
    codice VARCHAR(6) NOT NULL,
    scadenza DATETIME NOT NULL,              -- Calcolata come istante di generazione + 3 minuti
    FOREIGN KEY (studente_id) REFERENCES studenti(id) ON DELETE CASCADE
);

-- 9. Tabella di Override Visibilità per Link Condiviso
CREATE TABLE IF NOT EXISTS visibilita_link_sezioni (
    link_condiviso_id BIGINT NOT NULL,
    sezione_id BIGINT NOT NULL,
    is_visibile BOOLEAN NOT NULL DEFAULT TRUE, -- Stato personalizzato per questo link
    
    PRIMARY KEY (link_condiviso_id, sezione_id),
    FOREIGN KEY (link_condiviso_id) REFERENCES link_condivisi(id) ON DELETE CASCADE,
    FOREIGN KEY (sezione_id) REFERENCES sezioni(id) ON DELETE CASCADE
);
