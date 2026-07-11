package it.afam.is.progetto.afam_app.gestione_portfolio.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.gestione_portfolio.controller.CreazionePortfolioController;

public class FormCreazionePortfolioBoundary extends JFrame {

    private final CreazionePortfolioController creazionePortfolioController;

    private JTextField titoloField;
    private JTextArea descrizioneArea;

    public FormCreazionePortfolioBoundary(CreazionePortfolioController creazionePortfolioController) {
        this.creazionePortfolioController = creazionePortfolioController;
    }

    public void mostraForm() {
        setTitle("Creazione PortfolioEntity");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel titoloLabel = new JLabel("Titolo:");
        titoloField = new JTextField();

        JLabel descrizioneLabel = new JLabel("Descrizione:");
        descrizioneArea = new JTextArea();

        JButton confermaButton = new JButton("Conferma");
        confermaButton.addActionListener(e -> cliccaConferma());

        panel.add(titoloLabel);
        panel.add(titoloField);
        panel.add(descrizioneLabel);
        panel.add(descrizioneArea);
        panel.add(new JLabel());
        panel.add(confermaButton);

        setContentPane(panel);
        setVisible(true);
    }

    public void inserisciDatiPortfolio(String titolo, String descrizione) {
        titoloField.setText(titolo);
        descrizioneArea.setText(descrizione);
    }

    public void cliccaConferma() {
        String titolo = titoloField.getText();
        String descrizione = descrizioneArea.getText();

        // mandaDati(titolo, descrizione)
        creazionePortfolioController.mandaDati(titolo, descrizione);
    }
}

