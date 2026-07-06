package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.gestioneaccount.control.CancProfController;

public class PopupConfermaBoundary extends JFrame {

    private final CancProfController cancProfController;

    public PopupConfermaBoundary(CancProfController cancProfController) {
        this.cancProfController = cancProfController;
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
        cancProfController.conferma();
    }

    public void cliccaAnnulla() {
        // annulla()
        cancProfController.annulla();
    }
}