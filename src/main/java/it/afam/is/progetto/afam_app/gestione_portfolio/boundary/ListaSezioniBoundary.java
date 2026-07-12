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
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.CancellaSezioneController;

public class ListaSezioniBoundary extends JFrame {

    private final CancellaSezioneController cancellaSezioneController;

    private JTable tabellaSezioni;
    private SezioneTableModel tableModel;
    private JButton btnAzione;

    public ListaSezioniBoundary(CancellaSezioneController cancellaSezioneController) {
        this.cancellaSezioneController = cancellaSezioneController;
    }

    public void mostraListaSezioni(List<SezioneEntity> listaSezioni) {
        setTitle("Cancellazione Sezione");
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
        JButton btnBack = new JButton("← Annulla e Torna Indietro");
        btnBack.addActionListener(e -> dispose());
        backPanel.add(btnBack);
        topContainer.add(backPanel, BorderLayout.NORTH);

        // Titolo della pagina (in rosso per l'allerta)
        JLabel lblTitoloPagina = new JLabel("Seleziona la sezione da ELIMINARE:");
        lblTitoloPagina.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitoloPagina.setForeground(new Color(180, 0, 0));
        lblTitoloPagina.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        topContainer.add(lblTitoloPagina, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);

        // --- PANNELLO CENTRALE (JTable) ---
        tableModel = new SezioneTableModel(listaSezioni);
        tabellaSezioni = new JTable(tableModel);
        tabellaSezioni.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaSezioni.setRowHeight(32);
        tabellaSezioni.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabellaSezioni.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // Dimensionamento colonne
        tabellaSezioni.getColumnModel().getColumn(0).setPreferredWidth(200); // Nome
        tabellaSezioni.getColumnModel().getColumn(1).setPreferredWidth(500); // Descrizione
        tabellaSezioni.getColumnModel().getColumn(2).setPreferredWidth(100); // Visibilità

        JScrollPane scrollPane = new JScrollPane(tabellaSezioni);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        add(scrollPane, BorderLayout.CENTER);

        // --- PANNELLO INFERIORE (Azioni) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        bottomPanel.setBackground(new Color(230, 230, 230));

        btnAzione = new JButton("Cancella Sezione Selezionata");
        btnAzione.setEnabled(false); // Acceso solo se selezioni qualcosa
        btnAzione.setIcon(UIManager.getIcon("InternalFrame.closeIcon"));
        btnAzione.setForeground(Color.RED);

        bottomPanel.add(btnAzione);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- GESTIONE EVENTI ---

        // Attiva/Disattiva il bottone in base alla selezione
        tabellaSezioni.getSelectionModel().addListSelectionListener(e -> {
            btnAzione.setEnabled(tabellaSezioni.getSelectedRow() >= 0);
        });

        // Azione di cancellazione
        btnAzione.addActionListener(e -> {
            int selectedRow = tabellaSezioni.getSelectedRow();
            if (selectedRow >= 0) {
                Long idSezione = tableModel.getSezioneAt(selectedRow).getId();
                selezionaSezioneDaEliminare(idSezione);
            }
        });

        setVisible(true);
    }

    public void selezionaSezioneDaEliminare(Long sezione_id) {
        cancellaSezioneController.cancellaSezione(sezione_id);
    }

    // --- TABLE MODEL PERSONALIZZATO ---

    private static class SezioneTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Nome Sezione", "Descrizione", "Visibilità"};
        private final List<SezioneEntity> data;

        public SezioneTableModel(List<SezioneEntity> data) {
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
            SezioneEntity s = data.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return s.getTitolo() != null ? s.getTitolo() : "";
                case 1:
                    return s.getCorpoTesto() != null ? s.getCorpoTesto() : "";
                case 2:
                    return Boolean.TRUE.equals(s.getIsPubblica()) ? "Pubblico" : "Privato";
                default:
                    return null;
            }
        }

        public SezioneEntity getSezioneAt(int rowIndex) {
            return data.get(rowIndex);
        }
    }
}