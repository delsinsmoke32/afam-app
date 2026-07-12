package it.afam.is.progetto.afam_app.autenticazione.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
    private JTextArea biografiaArea; // Trasformato in JTextArea per maggiore comodità

    private CredenzialiRegistrazione credenziali;

    public FormRegistrazioneBoundary(RegistrazioneController registrazioneController) {
        this.registrazioneController = registrazioneController;
    }

    public void mostraFormReg() {
        setTitle("Registrazione Nuovo Studente");
        setSize(500, 600); // Dimensioni aumentate per contenere la JTextArea
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 10));

        // --- INTESTAZIONE ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        JLabel lblTitolo = new JLabel("Inserisci i tuoi dati per registrarti nel sistema:");
        lblTitolo.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(lblTitolo);
        add(topPanel, BorderLayout.NORTH);

        // --- PANNELLO CENTRALE ---
        JPanel centerContainer = new JPanel(new BorderLayout(10, 15));
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Griglia superiore per i campi brevi (8 righe)
        JPanel gridPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        codiceFiscaleField = new JTextField();
        nomeField = new JTextField();
        cognomeField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        corsoDiStudiField = new JTextField();
        dataDiNascitaField = new JTextField();
        linkPersonaleField = new JTextField();

        gridPanel.add(new JLabel("Codice Fiscale:"));
        gridPanel.add(codiceFiscaleField);

        gridPanel.add(new JLabel("Nome:"));
        gridPanel.add(nomeField);

        gridPanel.add(new JLabel("Cognome:"));
        gridPanel.add(cognomeField);

        gridPanel.add(new JLabel("Email:"));
        gridPanel.add(emailField);

        gridPanel.add(new JLabel("Password:"));
        gridPanel.add(passwordField);

        gridPanel.add(new JLabel("Corso di Studi:"));
        gridPanel.add(corsoDiStudiField);

        gridPanel.add(new JLabel("Data di Nascita (YYYY-MM-DD):"));
        gridPanel.add(dataDiNascitaField);

        gridPanel.add(new JLabel("Link Personale:"));
        gridPanel.add(linkPersonaleField);

        centerContainer.add(gridPanel, BorderLayout.NORTH);

        // Area inferiore per la Biografia
        JPanel bioPanel = new JPanel(new BorderLayout(5, 5));
        bioPanel.add(new JLabel("Biografia:"), BorderLayout.NORTH);

        biografiaArea = new JTextArea(4, 20);
        biografiaArea.setLineWrap(true);
        biografiaArea.setWrapStyleWord(true);
        biografiaArea.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane scrollBio = new JScrollPane(biografiaArea);
        bioPanel.add(scrollBio, BorderLayout.CENTER);

        centerContainer.add(bioPanel, BorderLayout.CENTER);
        add(centerContainer, BorderLayout.CENTER);

        // --- PANNELLO BOTTONI (Sud) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setBackground(new Color(230, 230, 230));

        JButton annullaButton = new JButton("Annulla");
        annullaButton.addActionListener(e -> dispose());

        JButton registratiButton = new JButton("Registrati");
        registratiButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        registratiButton.addActionListener(e -> cliccaOK());

        bottomPanel.add(annullaButton);
        bottomPanel.add(registratiButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void inserisciCredenziali(CredenzialiRegistrazione credenziali) {
        this.credenziali = credenziali;
    }

    public void cliccaOK() {
        CredenzialiRegistrazione nuoveCredenziali = new CredenzialiRegistrazione(
                nomeField.getText(),
                cognomeField.getText(),
                emailField.getText(),
                new String(passwordField.getPassword()),
                codiceFiscaleField.getText(),
                corsoDiStudiField.getText(),
                biografiaArea.getText(), // Recupera il testo dalla nuova Area
                linkPersonaleField.getText(),
                dataDiNascitaField.getText()
        );

        inserisciCredenziali(nuoveCredenziali);
        registrazioneController.mandaCredenziali(this.credenziali);
        dispose();
    }
}