package it.afam.is.progetto.afam_app.gestione_profilo.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import it.afam.is.progetto.afam_app.entity.StatisticaAccessoEntity;
import it.afam.is.progetto.afam_app.gestione_profilo.controller.CSAController;

public class ListaStatisticheBoundary extends JFrame {

    private final CSAController csaController;
    private JTable tabellaStatistiche;

    public ListaStatisticheBoundary(CSAController csaController) {
        this.csaController = csaController;
    }

    public void mostraLista(List<StatisticaAccessoEntity> dati) {
        setTitle("Statistiche di Accesso");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 10));

        // --- PANNELLO SUPERIORE ---
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(new Color(240, 240, 240));
        topContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitoloPagina = new JLabel("Visualizzazione Statistiche di Accesso");
        lblTitoloPagina.setFont(new Font("SansSerif", Font.BOLD, 18));
        topContainer.add(lblTitoloPagina, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);

        // --- PANNELLO CENTRALE (JTable) ---
        String[] colonne = {"Nome", "Cognome", "Ruolo", "Data/Ora Accesso"};

        DefaultTableModel modello = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (dati != null) {
            for (StatisticaAccessoEntity statistica : dati) {
                String dataOra = "";
                if (statistica.getDataOraAccesso() != null) {
                    dataOra = statistica.getDataOraAccesso()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                }
                Object[] riga = {
                        statistica.getNome(),
                        statistica.getCognome(),
                        statistica.getRuolo(),
                        dataOra
                };
                modello.addRow(riga);
            }
        }

        tabellaStatistiche = new JTable(modello);
        tabellaStatistiche.setRowHeight(30);
        tabellaStatistiche.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabellaStatistiche.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(tabellaStatistiche);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        add(scrollPane, BorderLayout.CENTER);

        // --- PANNELLO INFERIORE ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        bottomPanel.setBackground(new Color(230, 230, 230));

        JButton esciButton = new JButton("Chiudi Statistiche");
        esciButton.addActionListener(e -> cliccaEsci());

        bottomPanel.add(esciButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void cliccaEsci() {
        csaController.cliccaEsci();
    }
}