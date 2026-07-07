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

    public void mostraPaginaPersonale(StudenteEntity StudenteEntity) {
        setTitle("Pagina personale");
        setSize(450, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        JLabel titolo = new JLabel("Benvenuto: " + StudenteEntity.getEmail());

        JButton gestioneProfiloButton = new JButton("Gestione profilo");
        JButton gestionePortfolioButton = new JButton("Gestione portfolio");
        JButton logoutButton = new JButton("Logout");

        panel.add(titolo);
        panel.add(gestioneProfiloButton);
        panel.add(gestionePortfolioButton);
        panel.add(logoutButton);

        gestioneProfiloButton.addActionListener(e -> {
            GestioneProfiloBoundary gestioneProfiloBoundary =
                    new GestioneProfiloBoundary(autenticazioneBoundary, dbmsBoundary);

            gestioneProfiloBoundary.mostraGestioneProfilo();
        });

        gestionePortfolioButton.addActionListener(e -> {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Gestione portfolio non ancora implementata.");
        });

        logoutButton.addActionListener(e -> {
            GestioneProfiloBoundary gestioneProfiloBoundary =
                    new GestioneProfiloBoundary(autenticazioneBoundary, dbmsBoundary);

            gestioneProfiloBoundary.cliccaLogout();

            dispose();
        });

        setContentPane(panel);
        setVisible(true);
    }
}

