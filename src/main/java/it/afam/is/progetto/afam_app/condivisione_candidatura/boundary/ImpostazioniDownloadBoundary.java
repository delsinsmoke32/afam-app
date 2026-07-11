package it.afam.is.progetto.afam_app.condivisione_candidatura.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.condivisione_candidatura.controller.ImpostazioniDownloadController;

public class ImpostazioniDownloadBoundary extends JFrame {

    private final ImpostazioniDownloadController impostazioniDownloadController;

    private JCheckBox downloadCheckBox;

    public ImpostazioniDownloadBoundary(ImpostazioniDownloadController impostazioniDownloadController) {
        this.impostazioniDownloadController = impostazioniDownloadController;
    }

    public void mostraImpostazioneCorrente(Boolean impostazione) {
        setTitle("Impostazioni Download");
        setSize(450, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        downloadCheckBox = new JCheckBox(
                "Permetti download dei file del portfolio condiviso",
                Boolean.TRUE.equals(impostazione)
        );

        JButton salvaButton = new JButton("Salva");
        salvaButton.addActionListener(e -> cliccaSalva());

        panel.add(new JLabel("Impostazione download corrente:"));
        panel.add(downloadCheckBox);
        panel.add(salvaButton);

        setContentPane(panel);
        setVisible(true);
    }

    public void impostaDownload(Boolean impostazione) {
        if (downloadCheckBox != null) {
            downloadCheckBox.setSelected(Boolean.TRUE.equals(impostazione));
        }
    }

    public void cliccaSalva() {
        Boolean impostazione = downloadCheckBox.isSelected();

        // mandaImpostazione(impostazione)
        mandaImpostazione(impostazione);
    }

    public void mandaImpostazione(Boolean impostazione) {
        impostazioniDownloadController.mandaImpostazione(impostazione);
    }
}