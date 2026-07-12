package it.afam.is.progetto.afam_app.gestione_sezione.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import it.afam.is.progetto.afam_app.gestione_sezione.controller.ModificaDescrizioneSezController;

public class FormDescrizioneSezioneBoundary extends JFrame {

    private final ModificaDescrizioneSezController modificaDescrizioneSezController;

    private JTextArea descrizioneArea;

    public FormDescrizioneSezioneBoundary(ModificaDescrizioneSezController modificaDescrizioneSezController) {
        this.modificaDescrizioneSezController = modificaDescrizioneSezController;
    }

    public void mostraForm(String descrizione) {
        setTitle("Modifica Descrizione Sezione");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- INTESTAZIONE ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel label = new JLabel("Inserisci o modifica la descrizione della sezione:");
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(label);
        add(topPanel, BorderLayout.NORTH);

        // --- AREA TESTO ---
        descrizioneArea = new JTextArea(8, 30);
        descrizioneArea.setLineWrap(true);
        descrizioneArea.setWrapStyleWord(true);
        descrizioneArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Precompilazione dei dati se esistenti (già gestita bene da te!)
        if (descrizione != null && !descrizione.trim().isEmpty()) {
            descrizioneArea.setText(descrizione);
        }

        JScrollPane scrollPane = new JScrollPane(descrizioneArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTONI ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(new Color(240, 240, 240));

        JButton btnAnnulla = new JButton("Annulla");
        btnAnnulla.addActionListener(e -> dispose());

        JButton confermaButton = new JButton("Conferma");
        confermaButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        confermaButton.addActionListener(e -> cliccaConferma());

        bottomPanel.add(btnAnnulla);
        bottomPanel.add(confermaButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void modificaDescrizione(String descrizione) {
        if (descrizioneArea != null) {
            descrizioneArea.setText(descrizione);
        }
    }

    public void cliccaConferma() {
        String descrizione = descrizioneArea.getText();

        // mandaModifica(descrizione)
        modificaDescrizioneSezController.mandaModifica(descrizione);
    }
}