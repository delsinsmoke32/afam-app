package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

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
import it.afam.is.progetto.afam_app.gestioneaccount.control.GestioneVisibilitaController;

public class GestioneVisibilitaBoundary extends JFrame {

    private final GestioneVisibilitaController gestioneVisibilitaController;

    private final Map<Long, JCheckBox> checkboxSezioni = new HashMap<>();

    public GestioneVisibilitaBoundary(GestioneVisibilitaController gestioneVisibilitaController) {
        this.gestioneVisibilitaController = gestioneVisibilitaController;
    }

    public void mostraStatoVisibilita(List<SezioneEntity> listaSezioni) {
        setTitle("Gestione Visibilità Sezioni");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Seleziona le sezioni visibili nella candidatura:"));

        checkboxSezioni.clear();

        if (listaSezioni == null || listaSezioni.isEmpty()) {
            panel.add(new JLabel("Nessuna sezione presente."));
        } else {
            for (SezioneEntity sezione : listaSezioni) {
                JCheckBox checkBox = new JCheckBox(sezione.getTitolo(), true);

                checkboxSezioni.put(sezione.getId(), checkBox);

                panel.add(checkBox);
            }
        }

        JButton salvaButton = new JButton("Salva");
        salvaButton.addActionListener(e -> cliccaSalva());

        panel.add(salvaButton);

        setContentPane(panel);
        setVisible(true);
    }

    public void modificaStatoVisibilita(Long sezione_id, Boolean nuovoStato) {
        JCheckBox checkBox = checkboxSezioni.get(sezione_id);

        if (checkBox != null) {
            checkBox.setSelected(Boolean.TRUE.equals(nuovoStato));
        }
    }

    public void cliccaSalva() {
        Map<Long, Boolean> impostazioni = new HashMap<>();

        for (Map.Entry<Long, JCheckBox> entry : checkboxSezioni.entrySet()) {
            impostazioni.put(entry.getKey(), entry.getValue().isSelected());
        }

        // salvaModifiche(impostazioni)
        salvaModifiche(impostazioni);
    }

    public void salvaModifiche(Map<Long, Boolean> impostazioni) {
        gestioneVisibilitaController.salvaModifiche(impostazioni);
    }
}