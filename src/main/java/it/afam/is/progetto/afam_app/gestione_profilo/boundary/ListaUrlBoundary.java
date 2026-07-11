package it.afam.is.progetto.afam_app.gestione_profilo.boundary;

import java.awt.BorderLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import it.afam.is.progetto.afam_app.entity.LinkCondivisoEntity;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestione_profilo.controller.RevocaURLController;

public class ListaUrlBoundary extends JFrame {

    private final RevocaURLController revocaURLController;

    private JList<LinkCondivisoEntity> listaUrl;
    private LinkCondivisoEntity URL;

    public ListaUrlBoundary(RevocaURLController revocaURLController) {
        this.revocaURLController = revocaURLController;
    }

    public void mostraLista(List<LinkCondivisoEntity> elencoUrl) {
        setTitle("Revoca URL");
        setSize(650, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        listaUrl = new JList<>(elencoUrl.toArray(new LinkCondivisoEntity[0]));
        listaUrl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listaUrl.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            javax.swing.JLabel label = new javax.swing.JLabel(testoUrl(value));
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

        JButton revocaButton = new JButton("Revoca");
        revocaButton.addActionListener(e -> cliccaRevoca());

        JPanel panelBottoni = new JPanel();
        panelBottoni.add(revocaButton);

        add(new JScrollPane(listaUrl), BorderLayout.CENTER);
        add(panelBottoni, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void selezionaUrl(LinkCondivisoEntity URL) {
        this.URL = URL;
    }

    public void cliccaRevoca() {
        LinkCondivisoEntity selezionato = listaUrl.getSelectedValue();

        if (selezionato == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Seleziona un URL da revocare.");
            return;
        }

        // selezionaUrl(URL)
        selezionaUrl(selezionato);

        // mandaUrl(URL)
        mandaUrl(URL);
    }

    public void mandaUrl(LinkCondivisoEntity URL) {
        revocaURLController.mandaUrl(URL);
    }

    private String testoUrl(LinkCondivisoEntity link) {
        if (link == null) {
            return "";
        }

        StringBuilder testo = new StringBuilder();

        testo.append("Nome: ");

        if (link.getNomeRiferimento() != null && !link.getNomeRiferimento().trim().isEmpty()) {
            testo.append(link.getNomeRiferimento());
        } else {
            testo.append("Senza nome");
        }

        testo.append(" | Token: ");
        testo.append(link.getTokenUrl());

        if (link.getDataScadenza() != null) {
            testo.append(" | Scade: ");
            testo.append(link.getDataScadenza().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }

        return testo.toString();
    }
}