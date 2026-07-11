package it.afam.is.progetto.afam_app.gestione_portfolio.boundary;

import java.awt.GridLayout;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.api.FileStorageBoundary;
import it.afam.is.progetto.afam_app.condivisione_candidatura.boundary.CondivisioneCandidaturaBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.CancellaSezioneController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.GestioneLicenzaController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.GestioneVisibilitaPortController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.InserimentoSezioneController;
import it.afam.is.progetto.afam_app.gestione_portfolio.controller.OrdinamentoSezioniController;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.VisualizzaSezioneController;

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
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Portfolio: " + portfolio.getTitolo()));
        panel.add(new JLabel("Descrizione: " + portfolio.getDescrizione()));

        JButton aggiungiSezioneButton = new JButton("Aggiungi nuova sezione");
        aggiungiSezioneButton.addActionListener(e -> selezionaInserisciNuovaSezione());

        JButton eliminaSezioneButton = new JButton("Elimina sezione");
        eliminaSezioneButton.addActionListener(e -> cliccaEliminaSezione());

        JButton condividiPortfolioButton = new JButton("Condividi portfolio");
        condividiPortfolioButton.addActionListener(e -> mostraCondivisioneCandidatura());

        JButton gestioneLicenzeButton = new JButton("Gestione licenze");
        gestioneLicenzeButton.addActionListener(e -> cliccaGestioneLicenze());

        JButton gestioneVisibilitaSezioniButton = new JButton("Gestione visibilità sezioni");
        gestioneVisibilitaSezioniButton.addActionListener(e -> cliccaGestioneVisibilitaSezioni());

        JButton ordinamentoSezioniButton = new JButton("Ordinamento sezioni");
        ordinamentoSezioniButton.addActionListener(e -> selezionaOrdinamentoSezioni());

        panel.add(aggiungiSezioneButton);
        panel.add(eliminaSezioneButton);
        panel.add(condividiPortfolioButton);
        panel.add(gestioneLicenzeButton);
        panel.add(gestioneVisibilitaSezioniButton);
        panel.add(ordinamentoSezioniButton);

        panel.add(new JLabel("Sezioni:"));

        if (sezioniPortfolio == null || sezioniPortfolio.isEmpty()) {
            panel.add(new JLabel("Nessuna sezione presente."));
        } else {
            sezioniPortfolio.sort(Comparator.comparing(
                    sezione -> sezione.getOrdineVisualizzazione() != null
                            ? sezione.getOrdineVisualizzazione()
                            : 0
            ));

            for (SezioneEntity sezione : sezioniPortfolio) {
                String stato = Boolean.TRUE.equals(sezione.getIsPubblica()) ? "pubblica" : "non pubblica";

                JButton sezioneButton = new JButton(sezione.getTitolo() + " (" + stato + ")");
                sezioneButton.addActionListener(e -> cliccaVisualizzaSezione(sezione.getId()));
                panel.add(sezioneButton);
            }
        }

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void mostraPortfolio() {
        ricaricaPortfolio();
    }

    public void mostraPaginaCondivisione() {
        mostraPortfolio();
    }

    public void ricaricaPortfolio() {
        PortfolioEntity portfolio = dbmsBoundary.recuperaPortfolio(portfolio_id);
        List<SezioneEntity> sezioniPortfolio = dbmsBoundary.recuperaSezioniPortfolio(portfolio_id);

        if (portfolio != null) {
            mostraPortfolioInit(portfolio, sezioniPortfolio);
        } else {
            mostraPortfolioFallback();
        }
    }

    private void mostraPortfolioFallback() {
        setTitle("Visualizza Portfolio");
        setSize(450, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        JLabel titolo = new JLabel("Visualizza Portfolio");

        JButton aggiungiSezioneButton = new JButton("Aggiungi sezione");
        aggiungiSezioneButton.addActionListener(e -> selezionaInserisciNuovaSezione());

        JButton eliminaSezioneButton = new JButton("Elimina sezione");
        eliminaSezioneButton.addActionListener(e -> cliccaEliminaSezione());

        JButton condividiPortfolioButton = new JButton("Condividi portfolio");
        condividiPortfolioButton.addActionListener(e -> mostraCondivisioneCandidatura());

        JButton gestioneLicenzeButton = new JButton("Gestione licenze");
        gestioneLicenzeButton.addActionListener(e -> cliccaGestioneLicenze());

        JButton gestioneVisibilitaSezioniButton = new JButton("Gestione visibilità sezioni");
        gestioneVisibilitaSezioniButton.addActionListener(e -> cliccaGestioneVisibilitaSezioni());

        JButton ordinamentoSezioniButton = new JButton("Ordinamento sezioni");
        ordinamentoSezioniButton.addActionListener(e -> selezionaOrdinamentoSezioni());

        panel.add(titolo);
        panel.add(aggiungiSezioneButton);
        panel.add(eliminaSezioneButton);
        panel.add(condividiPortfolioButton);
        panel.add(gestioneLicenzeButton);
        panel.add(gestioneVisibilitaSezioniButton);
        panel.add(ordinamentoSezioniButton);

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void selezionaInserisciNuovaSezione() {
        InserimentoSezioneController inserimentoSezioneController =
                new InserimentoSezioneController(this, dbmsBoundary);

        inserimentoSezioneController.avviaInserimentoSezione(portfolio_id);
    }

    public void cliccaVisualizzaSezione(Long sezione_id) {
        VisualizzaSezioneController visualizzaSezioneController =
                new VisualizzaSezioneController(this, dbmsBoundary, fileStorageBoundary);

        visualizzaSezioneController.richiediVisualizzazione(sezione_id);
    }

    public void cliccaEliminaSezione() {
        CancellaSezioneController cancellaSezioneController =
                new CancellaSezioneController(this, dbmsBoundary, fileStorageBoundary);

        cancellaSezioneController.avviaCancellazioneSezione(portfolio_id);
    }

    public void mostraCondivisioneCandidatura() {
        CondivisioneCandidaturaBoundary condivisioneCandidaturaBoundary =
                new CondivisioneCandidaturaBoundary(dbmsBoundary, portfolio_id);

        condivisioneCandidaturaBoundary.mostraPaginaCondivisione();
    }

    public void cliccaGestioneLicenze() {
        // <<create>> GestioneLicenzaController
        GestioneLicenzaController gestioneLicenzaController =
                new GestioneLicenzaController(this, dbmsBoundary);

        // avviaGestioneLicenza(portfolio_id)
        gestioneLicenzaController.avviaGestioneLicenza(portfolio_id);
    }

    public void cliccaGestioneVisibilitaSezioni() {
        // <<create>> GestioneVisibilitaPortController
        GestioneVisibilitaPortController gestioneVisibilitaPortController =
                new GestioneVisibilitaPortController(this, dbmsBoundary);

        gestioneVisibilitaPortController.avviaGestioneVisibilitaSezioni(portfolio_id);
    }

    public void selezionaOrdinamentoSezioni() {
        // <<create>> OrdinamentoSezioniController
        OrdinamentoSezioniController ordinamentoSezioniController =
                new OrdinamentoSezioniController(this, dbmsBoundary);

        // avviaOrdinamentoSezioni(idPortfolio)
        ordinamentoSezioniController.avviaOrdinamentoSezioni(portfolio_id);
    }

    public void mostraPortfolioAggiungiSezione(SezioneEntity sezione) {
        ricaricaPortfolio();
    }

    public void mostraPortfolioRimuoviSezione(Long sezione_id) {
        ricaricaPortfolio();
    }
}