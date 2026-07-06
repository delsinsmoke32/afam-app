package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.springframework.stereotype.Component;

@Component
public class AutenticazioneBoundary extends JFrame {

    private final FormRegistrazioneBoundary formRegistrazioneBoundary;
    private final PopupErroreBoundary popupErroreBoundary;

    public AutenticazioneBoundary(
            FormRegistrazioneBoundary formRegistrazioneBoundary,
            PopupErroreBoundary popupErroreBoundary
    ) {
        this.formRegistrazioneBoundary = formRegistrazioneBoundary;
        this.popupErroreBoundary = popupErroreBoundary;
    }

    public void mostraPaginaAuth() {
        setTitle("AFAM - Autenticazione");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton loginButton = new JButton("Login");
        JButton registrazioneButton = new JButton("Registrazione");
        JButton spidButton = new JButton("Entra con SPID/eIDAS");

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        panel.add(loginButton);
        panel.add(registrazioneButton);
        panel.add(spidButton);

        registrazioneButton.addActionListener(e -> formRegistrazioneBoundary.mostraFormReg());

        loginButton.addActionListener(e ->
                popupErroreBoundary.mostraPopupErrore("Login non ancora implementato.")
        );

        spidButton.addActionListener(e ->
                popupErroreBoundary.mostraPopupSuccesso("Accesso SPID/eIDAS simulato.")
        );

        setContentPane(panel);
        setVisible(true);
    }
}