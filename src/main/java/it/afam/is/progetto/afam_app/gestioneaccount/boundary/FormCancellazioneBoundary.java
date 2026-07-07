package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import it.afam.is.progetto.afam_app.gestioneaccount.control.CancProfController;

public class FormCancellazioneBoundary extends JFrame {

    private final CancProfController cancProfController;

    private JPasswordField campoPassword;
    private String password;

    public FormCancellazioneBoundary(CancProfController cancProfController) {
        this.cancProfController = cancProfController;
    }

    public void mostraForm(JPasswordField campoPassword) {
        this.campoPassword = campoPassword;

        setTitle("Conferma eliminazione account");
        setSize(420, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelCampi = new JPanel(new GridLayout(1, 2, 8, 8));

        panelCampi.add(new JLabel("Password"));
        panelCampi.add(this.campoPassword);

        JButton confermaDefinitivaButton = new JButton("Conferma definitiva");

        confermaDefinitivaButton.addActionListener(e -> {
            // inserisciPassword(password)
            inserisciPassword(new String(this.campoPassword.getPassword()));

            // cliccaConfermaDefinitiva()
            cliccaConfermaDefinitiva();
        });

        add(panelCampi, BorderLayout.CENTER);
        add(confermaDefinitivaButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void inserisciPassword(String password) {
        this.password = password;
    }

    public void cliccaConfermaDefinitiva() {
        // mandaPassword(password)
        mandaPassword(password);
    }

    public void mandaPassword(String password) {
        cancProfController.mandaPassword(password);
    }
}

