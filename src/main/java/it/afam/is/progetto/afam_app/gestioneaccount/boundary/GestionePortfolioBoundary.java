package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.control.CreazionePortfolioController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.VisualizzaPortfolioController;

public class GestionePortfolioBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;

    public GestionePortfolioBoundary(DBMSBoundary dbmsBoundary) {
        this.dbmsBoundary = dbmsBoundary;
    }

    public void mostraGestionePortfolio() {
        setTitle("Gestione Portfolio");
        setSize(450, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();

        JLabel titolo = new JLabel("Gestione Portfolio");

        JButton creaPortfolioButton = new JButton("Crea portfolio");
        creaPortfolioButton.addActionListener(e -> selezionaCreazionePortfolio());

        JButton visualizzaPortfolioButton = new JButton("Visualizza portfolio");
        visualizzaPortfolioButton.addActionListener(e -> selezionaVisualizzaPortfolio());

        panel.add(titolo);
        panel.add(creaPortfolioButton);
        panel.add(visualizzaPortfolioButton);

        setContentPane(panel);
        setVisible(true);
    }

    public void selezionaCreazionePortfolio() {
        // <<create>> CreazionePortfolioController
        CreazionePortfolioController creazionePortfolioController =
                new CreazionePortfolioController(this, dbmsBoundary);

        // avviaCreazionePortfolio()
        creazionePortfolioController.avviaCreazionePortfolio();
    }

    public void selezionaVisualizzaPortfolio() {
        // <<create>> VisualizzaPortfolioController
        new VisualizzaPortfolioController(this, dbmsBoundary);
    }
}