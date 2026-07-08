package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.gestioneaccount.control.FormAccessoController;

public class FormAccessoBoundary extends JFrame {

    private FormAccessoController formAccessoController;

    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField ruoloField;

    public void setFormAccessoController(FormAccessoController formAccessoController) {
        this.formAccessoController = formAccessoController;
    }

    public void mostraFormAccesso() {
        setTitle("Accesso Portfolio Condiviso");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        nomeField = new JTextField();
        cognomeField = new JTextField();
        ruoloField = new JTextField();

        JButton accediButton = new JButton("Accedi portfolio");
        accediButton.addActionListener(e -> accediPortfolio());

        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Cognome:"));
        panel.add(cognomeField);
        panel.add(new JLabel("Ruolo:"));
        panel.add(ruoloField);
        panel.add(accediButton);

        setContentPane(panel);
        setVisible(true);
    }

    public void inserisciDati(String nome, String cognome, String ruolo) {
        nomeField.setText(nome);
        cognomeField.setText(cognome);
        ruoloField.setText(ruolo);
    }

    public void accediPortfolio() {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String ruolo = ruoloField.getText();

        // mandaCampi(nome, cognome, ruolo)
        formAccessoController.mandaCampi(nome, cognome, ruolo);
    }
}