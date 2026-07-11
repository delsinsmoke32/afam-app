package it.afam.is.progetto.afam_app.autenticazione.boundary;

import javax.swing.*;
import java.awt.*;
import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.EmailBoundary;
import it.afam.is.progetto.afam_app.autenticazione.controller.*;
import it.afam.is.progetto.afam_app.consult_cat_est.boundary.HomepageBoundary; // <-- IMPORT NECESSARIO
import org.springframework.stereotype.Component;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;

@Component
public class AutenticazioneBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;
    private final EmailBoundary emailBoundary;

    public AutenticazioneBoundary(DBMSBoundary dbmsBoundary, EmailBoundary emailBoundary) {
        this.dbmsBoundary = dbmsBoundary;
        this.emailBoundary = emailBoundary;
    }

    public void mostraPaginaAuth() {
        setTitle("AFAM - Area Studenti");
        setSize(400, 420); // Aumentato leggermente per fare spazio al nuovo bottone
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- INIZIO AGGIUNTA BACK BUTTON ---
        JButton backButton = new JButton("← Torna alla Home");
        backButton.addActionListener(e -> cliccaTornaAllaHome());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);
        // --- FINE AGGIUNTA BACK BUTTON ---

        // Solo i 4 bottoni visti nel mockup
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 40, 50)); // Ridotto leggermente il bordo superiore

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

    // Metodo specifico per la gestione del tasto indietro
    public void cliccaTornaAllaHome() {
        dispose(); // 1. Chiude la pagina di autenticazione
        new HomepageBoundary(dbmsBoundary, emailBoundary).mostraHomepage(); // 2. Riapre la Home fresca
    }

    public void CliccaLogin() { new LoginController(this, dbmsBoundary, emailBoundary); }
    public void CliccaRegistrati() { new RegistrazioneController(this, dbmsBoundary); }
    public void CliccaRecPwd() { new PwdRecController(this, dbmsBoundary, emailBoundary); }
    public void CliccaEntraSPID() { new PopupErroreBoundary().mostraPopupErrore("Funzionalità SPID attualmente non disponibile."); }

    // Metodi ponte per retrocompatibilità coi Controller
    public void MostraAutenticazione() {
        mostraPaginaAuth();
    }

    public void mostraAutenticazione() {
        mostraPaginaAuth();
    }
}