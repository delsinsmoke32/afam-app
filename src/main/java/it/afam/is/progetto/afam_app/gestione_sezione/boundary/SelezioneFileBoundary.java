package it.afam.is.progetto.afam_app.gestione_sezione.boundary;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.gestione_sezione.controller.GestioneAllegatiController;

public class SelezioneFileBoundary extends JFrame {

    private final GestioneAllegatiController gestioneAllegatiController;

    public SelezioneFileBoundary(GestioneAllegatiController gestioneAllegatiController) {
        this.gestioneAllegatiController = gestioneAllegatiController;
    }

    public void mostraSelezioneFile() {
        setTitle("Selezione File");
        setSize(400, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();

        JLabel titolo = new JLabel("Seleziona un file da caricare");
        JButton sfogliaButton = new JButton("Sfoglia");

        sfogliaButton.addActionListener(e -> premiSfoglia());

        panel.add(titolo);
        panel.add(sfogliaButton);

        setContentPane(panel);
        setVisible(true);
    }

    public void premiSfoglia() {
        JFileChooser fileChooser = new JFileChooser();

        // <<create>> FileNameExtensionFilter per limitare la scelta visiva dell'utente
        javax.swing.filechooser.FileNameExtensionFilter filtro =
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "File Multimediali Consentiti (.png, .jpg, .mp3, .mp4, .pdf)",
                        "png", "jpg", "jpeg", "mp3", "mp4", "pdf"
                );

        // Impostiamo il filtro nel selettore e disabilitiamo l'opzione "Tutti i file (*.*)"
        fileChooser.setFileFilter(filtro);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int risultato = fileChooser.showOpenDialog(this);

        if (risultato == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // selezionaFile(percorsoFile)
            selezionaFile(file.getAbsolutePath());
        }
    }

    public void selezionaFile(String percorsoFile) {
        // mandaFile(percorsoFile)
        mandaFile(percorsoFile);
    }

    public void mandaFile(String percorsoFile) {
        gestioneAllegatiController.mandaFile(percorsoFile);
        dispose();
    }
}