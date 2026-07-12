package it.afam.is.progetto.afam_app.gestione_profilo.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.gestione_profilo.controller.ModDatiPersController;

public class FormModDatiBoundary extends JFrame {

    private final ModDatiPersController modDatiPersController;

    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField CdSField;
    private JTextField linkPersonaleField; // Nuovo campo
    private JTextArea bioArea; // Modificata in JTextArea

    private Map<String, String> dati;

    public FormModDatiBoundary(ModDatiPersController modDatiPersController) {
        this.modDatiPersController = modDatiPersController;
    }

    public void mostraForm() {
        setTitle("Modifica Dati Personali");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- PANNELLO CAMPI LINEARI (Sopra) ---
        JPanel panelCampiOpzioni = new JPanel(new GridLayout(4, 2, 8, 8));
        panelCampiOpzioni.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        nomeField = new JTextField();
        cognomeField = new JTextField();
        CdSField = new JTextField();
        linkPersonaleField = new JTextField();

        panelCampiOpzioni.add(new JLabel("Nome:"));
        panelCampiOpzioni.add(nomeField);

        panelCampiOpzioni.add(new JLabel("Cognome:"));
        panelCampiOpzioni.add(cognomeField);

        panelCampiOpzioni.add(new JLabel("Corso di Studi (CdS):"));
        panelCampiOpzioni.add(CdSField);

        panelCampiOpzioni.add(new JLabel("Link Personale:"));
        panelCampiOpzioni.add(linkPersonaleField);

        add(panelCampiOpzioni, BorderLayout.NORTH);

        // --- PANNELLO BIO AREA TESTO (Centro) ---
        JPanel panelBio = new JPanel(new BorderLayout(5, 5));
        panelBio.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));

        JLabel lblBio = new JLabel("Biografia (Bio):");
        bioArea = new JTextArea(5, 20);
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane scrollBio = new JScrollPane(bioArea);

        panelBio.add(lblBio, BorderLayout.NORTH);
        panelBio.add(scrollBio, BorderLayout.CENTER);

        add(panelBio, BorderLayout.CENTER);

        // --- PANNELLO BOTTONI (Sotto) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(new Color(235, 235, 235));

        JButton bottoneAnnulla = new JButton("Annulla");
        bottoneAnnulla.addActionListener(e -> dispose());

        JButton bottoneOK = new JButton("Conferma");
        bottoneOK.setFont(new Font("SansSerif", Font.BOLD, 12));

        bottoneOK.addActionListener(e -> {
            inserisciDati(
                    nomeField.getText(),
                    cognomeField.getText(),
                    CdSField.getText(),
                    linkPersonaleField.getText(),
                    bioArea.getText()
            );
            cliccaOK();
        });

        bottomPanel.add(bottoneAnnulla);
        bottomPanel.add(bottoneOK);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void inserisciDati(String nome, String cognome, String CdS, String linkPersonale, String bio) {
        dati = new HashMap<>();
        dati.put("nome", nome);
        dati.put("cognome", cognome);
        dati.put("CdS", CdS);
        dati.put("linkPersonale", linkPersonale);
        dati.put("bio", bio);
    }

    public void cliccaOK() {
        mandaDati(dati);
    }

    public void mandaDati(Map<String, String> dati) {
        modDatiPersController.mandaDati(dati);
    }
}