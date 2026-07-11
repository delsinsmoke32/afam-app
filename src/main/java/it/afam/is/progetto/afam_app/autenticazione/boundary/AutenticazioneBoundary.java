package it.afam.is.progetto.afam_app.autenticazione.boundary;

import javax.swing.*;
import java.awt.*;
import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.HomepageBoundary;
import it.afam.is.progetto.afam_app.autenticazione.controller.*;
import it.afam.is.progetto.afam_app.gestioneaccount.control.SPIDController;
import org.springframework.stereotype.Component;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;

@Component
public class AutenticazioneBoundary extends JFrame {
    private final DBMSBoundary dbmsBoundary;

    public AutenticazioneBoundary(DBMSBoundary dbmsBoundary) {
        this.dbmsBoundary = dbmsBoundary;
    }

    public void mostraPaginaAuth() {
        setTitle("AFAM - Area Studenti");
        setSize(400, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // DISPOSE così non chiude l'intera app se aperta dalla Home
        setLayout(new BorderLayout());

        // Solo i 4 bottoni visti nel mockup
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JButton loginButton = new JButton("Accedi con 2FA");
        JButton spidButton = new JButton("Entra con SPID");
        JButton registrazioneButton = new JButton("Registrazione");
        JButton recuperaPasswordButton = new JButton("Recupero Password");

        panel.add(loginButton);
        panel.add(spidButton);
        panel.add(registrazioneButton);
        panel.add(recuperaPasswordButton);

        // Listener con i metodi originali per i Sequence Diagram
        loginButton.addActionListener(e -> CliccaLogin());
        spidButton.addActionListener(e -> CliccaEntraSPID());
        registrazioneButton.addActionListener(e -> CliccaRegistrati());
        recuperaPasswordButton.addActionListener(e -> CliccaRecPwd());

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void CliccaLogin() { new LoginController(this, dbmsBoundary, null); }
    public void CliccaRegistrati() { new RegistrazioneController(this, dbmsBoundary); }
    public void CliccaRecPwd() { new PwdRecController(this, dbmsBoundary, null); }
    public void CliccaEntraSPID() { new PopupErroreBoundary().mostraPopupErrore("Funzionalità SPID attualmente non disponibile."); }
    // Aggiungi questi due metodi dentro AutenticazioneBoundary.java
// In questo modo i controller che chiamano i vecchi nomi smetteranno di dare errore

    public void MostraAutenticazione() {
        mostraPaginaAuth();
    }

    public void mostraAutenticazione() {
        mostraPaginaAuth();
    }
}