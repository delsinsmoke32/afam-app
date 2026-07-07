package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.gestioneaccount.control.CancProfController;

public class PopupConfermaBoundary extends JFrame {

    private CancProfController cancProfController;

    private Runnable azioneConferma;
    private Runnable azioneAnnulla;

    private boolean soloOK = false;

    public PopupConfermaBoundary(CancProfController cancProfController) {
        this.cancProfController = cancProfController;
    }

    public PopupConfermaBoundary(Runnable azioneConferma, Runnable azioneAnnulla) {
        this.azioneConferma = azioneConferma;
        this.azioneAnnulla = azioneAnnulla;
    }

    public PopupConfermaBoundary(Runnable azioneOK) {
        this.azioneConferma = azioneOK;
        this.soloOK = true;
    }

    public void mostraPopup(String testo) {
        setTitle("Conferma");
        setSize(420, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        JLabel messaggio = new JLabel(testo);

        JButton confermaButton = new JButton(soloOK ? "OK" : "Conferma");
        confermaButton.addActionListener(e -> cliccaConferma());

        panel.add(messaggio);
        panel.add(confermaButton);

        if (!soloOK) {
            JButton annullaButton = new JButton("Annulla");
            annullaButton.addActionListener(e -> cliccaAnnulla());
            panel.add(annullaButton);
        }

        setContentPane(panel);
        setVisible(true);
    }

    public void cliccaConferma() {
        if (cancProfController != null) {
            cancProfController.conferma();
            return;
        }

        if (azioneConferma != null) {
            azioneConferma.run();
            return;
        }

        dispose();
    }

    public void cliccaAnnulla() {
        if (cancProfController != null) {
            cancProfController.annulla();
            return;
        }

        if (azioneAnnulla != null) {
            azioneAnnulla.run();
            return;
        }

        dispose();
    }
}