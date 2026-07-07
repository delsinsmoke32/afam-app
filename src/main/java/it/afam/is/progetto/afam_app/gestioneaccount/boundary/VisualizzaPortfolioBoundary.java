package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.boundary.FileStorageBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.control.InserimentoSezioneController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.VisualizzaSezioneController;

public class VisualizzaPortfolioBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;
    private final FileStorageBoundary fileStorageBoundary;
    private final Long portfolio_id;

    public VisualizzaPortfolioBoundary(DBMSBoundary dbmsBoundary, Long portfolio_id) {
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = new FileStorageBoundary();
        this.portfolio_id = portfolio_id;
    }

    public VisualizzaPortfolioBoundary(
            DBMSBoundary dbmsBoundary,
            FileStorageBoundary fileStorageBoundary,
            Long portfolio_id
    ) {
        this.dbmsBoundary = dbmsBoundary;
        this.fileStorageBoundary = fileStorageBoundary;
        this.portfolio_id = portfolio_id;
    }

    public void mostraPortfolioInit(PortfolioEntity portfolio, List<SezioneEntity> sezioniPortfolio) {
        setTitle("Visualizza Portfolio");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Portfolio: " + portfolio.getTitolo()));
        panel.add(new JLabel("Descrizione: " + portfolio.getDescrizione()));

        JButton aggiungiSezioneButton = new JButton("Aggiungi nuova sezione");
        aggiungiSezioneButton.addActionListener(e -> selezionaInserisciNuovaSezione());

        panel.add(aggiungiSezioneButton);

        panel.add(new JLabel("Sezioni:"));

        if (sezioniPortfolio == null || sezioniPortfolio.isEmpty()) {
            panel.add(new JLabel("Nessuna sezione presente."));
        } else {
            for (SezioneEntity sezione : sezioniPortfolio) {
                JButton sezioneButton = new JButton(sezione.getTitolo());

                // cliccaVisualizzaSezione()
                sezioneButton.addActionListener(e -> cliccaVisualizzaSezione(sezione.getId()));

                panel.add(sezioneButton);
            }
        }

        setContentPane(panel);
        setVisible(true);
    }

    public void mostraPortfolio() {
        setTitle("Visualizza Portfolio");
        setSize(450, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();

        JLabel titolo = new JLabel("Visualizza Portfolio");
        JButton aggiungiSezioneButton = new JButton("Aggiungi sezione");

        aggiungiSezioneButton.addActionListener(e -> selezionaInserisciNuovaSezione());

        panel.add(titolo);
        panel.add(aggiungiSezioneButton);

        setContentPane(panel);
        setVisible(true);
    }

    public void selezionaInserisciNuovaSezione() {
        // <<create>> InserimentoSezioneController
        InserimentoSezioneController inserimentoSezioneController =
                new InserimentoSezioneController(this, dbmsBoundary);

        // avviaInserimentoSezione(portfolio_id)
        inserimentoSezioneController.avviaInserimentoSezione(portfolio_id);
    }

    public void cliccaVisualizzaSezione(Long sezione_id) {
        // <<create>> VisualizzaSezioneController
        VisualizzaSezioneController visualizzaSezioneController =
                new VisualizzaSezioneController(this, dbmsBoundary, fileStorageBoundary);

        // richiediVisualizzazione(sezione_id)
        visualizzaSezioneController.richiediVisualizzazione(sezione_id);
    }

    public void mostraPortfolioAggiungiSezione(SezioneEntity sezione) {
        mostraPortfolio();
    }
}