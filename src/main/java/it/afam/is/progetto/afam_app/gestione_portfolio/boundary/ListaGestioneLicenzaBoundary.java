package it.afam.is.progetto.afam_app.gestione_portfolio.boundary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.entity.LicenzaEntity;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.GestioneLicenzaController;

public class ListaGestioneLicenzaBoundary extends JFrame {

    private final GestioneLicenzaController gestioneLicenzaController;

    private JComboBox<LicenzaWrapper> comboLicenze;
    private LicenzaEntity licenzaImpostata;

    public ListaGestioneLicenzaBoundary(GestioneLicenzaController gestioneLicenzaController) {
        this.gestioneLicenzaController = gestioneLicenzaController;
    }

    public void mostraLicenzeDisponibili(List<LicenzaEntity> listaLicenzeDisponibili) {
        setTitle("Gestione Licenza Portfolio");
        setSize(650, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 15));

        // --- PANNELLO SUPERIORE (Intestazione) ---
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(new Color(240, 240, 240));
        topContainer.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        // Pulsante Torna Indietro
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backPanel.setOpaque(false);
        JButton btnBack = new JButton("← Annulla e Torna al Portfolio");
        btnBack.addActionListener(e -> dispose());
        backPanel.add(btnBack);
        topContainer.add(backPanel, BorderLayout.NORTH);

        // Titolo informativo
        JLabel lblTitolo = new JLabel("Seleziona la licenza d'uso per le tue opere:");
        lblTitolo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitolo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        topContainer.add(lblTitolo, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);

        // --- PANNELLO CENTRALE (Pannello Contenitore ComboBox) ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        if (listaLicenzeDisponibili == null || listaLicenzeDisponibili.isEmpty()) {
            centerPanel.add(new JLabel("Nessuna licenza disponibile nel sistema.", JLabel.CENTER), BorderLayout.CENTER);
            add(centerPanel, BorderLayout.CENTER);
            setVisible(true);
            return;
        }

        // Popoliamo la JComboBox usando una classe Wrapper interna per una visualizzazione corretta
        comboLicenze = new JComboBox<>();
        LicenzaWrapper elementoSelezionato = null;

        for (LicenzaEntity licenza : listaLicenzeDisponibili) {
            LicenzaWrapper wrapper = new LicenzaWrapper(licenza);
            comboLicenze.addItem(wrapper);

            // Se questa licenza è quella attualmente impostata nel portfolio, la teniamo a mente
            if (licenzaImpostata != null
                    && licenza.getId() != null
                    && licenza.getId().equals(licenzaImpostata.getId())) {
                elementoSelezionato = wrapper;
            }
        }

        // Se abbiamo trovato la licenza attuale, forziamo la ComboBox a preselezionarla
        if (elementoSelezionato != null) {
            comboLicenze.setSelectedItem(elementoSelezionato);
        }

        comboLicenze.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(comboLicenze, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // --- PANNELLO INFERIORE (Salvataggio) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        bottomPanel.setBackground(new Color(230, 230, 230));

        JButton salvaButton = new JButton("Salva Licenza");
        salvaButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        salvaButton.addActionListener(e -> selezionaLicenza());
        bottomPanel.add(salvaButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void evidenziaLicenzaImpostata(LicenzaEntity licenza) {
        this.licenzaImpostata = licenza;

        // Se la finestra è già visibile, riaggiorna la selezione nella ComboBox
        if (comboLicenze != null) {
            for (int i = 0; i < comboLicenze.getItemCount(); i++) {
                LicenzaWrapper wrapper = comboLicenze.getItemAt(i);
                if (wrapper != null && wrapper.getLicenza().getId().equals(licenza.getId())) {
                    comboLicenze.setSelectedItem(wrapper);
                    break;
                }
            }
        }
    }

    public void selezionaLicenza() {
        if (comboLicenze == null) {
            return;
        }

        LicenzaWrapper wrapper = (LicenzaWrapper) comboLicenze.getSelectedItem();

        if (wrapper == null || wrapper.getLicenza() == null || wrapper.getLicenza().getId() == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Seleziona una licenza valida dalla lista.");
            return;
        }

        Long licenza_id = wrapper.getLicenza().getId();

        // Invia l'ID selezionato al controller originale, mantenendo la compatibilità totale
        gestioneLicenzaController.scegliLicenza(licenza_id);
    }

    // --- CLASSE WRAPPER INTERNA ---
    // Serve a definire in che modo la JComboBox converte l'entità complessa in stringa leggibile
    private static class LicenzaWrapper {
        private final LicenzaEntity licenza;

        public LicenzaWrapper(LicenzaEntity licenza) {
            this.licenza = licenza;
        }

        public LicenzaEntity getLicenza() {
            return licenza;
        }

        @Override
        public String toString() {
            if (licenza == null) return "";
            String nome = licenza.getNome() != null ? licenza.getNome() : "";
            String descrizione = licenza.getDescrizione() != null ? licenza.getDescrizione() : "";
            return nome + " (" + descrizione + ")";
        }
    }
}