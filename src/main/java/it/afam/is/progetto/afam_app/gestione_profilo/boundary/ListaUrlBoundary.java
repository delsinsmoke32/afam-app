package it.afam.is.progetto.afam_app.gestione_profilo.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.format.DateTimeFormatter;
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

import it.afam.is.progetto.afam_app.entity.LinkCondivisoEntity;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestione_profilo.controller.RevocaURLController;

public class ListaUrlBoundary extends JFrame {

    private final RevocaURLController revocaURLController;

    private JTable tabellaUrl;
    private UrlTableModel tableModel;
    private JButton btnRevoca;
    private LinkCondivisoEntity URL;

    public ListaUrlBoundary(RevocaURLController revocaURLController) {
        this.revocaURLController = revocaURLController;
    }

    public void mostraLista(List<LinkCondivisoEntity> elencoUrl) {
        setTitle("Revoca URL Condivisi");
        setSize(800, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 10));

        // --- PANNELLO SUPERIORE ---
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(new Color(240, 240, 240));
        topContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitoloPagina = new JLabel("Seleziona un URL da REVOCARE:");
        lblTitoloPagina.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitoloPagina.setForeground(new Color(180, 0, 0)); // Rosso per indicare attenzione
        topContainer.add(lblTitoloPagina, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);

        // --- PANNELLO CENTRALE (JTable) ---
        tableModel = new UrlTableModel(elencoUrl);
        tabellaUrl = new JTable(tableModel);
        tabellaUrl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaUrl.setRowHeight(32);
        tabellaUrl.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabellaUrl.setFont(new Font("SansSerif", Font.PLAIN, 13));

        // Regoliamo la larghezza delle colonne
        tabellaUrl.getColumnModel().getColumn(0).setPreferredWidth(150); // Nome Riferimento
        tabellaUrl.getColumnModel().getColumn(1).setPreferredWidth(250); // Token
        tabellaUrl.getColumnModel().getColumn(2).setPreferredWidth(150); // Scadenza

        JScrollPane scrollPane = new JScrollPane(tabellaUrl);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        add(scrollPane, BorderLayout.CENTER);

        // --- PANNELLO INFERIORE (Azioni) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        bottomPanel.setBackground(new Color(230, 230, 230));

        btnRevoca = new JButton("Revoca URL Selezionato");
        btnRevoca.setEnabled(false); // Disabilitato finché non clicchi una riga
        btnRevoca.setIcon(UIManager.getIcon("InternalFrame.closeIcon"));
        btnRevoca.setForeground(Color.RED);

        bottomPanel.add(btnRevoca);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- GESTIONE EVENTI ---
        tabellaUrl.getSelectionModel().addListSelectionListener(e -> {
            btnRevoca.setEnabled(tabellaUrl.getSelectedRow() >= 0);
        });

        btnRevoca.addActionListener(e -> cliccaRevoca());

        setVisible(true);
    }

    public void selezionaUrl(LinkCondivisoEntity URL) {
        this.URL = URL;
    }

    public void cliccaRevoca() {
        int selectedRow = tabellaUrl.getSelectedRow();

        if (selectedRow < 0) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Seleziona un URL da revocare.");
            return;
        }

        LinkCondivisoEntity selezionato = tableModel.getUrlAt(selectedRow);

        // selezionaUrl(URL)
        selezionaUrl(selezionato);

        // mandaUrl(URL)
        mandaUrl(this.URL);
    }

    public void mandaUrl(LinkCondivisoEntity URL) {
        revocaURLController.mandaUrl(URL);
    }

    // --- TABLE MODEL PERSONALIZZATO ---

    private static class UrlTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Nome Condivisione", "Token / Link", "Data Scadenza"};
        private final List<LinkCondivisoEntity> data;
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        public UrlTableModel(List<LinkCondivisoEntity> data) {
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
            LinkCondivisoEntity link = data.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return (link.getNomeRiferimento() != null && !link.getNomeRiferimento().trim().isEmpty())
                            ? link.getNomeRiferimento() : "Senza nome";
                case 1:
                    return link.getTokenUrl() != null ? link.getTokenUrl() : "";
                case 2:
                    return link.getDataScadenza() != null ? link.getDataScadenza().format(formatter) : "N/A";
                default:
                    return null;
            }
        }

        public LinkCondivisoEntity getUrlAt(int rowIndex) {
            return data.get(rowIndex);
        }
    }
}