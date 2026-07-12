package it.afam.is.progetto.afam_app.gestione_portfolio.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.CancellazionePortfolioController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.VisualizzaPortfolioController;

public class ListaPortfolioBoundary extends JFrame {

    private VisualizzaPortfolioController visualizzaPortfolioController;
    private CancellazionePortfolioController cancellazionePortfolioController;

    private JTable tabellaPortfoli;
    private PortfolioTableModel tableModel;
    private JButton btnAzione; // Rinominato in modo generico

    public ListaPortfolioBoundary(VisualizzaPortfolioController visualizzaPortfolioController) {
        this.visualizzaPortfolioController = visualizzaPortfolioController;
    }

    public ListaPortfolioBoundary(CancellazionePortfolioController cancellazionePortfolioController) {
        this.cancellazionePortfolioController = cancellazionePortfolioController;
    }

    public void mostraListaPortfoli(List<PortfolioEntity> listaPortfoli) {
        // --- LOGICA DI CONTESTO ---
        // Capiamo chi ha chiamato questa Boundary controllando quale controller NON è nullo
        boolean isModalitaCancellazione = (cancellazionePortfolioController != null);

        String titoloFinestra = isModalitaCancellazione ? "Cancellazione Portfolio" : "I Tuoi Portfolio";
        String titoloPagina = isModalitaCancellazione ? "Seleziona un Portfolio da ELIMINARE" : "Seleziona un Portfolio da visualizzare";
        String testoBottone = isModalitaCancellazione ? "Cancella Portfolio Selezionato" : "Visualizza Portfolio";

        setTitle(titoloFinestra);
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 10));

        // --- PANNELLO SUPERIORE (Intestazione) ---
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(new Color(240, 240, 240));
        topContainer.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        // Bottone Indietro
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backPanel.setOpaque(false);
        JButton btnBack = new JButton("← Torna alla Gestione Portfolio");
        btnBack.addActionListener(e -> dispose());
        backPanel.add(btnBack);
        topContainer.add(backPanel, BorderLayout.NORTH);

        // Titolo della pagina
        JLabel lblTitoloPagina = new JLabel(titoloPagina);
        lblTitoloPagina.setFont(new Font("SansSerif", Font.BOLD, 18));
        if (isModalitaCancellazione) {
            lblTitoloPagina.setForeground(new Color(180, 0, 0)); // Rosso scuro per allertare l'utente
        }
        lblTitoloPagina.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        topContainer.add(lblTitoloPagina, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);

        // --- PANNELLO CENTRALE (JTable) ---
        tableModel = new PortfolioTableModel(listaPortfoli);
        tabellaPortfoli = new JTable(tableModel);
        tabellaPortfoli.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaPortfoli.setRowHeight(32);
        tabellaPortfoli.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabellaPortfoli.setFont(new Font("SansSerif", Font.PLAIN, 13));

        tabellaPortfoli.getColumnModel().getColumn(0).setPreferredWidth(250);
        tabellaPortfoli.getColumnModel().getColumn(1).setPreferredWidth(550);

        JScrollPane scrollPane = new JScrollPane(tabellaPortfoli);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        add(scrollPane, BorderLayout.CENTER);

        // --- PANNELLO INFERIORE (Azioni) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        bottomPanel.setBackground(new Color(230, 230, 230));

        btnAzione = new JButton(testoBottone);
        btnAzione.setEnabled(false); // Acceso solo se selezioni qualcosa

        // Aggiungiamo un tocco visivo se siamo in modalità cancellazione
        if (isModalitaCancellazione) {
            btnAzione.setIcon(UIManager.getIcon("InternalFrame.closeIcon"));
            btnAzione.setForeground(Color.RED);
        }

        bottomPanel.add(btnAzione);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- GESTIONE EVENTI ---

        tabellaPortfoli.getSelectionModel().addListSelectionListener(e -> {
            btnAzione.setEnabled(tabellaPortfoli.getSelectedRow() >= 0);
        });

        // Azione del bottone
        btnAzione.addActionListener(e -> {
            int selectedRow = tabellaPortfoli.getSelectedRow();
            if (selectedRow >= 0) {
                Long idPortfolio = tableModel.getPortfolioAt(selectedRow).getId();
                selezionaPortfolio(idPortfolio);
            }
        });

        setVisible(true);
    }

    public void selezionaPortfolio(Long portfolio_id) {
        if (visualizzaPortfolioController != null) {
            visualizzaPortfolioController.recuperaPortfolio(portfolio_id);
        }

        if (cancellazionePortfolioController != null) {
            cancellazionePortfolioController.cancellaPortfolio(portfolio_id);
        }
    }

    // --- TABLE MODEL PERSONALIZZATO ---

    private static class PortfolioTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Nome Portfolio", "Descrizione"};
        private final List<PortfolioEntity> data;

        public PortfolioTableModel(List<PortfolioEntity> data) {
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
            PortfolioEntity p = data.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return p.getTitolo() != null ? p.getTitolo() : "";
                case 1:
                    return p.getDescrizione() != null ? p.getDescrizione() : "";
                default:
                    return null;
            }
        }

        public PortfolioEntity getPortfolioAt(int rowIndex) {
            return data.get(rowIndex);
        }
    }
}