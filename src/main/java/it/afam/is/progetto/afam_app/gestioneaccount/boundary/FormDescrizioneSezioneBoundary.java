package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import it.afam.is.progetto.afam_app.gestioneaccount.control.ModificaDescrizioneController;

public class FormDescrizioneSezioneBoundary extends JFrame {

    private final ModificaDescrizioneController modificaDescrizioneController;

    private JTextArea descrizioneArea;

    public FormDescrizioneSezioneBoundary(ModificaDescrizioneController modificaDescrizioneController) {
        this.modificaDescrizioneController = modificaDescrizioneController;
    }

    public void mostraForm(String descrizione) {
        setTitle("Modifica Descrizione Sezione");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Descrizione sezione:");

        descrizioneArea = new JTextArea();
        descrizioneArea.setLineWrap(true);
        descrizioneArea.setWrapStyleWord(true);

        if (descrizione != null) {
            descrizioneArea.setText(descrizione);
        }

        JButton confermaButton = new JButton("Conferma");
        confermaButton.addActionListener(e -> cliccaConferma());

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(descrizioneArea), BorderLayout.CENTER);
        panel.add(confermaButton, BorderLayout.SOUTH);

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void modificaDescrizione(String descrizione) {
        if (descrizioneArea != null) {
            descrizioneArea.setText(descrizione);
        }
    }

    public void cliccaConferma() {
        String descrizione = descrizioneArea.getText();

        // mandaModifica(descrizione)
        modificaDescrizioneController.mandaModifica(descrizione);
    }
}