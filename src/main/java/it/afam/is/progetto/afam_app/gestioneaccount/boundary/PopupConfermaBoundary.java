package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.gestioneaccount.control.CancProfController;

public class PopupConfermaBoundary extends JFrame {

    private CancProfController cancProfController;

    private Runnable azioneConferma;
    private Runnable azioneAnnulla;

    public PopupConfermaBoundary(CancProfController cancProfController) {
        this.cancProfController = cancProfController;
    }

    public PopupConfermaBoundary(Runnable azioneConferma, Runnable azioneAnnulla) {
        this.azioneConferma = azioneConferma;
        this.azioneAnnulla = azioneAnnulla;
    }

    public void mostraPopup(String testo) {
        setTitle("Conferma");
        setSize(420, 160);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel(testo, JLabel.CENTER);

        JButton confermaButton = new JButton("Conferma");
        JButton annullaButton = new JButton("Annulla");

        JPanel panelBottoni = new JPanel();
        panelBottoni.add(confermaButton);
        panelBottoni.add(annullaButton);

        confermaButton.addActionListener(e -> cliccaConferma());
        annullaButton.addActionListener(e -> cliccaAnnulla());

        add(label, BorderLayout.CENTER);
        add(panelBottoni, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void cliccaConferma() {
        // conferma()
        if (cancProfController != null) {
            cancProfController.conferma();
            return;
        }

        if (azioneConferma != null) {
            azioneConferma.run();
        }
    }

    public void cliccaAnnulla() {
        // annulla()
        if (cancProfController != null) {
            cancProfController.annulla();
            return;
        }

        if (azioneAnnulla != null) {
            azioneAnnulla.run();
        } else {
            dispose();
        }
    }
}