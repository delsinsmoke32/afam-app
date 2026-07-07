package it.afam.is.progetto.afam_app.boundary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;

@Service
public class FileStorageBoundary {

    private static final String CARTELLA_UPLOAD = "uploads";

    public String caricaFile(String percorsoFile) {
        // uploadFile(percorsoFile)
        return uploadFile(percorsoFile);
    }

    public String uploadFile(String percorsoFile) {
        if (percorsoFile == null || percorsoFile.trim().isEmpty()) {
            return null;
        }

        try {
            Path sorgente = Path.of(percorsoFile);

            if (!Files.exists(sorgente) || !Files.isRegularFile(sorgente)) {
                return null;
            }

            Path cartellaUpload = Path.of(CARTELLA_UPLOAD);
            Files.createDirectories(cartellaUpload);

            Path destinazione = cartellaUpload.resolve(sorgente.getFileName());

            Files.copy(sorgente, destinazione, StandardCopyOption.REPLACE_EXISTING);

            return destinazione.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public void eliminaFile(String pathAllegato) {
        // deleteFile(pathAllegato)
        deleteFile(pathAllegato);
    }

    public void deleteFile(String pathAllegato) {
        if (pathAllegato == null || pathAllegato.trim().isEmpty()) {
            return;
        }

        try {
            Path path = Path.of(pathAllegato);

            if (Files.exists(path) && Files.isRegularFile(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            System.out.println("Errore eliminazione file: " + e.getMessage());
        }
    }
}