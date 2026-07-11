package it.afam.is.progetto.afam_app.gestione_portfolio.boundary;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.GestioneVisibilitaPortController;

public class GestioneVisibilitaPortBoundary extends JFrame {

    private final GestioneVisibilitaPortController gestioneVisibilitaPortController;

    private final Map<Long, JCheckBox> checkboxSezioni = new HashMap<>();

    public GestioneVisibilitaPortBoundary(GestioneVisibilitaPortController gestioneVisibilitaPortController) {
        this.gestioneVisibilitaPortController = gestioneVisibilitaPortController;
    }

    public void mostraStatoVisibilita(List<SezioneEntity> impostazioni) {
        setTitle("Gestione Visibilità Sezioni Portfolio");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Gestisci la visibilità pubblica delle sezioni del portfolio:"));

        checkboxSezioni.clear();

        if (impostazioni == null || impostazioni.isEmpty()) {
            panel.add(new JLabel("Nessuna sezione disponibile."));
        } else {
            for (SezioneEntity sezione : impostazioni) {
                if (sezione == null || sezione.getId() == null) {
                    continue;
                }

                JCheckBox checkBox = new JCheckBox(
                        sezione.getTitolo(),
                        Boolean.TRUE.equals(sezione.getIsPubblica())
                );

                checkboxSezioni.put(sezione.getId(), checkBox);
                panel.add(checkBox);
            }
        }

        JButton salvaButton = new JButton("Salva");
        salvaButton.addActionListener(e -> cliccaSalva());

        panel.add(salvaButton);

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void modificaStatoVisibilita(Boolean nuovoStato) {
        for (JCheckBox checkBox : checkboxSezioni.values()) {
            checkBox.setSelected(Boolean.TRUE.equals(nuovoStato));
        }
    }

    public void cliccaSalva() {
        Map<Long, Boolean> impostazioni = new HashMap<>();

        for (Map.Entry<Long, JCheckBox> entry : checkboxSezioni.entrySet()) {
            impostazioni.put(entry.getKey(), entry.getValue().isSelected());
        }

        // salvaModifiche(impostazioni)
        gestioneVisibilitaPortController.salvaModifiche(impostazioni);
    }
}