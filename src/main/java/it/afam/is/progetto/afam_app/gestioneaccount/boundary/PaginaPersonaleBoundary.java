package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;

public class PaginaPersonaleBoundary extends JFrame {

    private final AutenticazioneBoundary autenticazioneBoundary;
    private final DBMSBoundary dbmsBoundary;

    public PaginaPersonaleBoundary(
            AutenticazioneBoundary autenticazioneBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.autenticazioneBoundary = autenticazioneBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void mostraPaginaPersonale(StudenteEntity studente) {
        setTitle("Pagina personale");
        setSize(450, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        JLabel titolo = new JLabel("Benvenuto: " + studente.getEmail());

        JButton gestioneProfiloButton = new JButton("Gestione profilo");
        JButton gestionePortfolioButton = new JButton("Gestione Portfolio");
        JButton logoutButton = new JButton("Logout");

        panel.add(titolo);
        panel.add(gestioneProfiloButton);
        panel.add(gestionePortfolioButton);
        panel.add(logoutButton);

        gestioneProfiloButton.addActionListener(e -> cliccaGestioneProfilo());

        gestionePortfolioButton.addActionListener(e -> cliccaGestionePortfolio());

        logoutButton.addActionListener(e -> {
            GestioneProfiloBoundary gestioneProfiloBoundary =
                    new GestioneProfiloBoundary(autenticazioneBoundary, dbmsBoundary);

            gestioneProfiloBoundary.cliccaLogout();

            dispose();
        });

        setContentPane(panel);
        setVisible(true);
    }

    public void cliccaGestioneProfilo() {
        GestioneProfiloBoundary gestioneProfiloBoundary =
                new GestioneProfiloBoundary(autenticazioneBoundary, dbmsBoundary);

        gestioneProfiloBoundary.mostraGestioneProfilo();
    }

    public void cliccaGestionePortfolio() {
        GestionePortfolioBoundary gestionePortfolioBoundary =
                new GestionePortfolioBoundary(dbmsBoundary);

        gestionePortfolioBoundary.mostraGestionePortfolio();
    }
}