package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import it.afam.is.progetto.afam_app.entity.LicenzaEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.control.GestioneLicenzaController;

public class ListaGestioneLicenzaBoundary extends JFrame {

    private final GestioneLicenzaController gestioneLicenzaController;

    private JList<LicenzaEntity> listaLicenze;
    private LicenzaEntity licenzaImpostata;

    public ListaGestioneLicenzaBoundary(GestioneLicenzaController gestioneLicenzaController) {
        this.gestioneLicenzaController = gestioneLicenzaController;
    }

    public void mostraLicenzeDisponibili(List<LicenzaEntity> listaLicenzeDisponibili) {
        setTitle("Gestione Licenza");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        if (listaLicenzeDisponibili == null || listaLicenzeDisponibili.isEmpty()) {
            panel.add(new JLabel("Nessuna licenza disponibile."), BorderLayout.CENTER);
            setContentPane(panel);
            setVisible(true);
            return;
        }

        listaLicenze = new JList<>(listaLicenzeDisponibili.toArray(new LicenzaEntity[0]));
        listaLicenze.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listaLicenze.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(testoLicenza(value));
            label.setOpaque(true);

            if (licenzaImpostata != null
                    && value != null
                    && value.getId() != null
                    && value.getId().equals(licenzaImpostata.getId())) {
                label.setText("[ATTUALE] " + testoLicenza(value));
            }

            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }

            return label;
        });

        JButton selezionaButton = new JButton("Seleziona licenza");
        selezionaButton.addActionListener(e -> selezionaLicenza());

        JPanel panelBottoni = new JPanel();
        panelBottoni.add(selezionaButton);

        panel.add(new JScrollPane(listaLicenze), BorderLayout.CENTER);
        panel.add(panelBottoni, BorderLayout.SOUTH);

        setContentPane(panel);
        setVisible(true);
    }

    public void evidenziaLicenzaImpostata(LicenzaEntity licenza) {
        this.licenzaImpostata = licenza;

        if (listaLicenze != null) {
            listaLicenze.repaint();
        }
    }

    public void selezionaLicenza() {
        if (listaLicenze == null) {
            return;
        }

        LicenzaEntity licenza = listaLicenze.getSelectedValue();

        if (licenza == null || licenza.getId() == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Seleziona una licenza.");
            return;
        }

        Long licenza_id = licenza.getId();

        // scegliLicenza(licenza_id)
        gestioneLicenzaController.scegliLicenza(licenza_id);
    }

    private String testoLicenza(LicenzaEntity licenza) {
        if (licenza == null) {
            return "";
        }

        String nome = licenza.getNome() != null ? licenza.getNome() : "";
        String descrizione = licenza.getDescrizione() != null ? licenza.getDescrizione() : "";

        return nome + " - " + descrizione;
    }
}