package it.afam.is.progetto.afam_app.autenticazione.boundary;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.autenticazione.controller.RegistrazioneController;
import it.afam.is.progetto.afam_app.dto.CredenzialiRegistrazione;

public class FormRegistrazioneBoundary extends JFrame {

    private final RegistrazioneController registrazioneController;

    private JTextField codiceFiscaleField;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField corsoDiStudiField;
    private JTextField dataDiNascitaField;
    private JTextField linkPersonaleField;
    private JTextField biografiaField;

    private CredenzialiRegistrazione credenziali;

    public FormRegistrazioneBoundary(RegistrazioneController registrazioneController) {
        this.registrazioneController = registrazioneController;
    }

    public void mostraFormReg() {
        setTitle("Registrazione");
        setSize(450, 450); // Aumentato per fare spazio ai nuovi campi
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 10 righe, 2 colonne
        JPanel panel = new JPanel(new GridLayout(10, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Margini per un look più pulito

        codiceFiscaleField = new JTextField();
        nomeField = new JTextField();
        cognomeField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        corsoDiStudiField = new JTextField();
        dataDiNascitaField = new JTextField();
        linkPersonaleField = new JTextField();
        biografiaField = new JTextField();

        JButton registratiButton = new JButton("Registrati");

        // Aggiunti nello stesso ordine della foto
        panel.add(new JLabel("Codice Fiscale:"));
        panel.add(codiceFiscaleField);

        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);

        panel.add(new JLabel("Cognome:"));
        panel.add(cognomeField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Corso di Studi:"));
        panel.add(corsoDiStudiField);

        panel.add(new JLabel("Data di Nascita (YYYY-MM-DD):"));
        panel.add(dataDiNascitaField);

        panel.add(new JLabel("Link Personale:"));
        panel.add(linkPersonaleField);

        panel.add(new JLabel("Biografia:"));
        panel.add(biografiaField);

        panel.add(new JLabel("")); // Spazio vuoto per allineare il bottone a destra
        panel.add(registratiButton);

        registratiButton.addActionListener(e -> cliccaOK());

        setContentPane(panel);
        setVisible(true);
    }

    public void inserisciCredenziali(CredenzialiRegistrazione credenziali) {
        this.credenziali = credenziali;
    }

    public void cliccaOK() {
        // NOTA: Aggiorna il costruttore di CredenzialiRegistrazione per accettare i nuovi campi!
        CredenzialiRegistrazione credenziali = new CredenzialiRegistrazione(
                nomeField.getText(),
                cognomeField.getText(),
                emailField.getText(),
                new String(passwordField.getPassword()),
                codiceFiscaleField.getText(),
                corsoDiStudiField.getText(),
                biografiaField.getText(),
                linkPersonaleField.getText(),
                dataDiNascitaField.getText()
        );

        inserisciCredenziali(credenziali);
        registrazioneController.mandaCredenziali(this.credenziali);
        dispose();
    }
}