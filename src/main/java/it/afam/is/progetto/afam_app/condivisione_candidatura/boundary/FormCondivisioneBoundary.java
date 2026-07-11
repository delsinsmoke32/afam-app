package it.afam.is.progetto.afam_app.condivisione_candidatura.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.condivisione_candidatura.controller.GenerazioneURLController;

public class FormCondivisioneBoundary extends JFrame {

    private final GenerazioneURLController generazioneURLController;

    private JTextField nomeField;

    public FormCondivisioneBoundary(GenerazioneURLController generazioneURLController) {
        this.generazioneURLController = generazioneURLController;
    }

    public void mostraForm() {
        setTitle("Condivisione Portfolio");
        setSize(450, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        nomeField = new JTextField();

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> cliccaOK());

        panel.add(new JLabel("Nome condivisione:"));
        panel.add(nomeField);
        panel.add(okButton);

        setContentPane(panel);
        setVisible(true);
    }

    public void inserisciNomeCondivisione(String nome) {
        nomeField.setText(nome);
    }

    public void cliccaOK() {
        String nome = nomeField.getText();

        // mandaNome(nome)
        mandaNome(nome);
    }

    public void mandaNome(String nome) {
        generazioneURLController.mandaNome(nome);
    }
}