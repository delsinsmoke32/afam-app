package it.afam.is.progetto.afam_app.gestione_sezione.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.GestioneAllegatiController;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.ModificaMetadatiController;

public class FormMetadatiFileBoundary extends JFrame {

    private GestioneAllegatiController gestioneAllegatiController;
    private ModificaMetadatiController modificaMetadatiController;

    private JTextField titoloField;
    private JTextField autoriField;
    private JTextArea descrizioneArea; // Cambiato da JTextField a JTextArea
    private JTextField dataCreazioneField;

    public FormMetadatiFileBoundary(GestioneAllegatiController gestioneAllegatiController) {
        this.gestioneAllegatiController = gestioneAllegatiController;
    }

    public FormMetadatiFileBoundary(ModificaMetadatiController modificaMetadatiController) {
        this.modificaMetadatiController = modificaMetadatiController;
    }

    public void mostraForm() {
        costruisciForm("Metadati Nuovo File", false);

        JButton salvaButton = new JButton("Salva Metadati");
        salvaButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        salvaButton.addActionListener(e -> cliccaSalva());

        aggiungiBottoni(salvaButton);
    }

    public void mostraFormMod(AllegatoEntity metadati) {
        costruisciForm("Modifica Metadati File", true);

        // Precompilazione dei dati se esistenti (Logica originale mantenuta)
        if (metadati != null) {
            modificaMetadati(
                    metadati.getTitoloOpera(),
                    metadati.getDescrizioneBreve(),
                    metadati.getAutoreOpera()
            );
        }

        JButton salvaButton = new JButton("Conferma Modifiche");
        salvaButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        salvaButton.addActionListener(e -> cliccaSalvaModifica());

        aggiungiBottoni(salvaButton);
    }

    private void costruisciForm(String titoloFinestra, boolean eModifica) {
        setTitle(titoloFinestra);
        setSize(500, eModifica ? 420 : 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- INTESTAZIONE ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel labelIntestazione = new JLabel("Inserisci o modifica i dettagli dell'allegato:");
        labelIntestazione.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(labelIntestazione);
        add(topPanel, BorderLayout.NORTH);

        // --- PANNELLO CENTRALE (Campi) ---
        JPanel centerContainer = new JPanel(new BorderLayout(5, 10));
        centerContainer.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Griglia superiore per campi a riga singola
        int righeGriglia = eModifica ? 2 : 3;
        JPanel gridPanel = new JPanel(new GridLayout(righeGriglia, 2, 10, 10));

        titoloField = new JTextField();
        autoriField = new JTextField();

        gridPanel.add(new JLabel("Titolo:"));
        gridPanel.add(titoloField);

        gridPanel.add(new JLabel("Autori:"));
        gridPanel.add(autoriField);

        if (!eModifica) {
            dataCreazioneField = new JTextField();
            gridPanel.add(new JLabel("Data creazione (yyyy-MM-dd):"));
            gridPanel.add(dataCreazioneField);
        }

        centerContainer.add(gridPanel, BorderLayout.NORTH);

        // Area per la descrizione
        JPanel panelDescrizione = new JPanel(new BorderLayout(5, 5));
        panelDescrizione.add(new JLabel("Descrizione:"), BorderLayout.NORTH);

        descrizioneArea = new JTextArea(5, 20);
        descrizioneArea.setLineWrap(true);
        descrizioneArea.setWrapStyleWord(true);
        descrizioneArea.setFont(new Font("SansSerif", Font.PLAIN, 13));

        panelDescrizione.add(new JScrollPane(descrizioneArea), BorderLayout.CENTER);

        centerContainer.add(panelDescrizione, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);
    }

    private void aggiungiBottoni(JButton salvaButton) {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(new Color(240, 240, 240));

        JButton annullaButton = new JButton("Annulla");
        annullaButton.addActionListener(e -> dispose());

        bottomPanel.add(annullaButton);
        bottomPanel.add(salvaButton);

        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    // --- METODI DI SUPPORTO ORIGINALI MANTENUTI ---

    public void inserisciMetadati(String titolo, String autori, String descrizione, LocalDate dataCreazione) {
        titoloField.setText(titolo);
        autoriField.setText(autori);
        descrizioneArea.setText(descrizione); // Cambiato in Area

        if (dataCreazione != null && dataCreazioneField != null) {
            dataCreazioneField.setText(dataCreazione.toString());
        }
    }

    public void modificaMetadati(String titolo, String descrizione, String autori) {
        titoloField.setText(titolo != null ? titolo : "");
        descrizioneArea.setText(descrizione != null ? descrizione : ""); // Cambiato in Area
        autoriField.setText(autori != null ? autori : "");
    }

    public void cliccaSalva() {
        String titolo = titoloField.getText();
        String autori = autoriField.getText();
        String descrizione = descrizioneArea.getText(); // Cambiato in Area
        LocalDate dataCreazione = leggiDataCreazione();

        gestioneAllegatiController.mandaDati(titolo, autori, descrizione, dataCreazione);
        dispose();
    }

    public void cliccaSalvaModifica() {
        String titolo = titoloField.getText();
        String descrizione = descrizioneArea.getText(); // Cambiato in Area
        String autori = autoriField.getText();

        modificaMetadatiController.mandaDati(titolo, descrizione, autori);
    }

    private LocalDate leggiDataCreazione() {
        if (dataCreazioneField == null) return LocalDate.now();

        String valore = dataCreazioneField.getText();

        if (valore == null || valore.trim().isEmpty()) {
            return LocalDate.now();
        }

        try {
            return LocalDate.parse(valore.trim());
        } catch (Exception e) {
            return LocalDate.now();
        }
    }
}