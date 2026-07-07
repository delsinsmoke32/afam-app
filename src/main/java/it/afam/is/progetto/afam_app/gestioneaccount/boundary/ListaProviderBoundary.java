package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.gestioneaccount.control.SPIDController;

public class ListaProviderBoundary extends JFrame {

    private final SPIDController spidController;

    private String scelta;

    public ListaProviderBoundary(SPIDController spidController) {
        this.spidController = spidController;
    }

    public void MostraProviderSPID() {
        setTitle("Provider SPID/eIDAS");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton posteButton = new JButton("PosteID");
        JButton arubaButton = new JButton("ArubaID");
        JButton namirialButton = new JButton("NamirialID");

        panel.add(posteButton);
        panel.add(arubaButton);
        panel.add(namirialButton);

        posteButton.addActionListener(e -> {
            // scegliProvider(scelta)
            scegliProvider("POSTE_ID");
        });

        arubaButton.addActionListener(e -> {
            // scegliProvider(scelta)
            scegliProvider("ARUBA_ID");
        });

        namirialButton.addActionListener(e -> {
            // scegliProvider(scelta)
            scegliProvider("NAMIRIAL_ID");
        });

        setContentPane(panel);
        setVisible(true);
    }

    public void scegliProvider(String scelta) {
        this.scelta = scelta;

        // providerScelto(provider_id)
        providerScelto(this.scelta);
    }

    public void providerScelto(String provider_id) {
        spidController.providerScelto(provider_id);
    }
}



