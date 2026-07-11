package it.afam.is.progetto.afam_app.gestione_portfolio.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.FileStorageBoundary;
import it.afam.is.progetto.afam_app.entity.Sessione;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.common.PopupErroreBoundary;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.CancellazionePortfolioController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.CreazionePortfolioController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.VisualizzaPortfolioController;

public class GestionePortfolioBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;
    private final FileStorageBoundary fileStorageBoundary;

    public GestionePortfolioBoundary(DBMSBoundary dbmsBoundary) {
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = new FileStorageBoundary();
    }

    public void mostraGestionePortfolio() {
        setTitle("Gestione Portfolio");
        setSize(450, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        JLabel titolo = new JLabel("Gestione Portfolio");

        JButton creaPortfolioButton = new JButton("Crea portfolio");
        creaPortfolioButton.addActionListener(e -> selezionaCreazionePortfolio());

        JButton visualizzaPortfolioButton = new JButton("Visualizza portfolio");
        visualizzaPortfolioButton.addActionListener(e -> selezionaVisualizzaPortfolio());

        JButton cancellaPortfolioButton = new JButton("Cancella portfolio");
        cancellaPortfolioButton.addActionListener(e -> selezionaCancellazionePortfolio());

        panel.add(titolo);
        panel.add(creaPortfolioButton);
        panel.add(visualizzaPortfolioButton);
        panel.add(cancellaPortfolioButton);

        setContentPane(panel);
        revalidate();
        repaint();
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
        StudenteEntity studente = Sessione.getStudenteCorrente();

        if (studente == null || studente.getId() == null) {
            PopupErroreBoundary popupErroreBoundary = new PopupErroreBoundary();
            popupErroreBoundary.mostraPopup("Studente non autenticato.");

            mostraGestionePortfolio();
            return;
        }

        // <<create>> VisualizzaPortfolioController
        VisualizzaPortfolioController visualizzaPortfolioController =
                new VisualizzaPortfolioController(this, dbmsBoundary);

        // recuperaListaPortfoli(studente_id)
        visualizzaPortfolioController.recuperaListaPortfoli(studente.getId());
    }

    public void selezionaCancellazionePortfolio() {
        // <<create>> CancellazionePortfolioController
        CancellazionePortfolioController cancellazionePortfolioController =
                new CancellazionePortfolioController(this, dbmsBoundary, fileStorageBoundary);

        // recuperaPortfoliStudente(idStudente)
        cancellazionePortfolioController.avviaCancellazionePortfolio();
    }
}