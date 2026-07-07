package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.boundary.FileStorageBoundary;
import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.control.CancellazioneFileController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.GestioneAllegatiController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.ModificaMetadatiController;

public class VisualizzaSezioneBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;
    private final FileStorageBoundary fileStorageBoundary;
    private final Long sezione_id;

    public VisualizzaSezioneBoundary(
            DBMSBoundary dbmsBoundary,
            FileStorageBoundary fileStorageBoundary,
            Long sezione_id
    ) {
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = fileStorageBoundary;
        this.sezione_id = sezione_id;
    }

    public void mostraSezioneInit(List<AllegatoEntity> listaAllegati) {
        setTitle("Visualizza Sezione");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Visualizza Sezione"));

        JButton uploadButton = new JButton("Upload file");
        uploadButton.addActionListener(e -> cliccaUploadFile());

        panel.add(uploadButton);

        panel.add(new JLabel("Allegati:"));

        if (listaAllegati == null || listaAllegati.isEmpty()) {
            panel.add(new JLabel("Nessun allegato presente."));
        } else {
            for (AllegatoEntity allegato : listaAllegati) {
                JLabel allegatoLabel = new JLabel(
                        "- " + allegato.getTitoloOpera() + " (" + allegato.getNomeFile() + ")"
                );

                JButton modificaButton = new JButton("Modifica metadati");
                modificaButton.addActionListener(e -> cliccaModificaMetadati(allegato.getId()));

                JButton eliminaButton = new JButton("Elimina file");

                // cliccaEliminaFile()
                eliminaButton.addActionListener(e -> cliccaEliminaFile(allegato.getId()));

                panel.add(allegatoLabel);
                panel.add(modificaButton);
                panel.add(eliminaButton);
            }
        }

        setContentPane(panel);
        setVisible(true);
    }

    public void mostraSezione() {
        setTitle("Visualizza Sezione");
        setSize(450, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();

        JLabel titolo = new JLabel("Visualizza Sezione");
        JButton uploadButton = new JButton("Upload file");

        uploadButton.addActionListener(e -> cliccaUploadFile());

        panel.add(titolo);
        panel.add(uploadButton);

        setContentPane(panel);
        setVisible(true);
    }

    public void cliccaUploadFile() {
        // <<create>> GestioneAllegatiController
        GestioneAllegatiController gestioneAllegatiController =
                new GestioneAllegatiController(this, dbmsBoundary, fileStorageBoundary);

        // richiediUpload(sezione_id)
        gestioneAllegatiController.richiediUpload(sezione_id);
    }

    public void cliccaModificaMetadati(Long allegato_id) {
        // <<create>> ModificaMetadatiController
        ModificaMetadatiController modificaMetadatiController =
                new ModificaMetadatiController(this, dbmsBoundary);

        // richiediModificaMetadati(allegato_id)
        modificaMetadatiController.richiediModificaMetadati(allegato_id);
    }

    public void cliccaEliminaFile(Long allegato_id) {
        // <<create>> CancellazioneFileController
        CancellazioneFileController cancellazioneFileController =
                new CancellazioneFileController(this, dbmsBoundary, fileStorageBoundary);

        // richiediCancellazioneFile(allegato_id)
        cancellazioneFileController.richiediCancellazioneFile(allegato_id);
    }

    public void mostraSezioneAggiungiAllegato(AllegatoEntity allegato) {
        mostraSezione();
    }

    public void mostraSezioneRimuoviAllegato(Long allegato_id) {
        mostraSezione();
    }

    public void mostraSezionePrecedente() {
        mostraSezione();
    }
}