package it.afam.is.progetto.afam_app.consult_cat_est.boundary;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.dto.RisultatoRicercaDTO;
import it.afam.is.progetto.afam_app.consult_cat_est.controller.VisualizzazionePortfolioPubblicoController;

public class PaginaRicercaBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;
    private JTable tabellaRisultati;
    private PortfolioTableModel tableModel;
    private JButton btnVisualizza;

    public PaginaRicercaBoundary(DBMSBoundary dbmsBoundary) {
        this.dbmsBoundary = dbmsBoundary;
    }

    public void mostraPaginaRicerca(List<RisultatoRicercaDTO> portfoliTrovati) {
        setTitle("Risultati Ricerca Portfolio Studenti");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("Seleziona un portfolio dalla lista per visualizzarlo:"));
        add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL (JTable) ---
        tableModel = new PortfolioTableModel(portfoliTrovati);
        tabellaRisultati = new JTable(tableModel);
        tabellaRisultati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaRisultati.setRowHeight(24);

        // Gestione abilitazione bottone al click sulla riga
        tabellaRisultati.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tabellaRisultati.getSelectedRow();
            btnVisualizza.setEnabled(selectedRow >= 0);
        });

        JScrollPane scrollPane = new JScrollPane(tabellaRisultati);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM PANEL (Bottoni) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnVisualizza = new JButton("Visualizza Portfolio Pubblico");
        btnVisualizza.setEnabled(false); // Disabilitato finché non selezioni una riga

        btnVisualizza.addActionListener(e -> {
            int selectedRow = tabellaRisultati.getSelectedRow();
            if (selectedRow >= 0) {
                PortfolioEntity portfolioSelezionato = tableModel.getPortfolioAt(selectedRow);
                cliccaVisualizza(portfolioSelezionato);
            }
        });

        bottomPanel.add(btnVisualizza);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void cliccaVisualizza(PortfolioEntity portfolio) {
        if (portfolio == null || portfolio.getId() == null) return;

        VisualizzazionePortfolioPubblicoController controller =
                new VisualizzazionePortfolioPubblicoController(this, dbmsBoundary);

        controller.mandaIdPortfolio(portfolio.getId());
    }

    // --- TABLE MODEL PERSONALIZZATO ---
    private static class PortfolioTableModel extends AbstractTableModel {
        // Tolto "Corso di Studi"
        private final String[] columnNames = {"Nome", "Cognome", "Titolo Portfolio", "Descrizione"};
        private final List<RisultatoRicercaDTO> data;

        public PortfolioTableModel(List<RisultatoRicercaDTO> data) {
            this.data = data != null ? data : new ArrayList<>();
        }

        @Override
        public int getRowCount() { return data.size(); }

        @Override
        public int getColumnCount() { return columnNames.length; }

        @Override
        public String getColumnName(int column) { return columnNames[column]; }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            RisultatoRicercaDTO row = data.get(rowIndex);
            StudenteEntity s = row.getStudente();
            PortfolioEntity p = row.getPortfolio();

            switch (columnIndex) {
                case 0: return (s != null) ? s.getNome() : "";
                case 1: return (s != null) ? s.getCognome() : "";
                case 2: return (p != null) ? p.getTitolo() : "";
                case 3: return (p != null) ? p.getDescrizione() : "";
                default: return null;
            }
        }

        // Restituisce il portfolio per la visualizzazione
        public PortfolioEntity getPortfolioAt(int rowIndex) {
            return data.get(rowIndex).getPortfolio();
        }
    }
}