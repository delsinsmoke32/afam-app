package it.afam.is.progetto.afam_app.consult_cat_est.boundary;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;

public class VisualizzazionePortfolioPubblicoBoundary extends JFrame {

    private JLabel imagePreviewLabel;
    private JTextArea dettagliTextArea; // Nuova area per i metadati e descrizioni
    private String pathImmagineSelezionata;

    public void mostraPortfolio(PortfolioEntity portfolio) {
        setTitle(portfolio != null ? "Portfolio Pubblico - " + portfolio.getTitolo() : "Portfolio Pubblico");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        if (portfolio == null) {
            add(new JLabel("Portfolio non trovato", SwingConstants.CENTER), BorderLayout.CENTER);
            setVisible(true);
            return;
        }

        // --- TOP PANEL (Intestazione Portfolio) ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton btnBack = new JButton("← Chiudi");
        btnBack.addActionListener(e -> dispose());
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
        costruisciAlbero(rootNode, portfolio);

        JTree tree = new JTree(rootNode);
        impostaIconeAlbero(tree);
        JScrollPane treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setBorder(BorderFactory.createTitledBorder("Esplora"));

        // --- AREA DETTAGLI E METADATI (In basso a destra) ---
        dettagliTextArea = new JTextArea(6, 20);
        dettagliTextArea.setEditable(false);
        dettagliTextArea.setLineWrap(true);
        dettagliTextArea.setWrapStyleWord(true);
        dettagliTextArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dettagliTextArea.setBackground(new Color(245, 245, 245));
        JScrollPane dettagliScroll = new JScrollPane(dettagliTextArea);
        dettagliScroll.setBorder(BorderFactory.createTitledBorder("Dettagli e Metadati"));

        // Listener per click sull'albero (Aggiornato per gestire anche le Sezioni)
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
                    selezionaAllegato(null); // Svuota l'immagine
                    mostraDescrizioneSezione(sezione);
                } else {
                    // Cliccato sulla radice
                    selezionaAllegato(null);
                    dettagliTextArea.setText("Seleziona una sezione o un file per visualizzarne i dettagli.");
                }
            }
        });

        // --- ANTEPRIMA PANEL (Al centro a destra) ---
        JPanel anteprimaPanel = new JPanel(new BorderLayout());
        anteprimaPanel.setBorder(BorderFactory.createTitledBorder("Anteprima Visiva"));

        imagePreviewLabel = new JLabel("Seleziona un file dall'albero per visualizzarne l'anteprima", SwingConstants.CENTER);
        imagePreviewLabel.setForeground(Color.GRAY);
        anteprimaPanel.add(imagePreviewLabel, BorderLayout.CENTER);

        // Resize listener per l'immagine
        anteprimaPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (pathImmagineSelezionata != null) {
                    caricaImmagineRidimensionata(pathImmagineSelezionata);
                }
            }
        });

        // --- ASSEMBLAGGIO PANNELLO DESTRO (Anteprima sopra, Dettagli sotto) ---
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, anteprimaPanel, dettagliScroll);
        rightSplitPane.setResizeWeight(0.75); // L'anteprima prende il 75% dello spazio, i dettagli il 25%
        rightSplitPane.setDividerSize(5);

        // --- SPLIT PANE PRINCIPALE ---
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, rightSplitPane);
        mainSplitPane.setDividerLocation(250);
        add(mainSplitPane, BorderLayout.CENTER);

        // --- BOTTOM PANEL (Status bar) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String nomeLicenza = portfolio.getLicenza() != null && portfolio.getLicenza().getNome() != null
                ? portfolio.getLicenza().getNome() : "Nessuna";

        JLabel statusLabel = new JLabel("👁 Visualizzazione Pubblica | Licenza: " + nomeLicenza + " | Download Disabilitato");
        statusLabel.setForeground(new Color(100, 100, 100));
        bottomPanel.add(statusLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        // Inizializza testo di default
        dettagliTextArea.setText("Seleziona una sezione o un file per visualizzarne i dettagli.");

        setVisible(true);
    }

    // --- METODI LOGICI ---

    private void costruisciAlbero(DefaultMutableTreeNode rootNode, PortfolioEntity portfolio) {
        List<SezioneEntity> sezioni = portfolio.getSezioni();
        if (sezioni == null || sezioni.isEmpty()) return;

        sezioni.sort(Comparator.comparing(
                sezione -> sezione.getOrdineVisualizzazione() != null ? sezione.getOrdineVisualizzazione() : 0
        ));

        for (SezioneEntity sezione : sezioni) {
            if (sezione == null || !Boolean.TRUE.equals(sezione.getIsPubblica())) continue;

            // Ora usiamo un Wrapper anche per la Sezione!
            DefaultMutableTreeNode sectionNode = new DefaultMutableTreeNode(new SezioneNodeWrapper(sezione));
            rootNode.add(sectionNode);

            List<AllegatoEntity> allegati = sezione.getAllegati();
            if (allegati != null) {
                for (AllegatoEntity allegato : allegati) {
                    sectionNode.add(new DefaultMutableTreeNode(new AllegatoNodeWrapper(allegato)));
                }
            }
        }
    }

    // Nuovi metodi per popolare la casella di testo
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

        // Corretto da getDescrizione() a getDescrizioneBreve()
        if (allegato.getDescrizioneBreve() != null && !allegato.getDescrizioneBreve().trim().isEmpty()) {
            sb.append("\nDescrizione:\n").append(allegato.getDescrizioneBreve());
        }

        dettagliTextArea.setText(sb.toString());
    }

    private void selezionaAllegato(AllegatoEntity allegato) {
        if (allegato == null) {
            pathImmagineSelezionata = null;
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Seleziona un file dall'albero per visualizzarne l'anteprima");
            return;
        }

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