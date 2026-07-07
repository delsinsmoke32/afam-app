package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.springframework.stereotype.Component;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.boundary.EmailBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.control.LoginController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.PwdRecController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.RegistrazioneController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.SPIDController;

@Component
public class AutenticazioneBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;
    private final EmailBoundary emailBoundary;
    private final PopupErroreBoundary popupErroreBoundary;

    public AutenticazioneBoundary(
            DBMSBoundary dbmsBoundary,
            EmailBoundary emailBoundary,
            PopupErroreBoundary popupErroreBoundary
    ) {
        this.dbmsBoundary = dbmsBoundary;
        this.emailBoundary = emailBoundary;
        this.popupErroreBoundary = popupErroreBoundary;
    }

    public void mostraPaginaAuth() {
        setTitle("AFAM - Autenticazione");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton loginButton = new JButton("Login");
        JButton registrazioneButton = new JButton("Registrazione");
        JButton recuperaPasswordButton = new JButton("Recupera Password");
        JButton spidButton = new JButton("Entra con SPID/eIDAS");

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        panel.add(loginButton);
        panel.add(registrazioneButton);
        panel.add(recuperaPasswordButton);
        panel.add(spidButton);

        loginButton.addActionListener(e -> CliccaLogin());

        registrazioneButton.addActionListener(e -> CliccaRegistrati());

        recuperaPasswordButton.addActionListener(e -> CliccaRecPwd());

        spidButton.addActionListener(e -> CliccaEntraSPID());

        setContentPane(panel);
        setVisible(true);
    }

    public void CliccaLogin() {
        new LoginController(this, dbmsBoundary, emailBoundary);
    }

    public void CliccaRegistrati() {
        new RegistrazioneController(this, dbmsBoundary);
    }

    public void CliccaRecPwd() {
        // <<create>> PwdRecController
        new PwdRecController(this, dbmsBoundary, emailBoundary);
    }

    public void CliccaEntraSPID() {
        // <<create>> SPIDController
        new SPIDController(this, dbmsBoundary);
    }

    public void mostraAutenticazione() {
        mostraPaginaAuth();
    }

    public void MostraAutenticazione() {
        mostraPaginaAuth();
    }
}



