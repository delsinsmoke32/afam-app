package it.afam.is.progetto.afam_app.gestione_portfolio.boundary;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.CancellaSezioneController;

public class ListaSezioniBoundary extends JFrame {

    private final CancellaSezioneController cancellaSezioneController;

    public ListaSezioniBoundary(CancellaSezioneController cancellaSezioneController) {
        this.cancellaSezioneController = cancellaSezioneController;
    }

    public void mostraListaSezioni(List<SezioneEntity> listaSezioni) {
        setTitle("Lista Sezioni");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Seleziona la sezione da eliminare:"));

        for (SezioneEntity sezione : listaSezioni) {
            JButton sezioneButton = new JButton(sezione.getTitolo());

            sezioneButton.addActionListener(e -> selezionaSezioneDaEliminare(sezione.getId()));

            panel.add(sezioneButton);
        }

        setContentPane(panel);
        setVisible(true);
    }

    public void selezionaSezioneDaEliminare(Long sezione_id) {
        // cancellaSezione(sezione_id)
        cancellaSezioneController.cancellaSezione(sezione_id);
    }
}