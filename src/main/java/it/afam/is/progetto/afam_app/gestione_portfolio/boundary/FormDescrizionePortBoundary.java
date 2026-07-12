package it.afam.is.progetto.afam_app.gestione_portfolio.boundary;

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

import it.afam.is.progetto.afam_app.gestione_portfolio.controller.ModificaDescrizionePortController;

public class FormDescrizionePortBoundary extends JFrame {

    private final ModificaDescrizionePortController controller;
    private JTextArea textAreaDescrizione;

    public FormDescrizionePortBoundary(ModificaDescrizionePortController controller) {
        this.controller = controller;
    }

    public void mostraForm(String descrizioneCorrente) {
        setTitle("Modifica Descrizione Portfolio");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- INTESTAZIONE ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel lblTitolo = new JLabel("Inserisci o modifica la descrizione:");
        lblTitolo.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(lblTitolo);
        add(topPanel, BorderLayout.NORTH);

        // --- AREA TESTO ---
        textAreaDescrizione = new JTextArea(8, 30);
        textAreaDescrizione.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textAreaDescrizione.setLineWrap(true);
        textAreaDescrizione.setWrapStyleWord(true);

        // Popoliamo la text area con la descrizione attuale (se esiste)
        if (descrizioneCorrente != null && !descrizioneCorrente.trim().isEmpty()) {
            textAreaDescrizione.setText(descrizioneCorrente);
        }

        JScrollPane scrollPane = new JScrollPane(textAreaDescrizione);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTONI ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(new Color(240, 240, 240));

        JButton btnAnnulla = new JButton("Annulla");
        btnAnnulla.addActionListener(e -> dispose());

        JButton btnSalva = new JButton("Conferma");
        btnSalva.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnSalva.addActionListener(e -> cliccaConferma());

        bottomPanel.add(btnAnnulla);
        bottomPanel.add(btnSalva);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void cliccaConferma() {
        String nuovaDescrizione = textAreaDescrizione.getText();

        // mandaModifica(descrizione)
        controller.mandaModifica(nuovaDescrizione);

        // Chiude il form dopo aver salvato
        dispose();
    }
}