package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.gestioneaccount.control.InserimentoSezioneController;

public class FormInserimentoSezioneBoundary extends JFrame {

    private final InserimentoSezioneController inserimentoSezioneController;

    private JTextField titoloField;
    private JTextArea corpoTestoArea;

    public FormInserimentoSezioneBoundary(InserimentoSezioneController inserimentoSezioneController) {
        this.inserimentoSezioneController = inserimentoSezioneController;
    }

    public void mostraForm() {
        setTitle("Inserimento SezioneEntity");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel titoloLabel = new JLabel("Titolo:");
        titoloField = new JTextField();

        JLabel corpoTestoLabel = new JLabel("Corpo testo:");
        corpoTestoArea = new JTextArea();

        JButton confermaButton = new JButton("Conferma");
        confermaButton.addActionListener(e -> confermaInserimentoSezione());

        panel.add(titoloLabel);
        panel.add(titoloField);
        panel.add(corpoTestoLabel);
        panel.add(corpoTestoArea);
        panel.add(new JLabel());
        panel.add(confermaButton);

        setContentPane(panel);
        setVisible(true);
    }

    public void inserisciDatiSezione(String titolo, String corpo_testo) {
        titoloField.setText(titolo);
        corpoTestoArea.setText(corpo_testo);
    }

    public void confermaInserimentoSezione() {
        String titolo = titoloField.getText();
        String corpo_testo = corpoTestoArea.getText();

        // mandaCampi(titolo, corpo_testo)
        inserimentoSezioneController.mandaCampi(titolo, corpo_testo);
    }
}
