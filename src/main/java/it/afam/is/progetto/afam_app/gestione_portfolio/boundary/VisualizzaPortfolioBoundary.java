package it.afam.is.progetto.afam_app.gestione_portfolio.boundary;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.FileStorageBoundary;
import it.afam.is.progetto.afam_app.condivisione_candidatura.boundary.CondivisioneCandidaturaBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.CancellaSezioneController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.GestioneLicenzaController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.GestioneVisibilitaPortController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.InserimentoSezioneController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.ModificaDescrizionePortController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.OrdinamentoSezioniController;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.VisualizzaSezioneController;

public class VisualizzaPortfolioBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;
    private final FileStorageBoundary fileStorageBoundary;
    private final Long portfolio_id;

    private JTable tabellaSezioni;
    private SezioneTableModel tableModel;
    private JButton btnVisualizzaSezione;

    public VisualizzaPortfolioBoundary(DBMSBoundary dbmsBoundary, Long portfolio_id) {
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = new FileStorageBoundary();
        this.portfolio_id = portfolio_id;
    }

    public VisualizzaPortfolioBoundary(
            DBMSBoundary dbmsBoundary,
            FileStorageBoundary fileStorageBoundary,
            Long portfolio_id
    ) {
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = fileStorageBoundary;
        this.portfolio_id = portfolio_id;
    }

    public void mostraPortfolioInit(PortfolioEntity portfolio, List<SezioneEntity> sezioniPortfolio) {
        setTitle("Gestione Portfolio - " + portfolio.getTitolo());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 10)); // Gap verticale

        // --- PANNELLO SUPERIORE (Intestazione) ---
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(new Color(240, 240, 240));
        topContainer.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        // Bottone Indietro
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backPanel.setOpaque(false);
        JButton btnBack = new JButton("← Torna alla Gestione Portfolio");
        btnBack.addActionListener(e -> dispose());
        backPanel.add(btnBack);
        topContainer.add(backPanel, BorderLayout.NORTH);

        // Titolo, Descrizione e Bottone Modifica
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        headerPanel.setOpaque(false);

        JLabel lblTitolo = new JLabel("Portfolio: " + portfolio.getTitolo());
        lblTitolo.setFont(new Font("SansSerif", Font.BOLD, 22));

        String descrizioneTesto = portfolio.getDescrizione() != null ? portfolio.getDescrizione() : "Nessuna descrizione";
        JLabel lblDescrizione = new JLabel("|   Descrizione: " + descrizioneTesto);
        lblDescrizione.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton btnModificaDescrizione = new JButton("Modifica Descrizione");
        btnModificaDescrizione.setFocusPainted(false);
        btnModificaDescrizione.addActionListener(e -> cliccaModificaDescrizione());

        headerPanel.add(lblTitolo);
        headerPanel.add(lblDescrizione);
        headerPanel.add(btnModificaDescrizione); // Posizionato esattamente qui come nel mockup
        topContainer.add(headerPanel, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);

        // --- PANNELLO CENTRALE (Tabella JTable) ---
        if (sezioniPortfolio != null) {
            sezioniPortfolio.sort(Comparator.comparing(
                    sezione -> sezione.getOrdineVisualizzazione() != null ? sezione.getOrdineVisualizzazione() : 0
            ));
        }

        tableModel = new SezioneTableModel(sezioniPortfolio);
        tabellaSezioni = new JTable(tableModel);
        tabellaSezioni.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaSezioni.setRowHeight(35);
        tabellaSezioni.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabellaSezioni.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(tabellaSezioni);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        add(scrollPane, BorderLayout.CENTER);

        // --- PANNELLO INFERIORE (Barra dei bottoni in 2 righe) ---
        // Usiamo un contenitore principale con GridLayout a 2 righe
        JPanel bottomContainer = new JPanel(new GridLayout(2, 1));

        JPanel rigaSuperiore = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        rigaSuperiore.setBackground(new Color(230, 230, 230));

        JPanel rigaInferiore = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        rigaInferiore.setBackground(new Color(230, 230, 230));

        // Creazione Bottoni
        btnVisualizzaSezione = new JButton("Visualizza Sezione");
        btnVisualizzaSezione.setEnabled(false); // Disabilitato se non c'è una riga selezionata
        JButton btnAggiungiSezione = new JButton("Aggiungi Sezione");
        JButton btnRimuoviSezione = new JButton("Rimuovi Sezione");

        JButton btnGestisciVisibilita = new JButton("Gestisci Visibilità");
        JButton btnGestisciLicenza = new JButton("Gestisci Licenza");
        JButton btnOrdinaSezioni = new JButton("Ordina Sezioni");
        JButton btnCondividi = new JButton("Condividi Portfolio");

        // Aggiungiamo le azioni sulle Sezioni alla prima riga
        rigaSuperiore.add(btnVisualizzaSezione);
        rigaSuperiore.add(btnAggiungiSezione);
        rigaSuperiore.add(btnRimuoviSezione);
        rigaSuperiore.add(btnOrdinaSezioni);

        // Aggiungiamo le impostazioni/condivisione del Portfolio alla seconda riga
        rigaInferiore.add(btnGestisciVisibilita);
        rigaInferiore.add(btnGestisciLicenza);
        rigaInferiore.add(btnCondividi);

        // Aggiungiamo le due righe al contenitore inferiore
        bottomContainer.add(rigaSuperiore);
        bottomContainer.add(rigaInferiore);

        // Sostituiamo il bottomPanel originale con il nostro nuovo contenitore
        add(bottomContainer, BorderLayout.SOUTH);

        // --- GESTIONE EVENTI ---

        // Abilita il bottone "Visualizza Sezione" solo quando viene selezionata una riga
        tabellaSezioni.getSelectionModel().addListSelectionListener(e -> {
            btnVisualizzaSezione.setEnabled(tabellaSezioni.getSelectedRow() >= 0);
        });

        btnVisualizzaSezione.addActionListener(e -> {
            int selectedRow = tabellaSezioni.getSelectedRow();
            if (selectedRow >= 0) {
                Long idSezione = tableModel.getSezioneAt(selectedRow).getId();
                cliccaVisualizzaSezione(idSezione);
            }
        });

        btnAggiungiSezione.addActionListener(e -> selezionaInserisciNuovaSezione());
        btnRimuoviSezione.addActionListener(e -> cliccaEliminaSezione()); // Apre il suo dialog dal controller
        btnCondividi.addActionListener(e -> mostraCondivisioneCandidatura());
        btnGestisciLicenza.addActionListener(e -> cliccaGestioneLicenze());
        btnGestisciVisibilita.addActionListener(e -> cliccaGestioneVisibilitaSezioni());
        btnOrdinaSezioni.addActionListener(e -> selezionaOrdinamentoSezioni());

        setVisible(true);
    }

    public void mostraPortfolio() {
        ricaricaPortfolio();
    }

    public void mostraPaginaCondivisione() {
        mostraPortfolio();
    }

    public void ricaricaPortfolio() {
        PortfolioEntity portfolio = dbmsBoundary.recuperaPortfolio(portfolio_id);
        List<SezioneEntity> sezioniPortfolio = dbmsBoundary.recuperaSezioniPortfolio(portfolio_id);

        if (portfolio != null) {
            // Rimuoviamo tutto dal frame prima di ricrearlo
            getContentPane().removeAll();
            mostraPortfolioInit(portfolio, sezioniPortfolio);
            revalidate();
            repaint();
        } else {
            mostraPortfolioFallback();
        }
    }

    private void mostraPortfolioFallback() {
        setTitle("Visualizza Portfolio");
        setSize(500, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel errorLabel = new JLabel("Errore: Portfolio non trovato.", SwingConstants.CENTER);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(errorLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dispose());
        bottomPanel.add(btnChiudi);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // --- METODI DI AZIONE ---

    public void cliccaModificaDescrizione() {
        ModificaDescrizionePortController modificaDescrizionePortController =
                new ModificaDescrizionePortController(this, dbmsBoundary);

        modificaDescrizionePortController.richiediModificaDescrizione(portfolio_id);
    }

    public void selezionaInserisciNuovaSezione() {
        InserimentoSezioneController inserimentoSezioneController =
                new InserimentoSezioneController(this, dbmsBoundary);
        inserimentoSezioneController.avviaInserimentoSezione(portfolio_id);
    }

    public void cliccaVisualizzaSezione(Long sezione_id) {
        VisualizzaSezioneController visualizzaSezioneController =
                new VisualizzaSezioneController(this, dbmsBoundary, fileStorageBoundary);
        visualizzaSezioneController.richiediVisualizzazione(sezione_id);
    }

    public void cliccaEliminaSezione() {
        CancellaSezioneController cancellaSezioneController =
                new CancellaSezioneController(this, dbmsBoundary, fileStorageBoundary);
        cancellaSezioneController.avviaCancellazioneSezione(portfolio_id);
    }

    public void mostraCondivisioneCandidatura() {
        CondivisioneCandidaturaBoundary condivisioneCandidaturaBoundary =
                new CondivisioneCandidaturaBoundary(dbmsBoundary, portfolio_id);
        condivisioneCandidaturaBoundary.mostraPaginaCondivisione();
    }

    public void cliccaGestioneLicenze() {
        GestioneLicenzaController gestioneLicenzaController =
                new GestioneLicenzaController(this, dbmsBoundary);
        gestioneLicenzaController.avviaGestioneLicenza(portfolio_id);
    }

    public void cliccaGestioneVisibilitaSezioni() {
        GestioneVisibilitaPortController gestioneVisibilitaPortController =
                new GestioneVisibilitaPortController(this, dbmsBoundary);
        gestioneVisibilitaPortController.avviaGestioneVisibilitaSezioni(portfolio_id);
    }

    public void selezionaOrdinamentoSezioni() {
        OrdinamentoSezioniController ordinamentoSezioniController =
                new OrdinamentoSezioniController(this, dbmsBoundary);
        ordinamentoSezioniController.avviaOrdinamentoSezioni(portfolio_id);
    }

    public void mostraPortfolioAggiungiSezione(SezioneEntity sezione) {
        ricaricaPortfolio();
    }

    public void mostraPortfolioRimuoviSezione(Long sezione_id) {
        ricaricaPortfolio();
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
                case 0: return s.getTitolo() != null ? s.getTitolo() : "";
                case 1: return s.getCorpoTesto() != null ? s.getCorpoTesto() : "";
                case 2: return Boolean.TRUE.equals(s.getIsPubblica()) ? "Pubblico" : "Privato";
                default: return null;
            }
        }

        public SezioneEntity getSezioneAt(int rowIndex) {
            return data.get(rowIndex);
        }
    }
}