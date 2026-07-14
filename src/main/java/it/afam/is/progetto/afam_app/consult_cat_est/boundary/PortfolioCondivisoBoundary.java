package it.afam.is.progetto.afam_app.consult_cat_est.boundary;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.consult_cat_est.controller.PortfolioCondivisoController;

public class PortfolioCondivisoBoundary extends JFrame {

    private final PortfolioCondivisoController portfolioCondivisoController;
    private final List<SezioneEntity> sezioniVisibili;
    private final List<AllegatoEntity> allegatiVisibili;
    private PortfolioEntity portfolio;

    private JLabel imagePreviewLabel;
    private JTextArea dettagliTextArea; // Nuova area per i metadati e descrizioni
    private JButton btnScarica;
    private Long allegatoSelezionatoId;
    private String pathImmagineSelezionata; // Per gestire il ridimensionamento

    public PortfolioCondivisoBoundary(
            PortfolioCondivisoController portfolioCondivisoController,
            List<SezioneEntity> sezioniVisibili,
            List<AllegatoEntity> allegatiVisibili
    ) {
        this.portfolioCondivisoController = portfolioCondivisoController;
        this.sezioniVisibili = sezioniVisibili;
        this.allegatiVisibili = allegatiVisibili;
    }

    public void mostraPortfolioCondiviso(PortfolioEntity portfolio) {
        this.portfolio = portfolio;

        System.out.println("DEBUG - Sezioni ricevute: " + (sezioniVisibili != null ? sezioniVisibili.size() : "NULL"));
        System.out.println("DEBUG - Allegati ricevuti: " + (allegatiVisibili != null ? allegatiVisibili.size() : "NULL"));

        setTitle(portfolio != null ? "Portfolio Condiviso - " + portfolio.getTitolo() : "Portfolio Condiviso");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        if (portfolio == null) {
            add(new JLabel("Portfolio non disponibile", SwingConstants.CENTER), BorderLayout.CENTER);
            setVisible(true);
            return;
        }

        // --- TOP PANEL (Intestazione) ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton btnBack = new JButton("← Torna ai Risultati");
        btnBack.addActionListener(e -> cliccaTornaIndietro());
        topPanel.add(btnBack, BorderLayout.WEST);

        // Pannello per Titolo e Descrizione Portfolio
        JPanel infoPortfolioPanel = new JPanel(new GridLayout(2, 1));
        JLabel titleLabel = new JLabel(portfolio.getTitolo());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        String descPortfolio = portfolio.getDescrizione() != null ? portfolio.getDescrizione() : "Nessuna descrizione disponibile per questo portfolio.";
        JLabel descLabel = new JLabel("<html><i>" + descPortfolio + "</i></html>");
        descLabel.setForeground(Color.DARK_GRAY);

        infoPortfolioPanel.add(titleLabel);
        infoPortfolioPanel.add(descLabel);
        topPanel.add(infoPortfolioPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // --- JTREE (Struttura a sinistra) ---
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Struttura Portfolio");
        costruisciAlbero(rootNode);

        JTree tree = new JTree(rootNode);
        impostaIconeAlbero(tree);

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setBorder(BorderFactory.createTitledBorder("Struttura Portfolio"));

        // --- AREA DETTAGLI E METADATI (In basso a destra) ---
        dettagliTextArea = new JTextArea(6, 20);
        dettagliTextArea.setEditable(false);
        dettagliTextArea.setLineWrap(true);
        dettagliTextArea.setWrapStyleWord(true);
        dettagliTextArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dettagliTextArea.setBackground(new Color(245, 245, 245));
        JScrollPane dettagliScroll = new JScrollPane(dettagliTextArea);
        dettagliScroll.setBorder(BorderFactory.createTitledBorder("Dettagli e Metadati"));

        // Listener per click sull'albero
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                Object userObj = selectedNode.getUserObject();

                if (userObj instanceof AllegatoNodeWrapper) {
                    AllegatoEntity allegato = ((AllegatoNodeWrapper) userObj).getAllegato();
                    selezionaAllegato(allegato);
                    mostraMetadatiAllegato(allegato);
                } else if (userObj instanceof SezioneNodeWrapper) {
                    SezioneEntity sezione = ((SezioneNodeWrapper) userObj).getSezione();
                    selezionaAllegato(null); // Disabilita download e svuota l'immagine
                    mostraDescrizioneSezione(sezione);
                } else {
                    // Cliccato sulla radice
                    selezionaAllegato(null);
                    dettagliTextArea.setText("Seleziona una sezione o un file per visualizzarne i dettagli.");
                }
            }
        });

        // --- ANTEPRIMA PANEL (Al centro a destra) ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Anteprima Allegato"));

        imagePreviewLabel = new JLabel("Seleziona un file dall'albero per visualizzarne l'anteprima", SwingConstants.CENTER);
        imagePreviewLabel.setForeground(Color.GRAY);
        rightPanel.add(imagePreviewLabel, BorderLayout.CENTER);

        // Bottone Download in basso all'anteprima
        JPanel downloadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnScarica = new JButton("Scarica File");
        btnScarica.setEnabled(false); // Disabilitato finché non selezioni un file
        btnScarica.addActionListener(e -> cliccaScarica(allegatoSelezionatoId));
        downloadPanel.add(btnScarica);
        rightPanel.add(downloadPanel, BorderLayout.SOUTH);

        // Resize listener per adattare l'immagine dinamicamente
        rightPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (pathImmagineSelezionata != null) {
                    caricaImmagineRidimensionata(pathImmagineSelezionata);
                }
            }
        });

        // --- ASSEMBLAGGIO PANNELLO DESTRO (Anteprima sopra, Dettagli sotto) ---
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rightPanel, dettagliScroll);
        rightSplitPane.setResizeWeight(0.75); // L'anteprima prende il 75% dello spazio, i dettagli il 25%
        rightSplitPane.setDividerSize(5);

        // --- SPLIT PANE PRINCIPALE ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, rightSplitPane);
        splitPane.setDividerLocation(250); // Larghezza del menu laterale
        add(splitPane, BorderLayout.CENTER);

        // --- BOTTOM PANEL ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel statusLabel = new JLabel("Modalità Condivisa");
        statusLabel.setForeground(new Color(153, 0, 0));
        bottomPanel.add(statusLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        // Inizializza testo di default
        dettagliTextArea.setText("Seleziona una sezione o un file per visualizzarne i dettagli.");

        setVisible(true);
    }

    // --- METODI LOGICI ---

    private void costruisciAlbero(DefaultMutableTreeNode rootNode) {
        if (sezioniVisibili == null) return;

        for (SezioneEntity sezione : sezioniVisibili) {
            // Usiamo il wrapper anche qui
            DefaultMutableTreeNode sectionNode = new DefaultMutableTreeNode(new SezioneNodeWrapper(sezione));
            rootNode.add(sectionNode);

            if (allegatiVisibili != null) {
                for (AllegatoEntity allegato : allegatiVisibili) {
                    if (allegato.getSezione() != null && allegato.getSezione().getId().equals(sezione.getId())) {
                        sectionNode.add(new DefaultMutableTreeNode(new AllegatoNodeWrapper(allegato)));
                    }
                }
            }
        }
    }

    private void mostraDescrizioneSezione(SezioneEntity sezione) {
        String desc = sezione.getCorpoTesto();
        if (desc == null || desc.trim().isEmpty()) {
            dettagliTextArea.setText("Nessuna descrizione fornita per questa sezione.");
        } else {
            dettagliTextArea.setText("DESCRIZIONE SEZIONE:\n\n" + desc);
        }
    }

    private void mostraMetadatiAllegato(AllegatoEntity allegato) {
        StringBuilder sb = new StringBuilder();
        sb.append("METADATI ALLEGATO:\n\n");

        sb.append("Titolo Opera: ").append(allegato.getTitoloOpera() != null ? allegato.getTitoloOpera() : "Nessun titolo").append("\n");
        sb.append("Nome File: ").append(allegato.getNomeFile() != null ? allegato.getNomeFile() : "Sconosciuto").append("\n");

        if (allegato.getAutoreOpera() != null && !allegato.getAutoreOpera().trim().isEmpty()) {
            sb.append("Autore: ").append(allegato.getAutoreOpera()).append("\n");
        }

        if (allegato.getTipoFile() != null && !allegato.getTipoFile().trim().isEmpty()) {
            sb.append("Formato: ").append(allegato.getTipoFile()).append("\n");
        }

        if (allegato.getDataCreazione() != null) {
            sb.append("Data Creazione: ").append(allegato.getDataCreazione()).append("\n");
        }

        if (allegato.getDescrizioneBreve() != null && !allegato.getDescrizioneBreve().trim().isEmpty()) {
            sb.append("\nDescrizione:\n").append(allegato.getDescrizioneBreve());
        }

        dettagliTextArea.setText(sb.toString());
    }

    private void selezionaAllegato(AllegatoEntity allegato) {
        if (allegato == null) {
            allegatoSelezionatoId = null;
            pathImmagineSelezionata = null;
            btnScarica.setEnabled(false);
            btnScarica.setText("Scarica File");
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Seleziona un file dall'albero per visualizzarne l'anteprima");
            return;
        }

        allegatoSelezionatoId = allegato.getId();
        btnScarica.setEnabled(true);
        btnScarica.setText("Scarica: " + (allegato.getTitoloOpera() != null ? allegato.getTitoloOpera() : allegato.getNomeFile()));

        String nomeFile = allegato.getNomeFile() != null ? allegato.getNomeFile().toLowerCase() : "";
        String estensione = nomeFile.contains(".") ? nomeFile.substring(nomeFile.lastIndexOf(".") + 1) : "";

        if (estensione.equals("jpg") || estensione.equals("jpeg") || estensione.equals("png")) {
            pathImmagineSelezionata = allegato.getPercorsoRisorsa();
            caricaImmagineRidimensionata(pathImmagineSelezionata);
        } else {
            pathImmagineSelezionata = null;
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Anteprima non disponibile per questo formato (." + estensione.toUpperCase() + ")");
        }
    }

    private void caricaImmagineRidimensionata(String path) {
        try {
            ImageIcon originalIcon = new ImageIcon(path);
            if (originalIcon.getIconWidth() <= 0) {
                imagePreviewLabel.setText("Immagine non trovata nel filesystem.");
                return;
            }

            int labelW = imagePreviewLabel.getWidth();
            int labelH = imagePreviewLabel.getHeight();

            if (labelW == 0 || labelH == 0) return;

            Image img = originalIcon.getImage();
            Image scaledImg = img.getScaledInstance(-1, labelH - 20, Image.SCALE_SMOOTH);

            if (scaledImg.getWidth(null) > labelW) {
                scaledImg = img.getScaledInstance(labelW - 20, -1, Image.SCALE_SMOOTH);
            }

            imagePreviewLabel.setIcon(new ImageIcon(scaledImg));
            imagePreviewLabel.setText(null);
        } catch (Exception e) {
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Errore nel caricamento dell'immagine.");
        }
    }

    private void impostaIconeAlbero(JTree tree) {
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        renderer.setLeafIcon(UIManager.getIcon("FileView.fileIcon"));
        renderer.setClosedIcon(UIManager.getIcon("FileView.directoryIcon"));
        renderer.setOpenIcon(UIManager.getIcon("FileView.directoryIcon"));
    }

    // --- METODI COMPATIBILI CON I DIAGRAMMI ---

    public void mostraPortfolio() {
        mostraPortfolioCondiviso(portfolio);
    }

    public void cliccaScarica(Long allegato_id) {
        portfolioCondivisoController.controllaDownload(allegato_id);
    }

    public void cliccaTornaIndietro() {
        dispose();
    }

    // AGGIORNATO per permettere la selezione del file diretto (e suggerire il nome)
    public String apriSelettoreSalvataggio(String nomeFileSuggerito) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salva file come...");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (nomeFileSuggerito != null && !nomeFileSuggerito.trim().isEmpty()) {
            fileChooser.setSelectedFile(new java.io.File(nomeFileSuggerito));
        }

        int risultato = fileChooser.showSaveDialog(this);

        if (risultato == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }

        return null;
    }

    // --- CLASSI WRAPPER PER L'ALBERO ---

    private static class SezioneNodeWrapper {
        private final SezioneEntity sezione;

        public SezioneNodeWrapper(SezioneEntity sezione) {
            this.sezione = sezione;
        }

        public SezioneEntity getSezione() {
            return sezione;
        }

        @Override
        public String toString() {
            return sezione.getTitolo();
        }
    }

    private static class AllegatoNodeWrapper {
        private final AllegatoEntity allegato;

        public AllegatoNodeWrapper(AllegatoEntity allegato) {
            this.allegato = allegato;
        }

        public AllegatoEntity getAllegato() {
            return allegato;
        }

        @Override
        public String toString() {
            String nome = allegato.getNomeFile();
            return allegato.getTitoloOpera() != null ? allegato.getTitoloOpera() + " (" + nome + ")" : nome;
        }
    }
}