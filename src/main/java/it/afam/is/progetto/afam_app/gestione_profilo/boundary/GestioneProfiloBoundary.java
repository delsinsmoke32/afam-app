package it.afam.is.progetto.afam_app.gestione_profilo.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.autenticazione.boundary.AutenticazioneBoundary;
import it.afam.is.progetto.afam_app.gestione_profilo.controller.CSAController;
import it.afam.is.progetto.afam_app.gestione_profilo.controller.CancProfController;
import it.afam.is.progetto.afam_app.gestione_profilo.controller.GestioneProfiloController;
import it.afam.is.progetto.afam_app.gestione_profilo.controller.ModDatiPersController;
import it.afam.is.progetto.afam_app.gestione_profilo.controller.ModificaPwdController;
import it.afam.is.progetto.afam_app.gestione_profilo.controller.RevocaURLController;

public class GestioneProfiloBoundary extends JFrame {

    private final AutenticazioneBoundary autenticazioneBoundary;
    private final DBMSBoundary dbmsBoundary;

    // Riferimento alla pagina personale per poterla chiudere al logout
    private final PaginaPersonaleBoundary paginaPersonaleBoundary;

    // Costruttore aggiornato che accetta la PaginaPersonaleBoundary
    public GestioneProfiloBoundary(
            PaginaPersonaleBoundary paginaPersonaleBoundary,
            AutenticazioneBoundary autenticazioneBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.paginaPersonaleBoundary = paginaPersonaleBoundary;
        this.autenticazioneBoundary = autenticazioneBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void mostraGestioneProfilo() {
        setTitle("Gestione Profilo");
        setSize(400, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));

        JButton modificaDatiButton = new JButton("Modifica Dati Personali");
        JButton modificaPasswordButton = new JButton("Modifica Password");
        JButton eliminaAccountButton = new JButton("Elimina Account");
        JButton revocaURLButton = new JButton("Revoca URL");
        JButton consultaStatisticheButton = new JButton("Consulta statistiche");
        JButton logoutButton = new JButton("Logout");
        JButton chiudiButton = new JButton("Chiudi");

        panel.add(modificaDatiButton);
        panel.add(modificaPasswordButton);
        panel.add(eliminaAccountButton);
        panel.add(revocaURLButton);
        panel.add(consultaStatisticheButton);
        panel.add(logoutButton);
        panel.add(chiudiButton);

        modificaDatiButton.addActionListener(e -> cliccaModificaDati());
        modificaPasswordButton.addActionListener(e -> cliccaModificaPassword());
        eliminaAccountButton.addActionListener(e -> cliccaEliminaAccount());
        revocaURLButton.addActionListener(e -> cliccaRevocaURL());
        consultaStatisticheButton.addActionListener(e -> cliccaConsultaStatistiche());

        logoutButton.addActionListener(e -> {
            cliccaLogout();

            // FIX: Se il riferimento alla pagina personale esiste, la chiudiamo forzatamente
            if (paginaPersonaleBoundary != null) {
                paginaPersonaleBoundary.dispose();
            }

            dispose(); // Chiude la finestra corrente di Gestione Profilo
        });

        chiudiButton.addActionListener(e -> dispose());

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void cliccaModificaDati() {
        new ModDatiPersController(this, dbmsBoundary);
    }

    public void cliccaModificaPassword() {
        new ModificaPwdController(this, dbmsBoundary);
    }

    public void cliccaEliminaAccount() {
        new CancProfController(this, autenticazioneBoundary, dbmsBoundary);
    }

    public void cliccaRevocaURL() {
        new RevocaURLController(this, dbmsBoundary);
    }

    public void cliccaConsultaStatistiche() {
        CSAController csaController = new CSAController(this, dbmsBoundary);
    }

    public void cliccaLogout() {
        GestioneProfiloController gestioneProfiloController =
                new GestioneProfiloController(autenticazioneBoundary);
        gestioneProfiloController.logout();
    }
}