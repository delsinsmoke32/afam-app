package it.afam.is.progetto.afam_app.consult_cat_est.boundary;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.EmailBoundary;
import it.afam.is.progetto.afam_app.autenticazione.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.consult_cat_est.controller.*;

@Component
public class HomepageBoundary extends JFrame {
    private final DBMSBoundary dbmsBoundary;
    private final EmailBoundary emailBoundary;
    private JTextField urlField, ricercaField, portfolioIdField;
    private RicercaStudentiController ricercaStudentiController;

    public HomepageBoundary(DBMSBoundary dbmsBoundary, EmailBoundary emailBoundary) {
        this.dbmsBoundary = dbmsBoundary;
        this.emailBoundary = emailBoundary;
    }

    public void mostraHomepage() {
        setTitle("Catalogo Portfolio AFAM");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top: Bottone per andare ad Autenticazione
        JButton btnAuth = new JButton("Autenticati (Area Studenti)");
        btnAuth.addActionListener(e -> {
            dispose();
            // 3. Ora passi entrambi gli oggetti richiesti dal nuovo costruttore
            new AutenticazioneBoundary(dbmsBoundary, emailBoundary).mostraPaginaAuth();
        });
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.add(btnAuth);
        add(top, BorderLayout.NORTH);

        // Center: Form del Catalogo
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        urlField = new JTextField();
        ricercaField = new JTextField();
        portfolioIdField = new JTextField();

        JButton apriUrlButton = new JButton("Vai al Portfolio");
        apriUrlButton.addActionListener(e -> cliccaURLCondiviso());

        JButton ricercaButton = new JButton("Cerca");
        ricercaButton.addActionListener(e -> cliccaOK());

        JButton elencoStudentiButton = new JButton("Mostra Elenco Completo Studenti");
        elencoStudentiButton.addActionListener(e -> cliccaMostraElencoStudenti());

        panel.add(new JLabel("URL Portfolio Condiviso:"));
        panel.add(urlField);
        panel.add(apriUrlButton);

        panel.add(new JLabel("Cerca nel Catalogo:"));
        panel.add(ricercaField);
        panel.add(ricercaButton);

        panel.add(elencoStudentiButton);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    // Metodi originali mantenuti intatti
    public void cliccaURLCondiviso() {
        new PortfolioCondivisoController(this, dbmsBoundary).mandaURL(urlField.getText());
    }

    public void cliccaOK() {
        if (ricercaStudentiController == null) {
            ricercaStudentiController = new RicercaStudentiController(this, dbmsBoundary);
        }
        ricercaStudentiController.ricercaStudente(ricercaField.getText());
    }

    public void cliccaMostraElencoStudenti() {
        new VisualizzazioneElencoStudentiController(this, dbmsBoundary).recuperaElencoStudenti();
    }
}