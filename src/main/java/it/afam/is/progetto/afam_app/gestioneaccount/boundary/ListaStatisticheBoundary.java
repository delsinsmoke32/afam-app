package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.BorderLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import it.afam.is.progetto.afam_app.entity.StatisticaAccessoEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.control.CSAController;

public class ListaStatisticheBoundary extends JFrame {

    private final CSAController csaController;

    private JTable tabellaStatistiche;

    public ListaStatisticheBoundary(CSAController csaController) {
        this.csaController = csaController;
    }

    public void mostraLista(List<StatisticaAccessoEntity> dati) {
        setTitle("Statistiche Accesso");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] colonne = {
                "Nome",
                "Cognome",
                "Ruolo",
                "Data/Ora Accesso"
        };

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

        JButton esciButton = new JButton("Esci");
        esciButton.addActionListener(e -> cliccaEsci());

        JPanel panelBottoni = new JPanel();
        panelBottoni.add(esciButton);

        add(new JScrollPane(tabellaStatistiche), BorderLayout.CENTER);
        add(panelBottoni, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void cliccaEsci() {
        csaController.cliccaEsci();
    }
}