package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.control.OrdinamentoSezioniController;

public class ListaSezioniOrdinamentoBoundary extends JFrame {

    private final OrdinamentoSezioniController ordinamentoSezioniController;

    private DefaultListModel<SezioneEntity> modelloLista;
    private JList<SezioneEntity> listaSezioni;

    public ListaSezioniOrdinamentoBoundary(OrdinamentoSezioniController ordinamentoSezioniController) {
        this.ordinamentoSezioniController = ordinamentoSezioniController;
    }

    public void mostraListaSezioni(List<SezioneEntity> listaSezioniInput) {
        setTitle("Ordinamento Sezioni");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        modelloLista = new DefaultListModel<>();

        if (listaSezioniInput != null) {
            for (SezioneEntity sezione : listaSezioniInput) {
                modelloLista.addElement(sezione);
            }
        }

        listaSezioni = new JList<>(modelloLista);
        listaSezioni.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listaSezioni.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            javax.swing.JLabel label = new javax.swing.JLabel(testoSezione(value, index));
            label.setOpaque(true);

            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }

            return label;
        });

        JButton suButton = new JButton("Sposta su");
        suButton.addActionListener(e -> spostaSu());

        JButton giuButton = new JButton("Sposta giù");
        giuButton.addActionListener(e -> spostaGiu());

        JButton confermaButton = new JButton("Conferma ordinamento");
        confermaButton.addActionListener(e -> confermaNuovoOrdinamento());

        JPanel panelBottoni = new JPanel(new GridLayout(1, 3, 10, 10));
        panelBottoni.add(suButton);
        panelBottoni.add(giuButton);
        panelBottoni.add(confermaButton);

        add(new JScrollPane(listaSezioni), BorderLayout.CENTER);
        add(panelBottoni, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void modificaOrdineSezioni(List<Long> nuovoOrdine) {
        // Metodo presente per coerenza 1:1 col sequence.
        // L'ordine reale viene modificato graficamente con Sposta su / Sposta giù.
    }

    public void confermaNuovoOrdinamento() {
        List<Long> nuovoOrdine = new ArrayList<>();

        for (int i = 0; i < modelloLista.size(); i++) {
            SezioneEntity sezione = modelloLista.getElementAt(i);

            if (sezione != null && sezione.getId() != null) {
                nuovoOrdine.add(sezione.getId());
            }
        }

        // confermaOrdinamentoSezioni(nuovoOrdine, portfolio_id)
        ordinamentoSezioniController.confermaOrdinamentoSezioni(
                nuovoOrdine,
                ordinamentoSezioniController.getPortfolio_id()
        );
    }

    private void spostaSu() {
        int indice = listaSezioni.getSelectedIndex();

        if (indice <= 0) {
            return;
        }

        SezioneEntity sezione = modelloLista.remove(indice);
        modelloLista.add(indice - 1, sezione);
        listaSezioni.setSelectedIndex(indice - 1);
    }

    private void spostaGiu() {
        int indice = listaSezioni.getSelectedIndex();

        if (indice < 0 || indice >= modelloLista.size() - 1) {
            return;
        }

        SezioneEntity sezione = modelloLista.remove(indice);
        modelloLista.add(indice + 1, sezione);
        listaSezioni.setSelectedIndex(indice + 1);
    }

    private String testoSezione(SezioneEntity sezione, int index) {
        if (sezione == null) {
            return "";
        }

        String titolo = sezione.getTitolo() != null ? sezione.getTitolo() : "Senza titolo";

        return (index + 1) + ". " + titolo;
    }
}