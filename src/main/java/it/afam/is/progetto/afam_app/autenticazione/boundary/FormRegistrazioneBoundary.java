package it.afam.is.progetto.afam_app.autenticazione.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.autenticazione.controller.RegistrazioneController;
import it.afam.is.progetto.afam_app.gestioneaccount.dto.CredenzialiRegistrazione;

public class FormRegistrazioneBoundary extends JFrame {

    private final RegistrazioneController registrazioneController;

    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField codiceFiscaleField;
    private JTextField corsoDiStudiField;
    private CredenzialiRegistrazione credenziali;

    public FormRegistrazioneBoundary(RegistrazioneController registrazioneController) {
        this.registrazioneController = registrazioneController;
    }

    public void mostraFormReg() {
        setTitle("Registrazione");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        nomeField = new JTextField();
        cognomeField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        codiceFiscaleField = new JTextField();
        corsoDiStudiField = new JTextField();

        JButton okButton = new JButton("OK");

        panel.add(new JLabel("Nome *"));
        panel.add(nomeField);

        panel.add(new JLabel("Cognome *"));
        panel.add(cognomeField);

        panel.add(new JLabel("Email *"));
        panel.add(emailField);

        panel.add(new JLabel("Password *"));
        panel.add(passwordField);

        panel.add(new JLabel("Codice fiscale *"));
        panel.add(codiceFiscaleField);

        panel.add(new JLabel("Corso di studi"));
        panel.add(corsoDiStudiField);

        panel.add(new JLabel(""));
        panel.add(okButton);

        okButton.addActionListener(e -> cliccaOK());

        setContentPane(panel);
        setVisible(true);
    }

    public void inserisciCredenziali(CredenzialiRegistrazione credenziali) {
        this.credenziali = credenziali;
    }

    public void cliccaOK() {
        CredenzialiRegistrazione credenziali = new CredenzialiRegistrazione(
                nomeField.getText(),
                cognomeField.getText(),
                emailField.getText(),
                new String(passwordField.getPassword()),
                codiceFiscaleField.getText(),
                corsoDiStudiField.getText()
        );

        inserisciCredenziali(credenziali);
        registrazioneController.mandaCredenziali(this.credenziali);
        dispose();
    }
}



