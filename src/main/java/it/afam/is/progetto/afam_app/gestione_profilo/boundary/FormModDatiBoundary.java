package it.afam.is.progetto.afam_app.gestione_profilo.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.gestione_profilo.controller.ModDatiPersController;

public class FormModDatiBoundary extends JFrame {

    private final ModDatiPersController modDatiPersController;

    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField CdSField;
    private JTextField bioField;

    private Map<String, String> dati;

    public FormModDatiBoundary(ModDatiPersController modDatiPersController) {
        this.modDatiPersController = modDatiPersController;
    }

    public void mostraForm() {
        setTitle("Modifica dati personali");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelCampi = new JPanel(new GridLayout(4, 2, 8, 8));

        nomeField = new JTextField();
        cognomeField = new JTextField();
        CdSField = new JTextField();
        bioField = new JTextField();

        panelCampi.add(new JLabel("Nome"));
        panelCampi.add(nomeField);

        panelCampi.add(new JLabel("Cognome"));
        panelCampi.add(cognomeField);

        panelCampi.add(new JLabel("CdS"));
        panelCampi.add(CdSField);

        panelCampi.add(new JLabel("Bio"));
        panelCampi.add(bioField);

        JButton bottoneOK = new JButton("OK");

        bottoneOK.addActionListener(e -> {
            // inserisciDati(nome, cognome, CdS, bio)
            inserisciDati(
                    nomeField.getText(),
                    cognomeField.getText(),
                    CdSField.getText(),
                    bioField.getText()
            );

            // cliccaOK()
            cliccaOK();
        });

        add(panelCampi, BorderLayout.CENTER);
        add(bottoneOK, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void inserisciDati(String nome, String cognome, String CdS, String bio) {
        dati = new HashMap<>();
        dati.put("nome", nome);
        dati.put("cognome", cognome);
        dati.put("CdS", CdS);
        dati.put("bio", bio);
    }

    public void cliccaOK() {
        // mandaDati(dati)
        mandaDati(dati);
    }

    public void mandaDati(Map<String, String> dati) {
        modDatiPersController.mandaDati(dati);
    }
}



