package it.afam.is.progetto.afam_app.gestione_sezione.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.FileStorageBoundary;
import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestione_sezione.boundary.FormMetadatiFileBoundary;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestione_sezione.boundary.SelezioneFileBoundary;
import it.afam.is.progetto.afam_app.gestione_sezione.boundary.VisualizzaSezioneBoundary;

public class GestioneAllegatiController {

    private final VisualizzaSezioneBoundary visualizzaSezioneBoundary;
    private final DBMSBoundary dbmsBoundary;
    private final FileStorageBoundary fileStorageBoundary;

    private SelezioneFileBoundary selezioneFileBoundary;
    private FormMetadatiFileBoundary formMetadatiFileBoundary;

    private Long sezione_id;
    private String percorsoFileOriginale;
    private String percorsoFileCaricato;

    public GestioneAllegatiController(
            VisualizzaSezioneBoundary visualizzaSezioneBoundary,
            DBMSBoundary dbmsBoundary,
            FileStorageBoundary fileStorageBoundary
    ) {
        this.visualizzaSezioneBoundary = visualizzaSezioneBoundary;
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = fileStorageBoundary;
    }

    public void richiediUpload(Long sezione_id) {
        this.sezione_id = sezione_id;

        // <<create>> SelezioneFileBoundary
        selezioneFileBoundary = new SelezioneFileBoundary(this);

        // mostraSelezioneFile()
        selezioneFileBoundary.mostraSelezioneFile();
    }

    public void mandaFile(String percorsoFile) {
        this.percorsoFileOriginale = percorsoFile;

        // controllaFile(percorsoFile)
        boolean controllaFile = controllaFile(percorsoFile);

        // alt [controllaFile = true]
        if (controllaFile) {
            // caricaFile(percorsoFile)
            percorsoFileCaricato = fileStorageBoundary.caricaFile(percorsoFile);

            if (percorsoFileCaricato == null) {
                mostraErroreUpload();
                return;
            }

            // <<create>> FormMetadatiFileBoundary
            formMetadatiFileBoundary = new FormMetadatiFileBoundary(this);

            // mostraForm()
            formMetadatiFileBoundary.mostraForm();
        } else {
            // [else]
            mostraErroreUpload();
        }
    }

    public boolean controllaFile(String percorsoFile) {
        if (percorsoFile == null || percorsoFile.trim().isEmpty()) {
            return false;
        }

        Path path = Path.of(percorsoFile);

        // Verifichiamo prima di tutto che il file esista e sia un file regolare
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            return false;
        }

        // Estraiamo l'estensione convertendola in minuscolo
        String nomeFile = path.getFileName().toString().toLowerCase();

        // Controlliamo se finisce con una delle estensioni consentite
        return nomeFile.endsWith(".png")  ||
                nomeFile.endsWith(".jpg")  ||
                nomeFile.endsWith(".jpeg") ||
                nomeFile.endsWith(".mp3")  ||
                nomeFile.endsWith(".mp4")  ||
                nomeFile.endsWith(".pdf");
    }

    public void mandaDati(String titolo, String autori, String descrizione, LocalDate dataCreazione) {
        SezioneEntity sezione = dbmsBoundary.cercaSezione(sezione_id);

        if (sezione == null) {
            mostraErroreUpload();
            return;
        }

        String nomeFile = estraiNomeFile(percorsoFileOriginale);
        String tipoFile = estraiTipoFile(nomeFile);

        // <<create>> AllegatoEntity
        AllegatoEntity allegato = AllegatoEntity.builder()
                .nomeFile(nomeFile)
                .titoloOpera(titolo)
                .autoreOpera(autori)
                .descrizioneBreve(descrizione)
                .dataCreazione(dataCreazione)
                .tipoFile(tipoFile)
                .percorsoRisorsa(percorsoFileCaricato)
                .sezione(sezione)
                .build();

        // salvaFile(Allegato)
        dbmsBoundary.salvaFile(allegato);

        // mostraSezioneAggiungiAllegato(Allegato)
        visualizzaSezioneBoundary.mostraSezioneAggiungiAllegato(allegato);
    }

    private void mostraErroreUpload() {
        PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();

        // mostraPopup(testo)
        popupErroreBoundary.mostraPopup("File non valido o upload non riuscito.");

        // cliccaOK() gestito dal popup

        // mostraSezionePrecedente()
        visualizzaSezioneBoundary.mostraSezionePrecedente();
    }

    private String estraiNomeFile(String percorsoFile) {
        if (percorsoFile == null) {
            return "file";
        }

        return Path.of(percorsoFile).getFileName().toString();
    }

    private String estraiTipoFile(String nomeFile) {
        if (nomeFile == null || !nomeFile.contains(".")) {
            return "sconosciuto";
        }

        return nomeFile.substring(nomeFile.lastIndexOf(".") + 1);
    }
}