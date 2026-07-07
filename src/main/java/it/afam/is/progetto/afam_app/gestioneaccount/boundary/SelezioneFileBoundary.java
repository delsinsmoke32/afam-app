package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.gestioneaccount.control.GestioneAllegatiController;

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