package it.afam.is.progetto.afam_app.gestione_sezione.boundary;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.FileStorageBoundary;
import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.CancellazioneFileController;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.GestioneAllegatiController;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.ModificaDescrizioneSezController;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.ModificaMetadatiController;

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
        setSize(700, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Visualizza Sezione"));

        String descrizione = dbmsBoundary.recuperaDescrizioneSezione(sezione_id);
        panel.add(new JLabel("Descrizione: " + (descrizione != null ? descrizione : "")));

        JButton modificaDescrizioneButton = new JButton("Modifica descrizione");
        modificaDescrizioneButton.addActionListener(e -> cliccaModificaDescrizione());

        JButton uploadButton = new JButton("Upload file");
        uploadButton.addActionListener(e -> cliccaUploadFile());

        panel.add(modificaDescrizioneButton);
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
                eliminaButton.addActionListener(e -> cliccaEliminaFile(allegato.getId()));

                panel.add(allegatoLabel);
                panel.add(modificaButton);
                panel.add(eliminaButton);
            }
        }

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void mostraSezione() {
        List<AllegatoEntity> listaAllegati = dbmsBoundary.recuperaAllegati(sezione_id);
        mostraSezioneInit(listaAllegati);
    }

    public void cliccaUploadFile() {
        // <<create>> GestioneAllegatiController
        GestioneAllegatiController gestioneAllegatiController =
                new GestioneAllegatiController(this, dbmsBoundary, fileStorageBoundary);

        // richiediUpload(sezione_id)
        gestioneAllegatiController.richiediUpload(sezione_id);
    }

    public void cliccaModificaDescrizione() {
        // <<create>> ModificaDescrizioneController
        ModificaDescrizioneSezController modificaDescrizioneSezController =
                new ModificaDescrizioneSezController(this, dbmsBoundary);

        // richiediModificaDescrizione(sezione_id)
        modificaDescrizioneSezController.richiediModificaDescrizione(sezione_id);
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