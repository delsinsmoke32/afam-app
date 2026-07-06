package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.springframework.stereotype.Component;

import it.afam.is.progetto.afam_app.gestioneaccount.control.RegistrazioneControl;
import it.afam.is.progetto.afam_app.gestioneaccount.dto.CredenzialiRegistrazione;

@Component
public class FormRegistrazioneBoundary extends JFrame {

    private final RegistrazioneControl registrazioneControl;
    private final PopupErroreBoundary popupErroreBoundary;

    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField codiceFiscaleField;
    private JTextField corsoDiStudiField;

    public FormRegistrazioneBoundary(
            RegistrazioneControl registrazioneControl,
            PopupErroreBoundary popupErroreBoundary
    ) {
        this.registrazioneControl = registrazioneControl;
        this.popupErroreBoundary = popupErroreBoundary;
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

        JButton registratiButton = new JButton("Registrati");

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
        panel.add(registratiButton);

        registratiButton.addActionListener(e -> inserisciCredenziali());

        setContentPane(panel);
        setVisible(true);
    }

    private void inserisciCredenziali() {
        CredenzialiRegistrazione credenziali = new CredenzialiRegistrazione(
                nomeField.getText(),
                cognomeField.getText(),
                emailField.getText(),
                new String(passwordField.getPassword()),
                codiceFiscaleField.getText(),
                corsoDiStudiField.getText()
        );

        String errore = registrazioneControl.inserisciCredenziali(credenziali);

        if (errore != null) {
            popupErroreBoundary.mostraPopupErrore(errore);
            return;
        }

        popupErroreBoundary.mostraPopupSuccesso("Registrazione completata con successo.");
        dispose();
    }
}