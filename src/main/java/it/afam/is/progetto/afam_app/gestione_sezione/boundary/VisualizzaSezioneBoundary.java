package it.afam.is.progetto.afam_app.gestione_sezione.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.FileStorageBoundary;
import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.CancellazioneFileController;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.GestioneAllegatiController;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.ModificaDescrizioneSezController;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.ModificaMetadatiController;

public class VisualizzaSezioneBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;
    private final FileStorageBoundary fileStorageBoundary;
    private final Long sezione_id;

    private JTable tabellaAllegati;
    private AllegatoTableModel tableModel;
    private JTextArea dettagliArea;
    private JButton btnEliminaFile;
    private JButton btnModificaMetadati;

    public VisualizzaSezioneBoundary(
            DBMSBoundary dbmsBoundary,
            FileStorageBoundary fileStorageBoundary,
            Long sezione_id
    ) {
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = fileStorageBoundary;
        this.sezione_id = sezione_id;
    }

    public void mostraSezioneInit(List<AllegatoEntity> listaAllegati) {
        // Recuperiamo l'intera sezione per avere Titolo e Descrizione corretti
        SezioneEntity sezione = dbmsBoundary.cercaSezione(sezione_id);
        String titoloSezione = (sezione != null && sezione.getTitolo() != null) ? sezione.getTitolo() : "Sconosciuta";
        String descSezione = (sezione != null && sezione.getCorpoTesto() != null) ? sezione.getCorpoTesto() : "Nessuna descrizione disponibile.";

        setTitle("Gestione Sezione - " + titoloSezione);
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- PANNELLO SUPERIORE (Intestazione) ---
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setBackground(new Color(240, 240, 240));
        topContainer.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // Bottone Indietro
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backPanel.setOpaque(false);
        JButton btnBack = new JButton("← Torna a Visualizza Portfolio");
        btnBack.addActionListener(e -> dispose());
        backPanel.add(btnBack);
        topContainer.add(backPanel, BorderLayout.NORTH);

        // Titolo e Descrizione Sezione
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setOpaque(false);

        JLabel lblTitolo = new JLabel("Sezione: " + titoloSezione);
        lblTitolo.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel lblDescrizione = new JLabel("Descrizione: " + descSezione);
        lblDescrizione.setFont(new Font("SansSerif", Font.PLAIN, 14));

        headerPanel.add(lblTitolo);
        headerPanel.add(lblDescrizione);
        topContainer.add(headerPanel, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);

        // --- PANNELLO CENTRALE (SplitPane con Tabella e Dettagli) ---

        // Sinistra: Tabella Allegati
        tableModel = new AllegatoTableModel(listaAllegati);
        tabellaAllegati = new JTable(tableModel);
        tabellaAllegati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaAllegati.setRowHeight(30);
        tabellaAllegati.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabellaAllegati.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JScrollPane tableScrollPane = new JScrollPane(tabellaAllegati);

        // Destra: Area Dettagli Allegato
        dettagliArea = new JTextArea();
        dettagliArea.setEditable(false);
        dettagliArea.setLineWrap(true);
        dettagliArea.setWrapStyleWord(true);
        dettagliArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dettagliArea.setBackground(new Color(245, 245, 245));
        dettagliArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane dettagliScroll = new JScrollPane(dettagliArea);
        dettagliScroll.setBorder(BorderFactory.createTitledBorder("Dettagli Allegato Selezionato"));

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, dettagliScroll);
        splitPane.setDividerLocation(600); // La tabella prende più spazio
        splitPane.setResizeWeight(0.7);
        add(splitPane, BorderLayout.CENTER);

        // --- PANNELLO INFERIORE (Barra dei bottoni) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(new Color(235, 235, 235));

        JButton btnAggiungiAllegato = new JButton("Aggiungi Allegato");
        btnEliminaFile = new JButton("Cancella Allegato");
        btnEliminaFile.setIcon(UIManager.getIcon("FileView.trashIcon"));
        JButton btnModificaDescSezione = new JButton("Modifica Descrizione Sezione");
        btnModificaMetadati = new JButton("Modifica Metadati Allegato");

        // Disabilitati di default (si attivano cliccando un file nella tabella)
        btnEliminaFile.setEnabled(false);
        btnModificaMetadati.setEnabled(false);

        bottomPanel.add(btnAggiungiAllegato);
        bottomPanel.add(btnEliminaFile);
        bottomPanel.add(btnModificaDescSezione);
        bottomPanel.add(btnModificaMetadati);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- GESTIONE EVENTI ---

        // Aggiorna pannello dettagli quando si seleziona una riga
        tabellaAllegati.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tabellaAllegati.getSelectedRow();
            if (selectedRow >= 0) {
                btnEliminaFile.setEnabled(true);
                btnModificaMetadati.setEnabled(true);
                AllegatoEntity allegato = tableModel.getAllegatoAt(selectedRow);
                mostraDettagliAllegato(allegato);
            } else {
                btnEliminaFile.setEnabled(false);
                btnModificaMetadati.setEnabled(false);
                dettagliArea.setText("");
            }
        });

        // Azioni Bottoni
        btnAggiungiAllegato.addActionListener(e -> cliccaUploadFile());
        btnModificaDescSezione.addActionListener(e -> cliccaModificaDescrizione());

        btnEliminaFile.addActionListener(e -> {
            int row = tabellaAllegati.getSelectedRow();
            if (row >= 0) {
                cliccaEliminaFile(tableModel.getAllegatoAt(row).getId());
            }
        });

        btnModificaMetadati.addActionListener(e -> {
            int row = tabellaAllegati.getSelectedRow();
            if (row >= 0) {
                cliccaModificaMetadati(tableModel.getAllegatoAt(row).getId());
            }
        });

        setVisible(true);
    }

    private void mostraDettagliAllegato(AllegatoEntity allegato) {
        StringBuilder sb = new StringBuilder();

        String tipo = estraiEstensione(allegato.getNomeFile());
        if (tipo.isEmpty() && allegato.getTipoFile() != null) tipo = allegato.getTipoFile();

        sb.append("Tipo: ").append(tipo.toUpperCase()).append("\n");
        sb.append("Titolo: ").append(allegato.getTitoloOpera() != null ? allegato.getTitoloOpera() : "Sconosciuto").append("\n");
        sb.append("Autori: ").append(allegato.getAutoreOpera() != null ? allegato.getAutoreOpera() : "N/A").append("\n");
        sb.append("Data: ").append(allegato.getDataCreazione() != null ? allegato.getDataCreazione().toString() : "N/A").append("\n");
        sb.append("Descrizione: ").append(allegato.getDescrizioneBreve() != null ? allegato.getDescrizioneBreve() : "Nessuna descrizione presente.");

        dettagliArea.setText(sb.toString());
    }

    private String estraiEstensione(String nomeFile) {
        if (nomeFile != null && nomeFile.contains(".")) {
            return nomeFile.substring(nomeFile.lastIndexOf(".") + 1);
        }
        return "";
    }

    public void mostraSezione() {
        // Ricarichiamo pulendo il pannello
        List<AllegatoEntity> listaAllegati = dbmsBoundary.recuperaAllegati(sezione_id);
        getContentPane().removeAll();
        mostraSezioneInit(listaAllegati);
        revalidate();
        repaint();
    }

    // --- CHIAMATE AL CONTROLLER (invariate) ---

    public void cliccaUploadFile() {
        GestioneAllegatiController gestioneAllegatiController =
                new GestioneAllegatiController(this, dbmsBoundary, fileStorageBoundary);
        gestioneAllegatiController.richiediUpload(sezione_id);
    }

    public void cliccaModificaDescrizione() {
        ModificaDescrizioneSezController modificaDescrizioneSezController =
                new ModificaDescrizioneSezController(this, dbmsBoundary);
        modificaDescrizioneSezController.richiediModificaDescrizione(sezione_id);
    }

    public void cliccaModificaMetadati(Long allegato_id) {
        ModificaMetadatiController modificaMetadatiController =
                new ModificaMetadatiController(this, dbmsBoundary);
        modificaMetadatiController.richiediModificaMetadati(allegato_id);
    }

    public void cliccaEliminaFile(Long allegato_id) {
        CancellazioneFileController cancellazioneFileController =
                new CancellazioneFileController(this, dbmsBoundary, fileStorageBoundary);
        cancellazioneFileController.richiediCancellazioneFile(allegato_id);
    }

    public void mostraSezioneAggiungiAllegato(AllegatoEntity allegato) {
        mostraSezione();
    }

    public void mostraSezioneRimuoviAllegato(Long allegato_id) {
        mostraSezione();
    }

    public void mostraSezionePrecedente() {
        mostraSezione();
    }

    // --- TABLE MODEL PERSONALIZZATO ---

    private class AllegatoTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Tipo", "Titolo Allegato", "Autori", "Data"};
        private final List<AllegatoEntity> data;

        public AllegatoTableModel(List<AllegatoEntity> data) {
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
            AllegatoEntity a = data.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return estraiEstensione(a.getNomeFile()).toUpperCase();
                case 1:
                    return a.getTitoloOpera() != null ? a.getTitoloOpera() : a.getNomeFile();
                case 2:
                    return a.getAutoreOpera() != null ? a.getAutoreOpera() : "";
                case 3:
                    return a.getDataCreazione() != null ? a.getDataCreazione().toString() : "";
                default:
                    return null;
            }
        }

        public AllegatoEntity getAllegatoAt(int rowIndex) {
            return data.get(rowIndex);
        }
    }
}