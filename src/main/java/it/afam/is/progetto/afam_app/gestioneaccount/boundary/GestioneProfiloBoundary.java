package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.control.CancProfController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.GestioneProfiloController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.ModDatiPersController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.ModificaPwdController;

public class GestioneProfiloBoundary extends JFrame {

    private final AutenticazioneBoundary autenticazioneBoundary;
    private final DBMSBoundary dbmsBoundary;

    public GestioneProfiloBoundary(
            AutenticazioneBoundary autenticazioneBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.autenticazioneBoundary = autenticazioneBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void mostraGestioneProfilo() {
        setTitle("Gestione Profilo");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton modificaDatiButton = new JButton("Modifica Dati Personali");
        JButton modificaPasswordButton = new JButton("Modifica Password");
        JButton eliminaAccountButton = new JButton("Elimina Account");
        JButton logoutButton = new JButton("Logout");
        JButton chiudiButton = new JButton("Chiudi");

        panel.add(modificaDatiButton);
        panel.add(modificaPasswordButton);
        panel.add(eliminaAccountButton);
        panel.add(logoutButton);
        panel.add(chiudiButton);

        modificaDatiButton.addActionListener(e -> cliccaModificaDati());

        modificaPasswordButton.addActionListener(e -> cliccaModificaPassword());

        eliminaAccountButton.addActionListener(e -> cliccaEliminaAccount());

        logoutButton.addActionListener(e -> {
            cliccaLogout();
            dispose();
        });

        chiudiButton.addActionListener(e -> dispose());

        setContentPane(panel);
        setVisible(true);
    }

    public void cliccaModificaDati() {
        // <<create>> ModDatiPersController
        new ModDatiPersController(this, dbmsBoundary);
    }

    public void cliccaModificaPassword() {
        // <<create>> ModificaPwdController
        new ModificaPwdController(this, dbmsBoundary);
    }

    public void cliccaEliminaAccount() {
        // <<create>> CancProfController
        new CancProfController(this, autenticazioneBoundary, dbmsBoundary);
    }

    public void cliccaLogout() {
        // <<create>> GestioneProfiloController
        GestioneProfiloController gestioneProfiloController =
                new GestioneProfiloController(autenticazioneBoundary);

        // logout()
        gestioneProfiloController.logout();
    }
}