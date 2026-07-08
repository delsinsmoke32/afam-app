package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.control.PortfolioCondivisoController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.RicercaStudentiController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.VisualizzazioneElencoStudentiController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.VisualizzazionePortfolioPubblicoController;

public class HomepageBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;

    private JTextField urlField;
    private JTextField ricercaField;
    private JTextField portfolioIdField;

    private RicercaStudentiController ricercaStudentiController;

    public HomepageBoundary(DBMSBoundary dbmsBoundary) {
        this.dbmsBoundary = dbmsBoundary;
    }

    public void mostraHomepage() {
        setTitle("Homepage");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        urlField = new JTextField();
        ricercaField = new JTextField();
        portfolioIdField = new JTextField();

        JButton apriUrlButton = new JButton("Apri URL condiviso");
        apriUrlButton.addActionListener(e -> cliccaURLCondiviso());

        JButton ricercaButton = new JButton("Cerca studente");
        ricercaButton.addActionListener(e -> cliccaOK());

        JButton elencoStudentiButton = new JButton("Mostra elenco studenti");
        elencoStudentiButton.addActionListener(e -> cliccaMostraElencoStudenti());

        JButton visualizzaPortfolioPubblicoButton = new JButton("Visualizza portfolio pubblico");
        visualizzaPortfolioPubblicoButton.addActionListener(e -> cliccaVisualizza());

        panel.add(new JLabel("Inserisci URL condiviso:"));
        panel.add(urlField);
        panel.add(apriUrlButton);

        panel.add(new JLabel("Cerca studente:"));
        panel.add(ricercaField);
        panel.add(ricercaButton);

        panel.add(elencoStudentiButton);

        panel.add(new JLabel("Inserisci ID portfolio pubblico:"));
        panel.add(portfolioIdField);
        panel.add(visualizzaPortfolioPubblicoButton);

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void inserisciURL(String URL) {
        urlField.setText(URL);
    }

    public void cliccaURLCondiviso() {
        String URL = urlField.getText();

        // <<create>> PortfolioCondivisoController
        PortfolioCondivisoController portfolioCondivisoController =
                new PortfolioCondivisoController(this, dbmsBoundary);

        // mandaURL(URL)
        portfolioCondivisoController.mandaURL(URL);
    }

    public void cliccaBarraRicerca() {
        // <<create>> RicercaStudentiController
        ricercaStudentiController = new RicercaStudentiController(this, dbmsBoundary);
    }

    public void cercaStudente(String ricerca) {
        if (ricercaStudentiController == null) {
            cliccaBarraRicerca();
        }

        ricercaStudentiController.setRicerca(ricerca);
    }

    public void cliccaOK() {
        if (ricercaStudentiController == null) {
            cliccaBarraRicerca();
        }

        String ricerca = ricercaField.getText();

        // cercaStudente(ricerca)
        cercaStudente(ricerca);

        // ricercaStudente(ricerca)
        ricercaStudentiController.ricercaStudente(ricerca);
    }

    public void cliccaMostraElencoStudenti() {
        // <<create>> VisualizzazioneElencoStudentiController
        VisualizzazioneElencoStudentiController visualizzazioneElencoStudentiController =
                new VisualizzazioneElencoStudentiController(this, dbmsBoundary);

        // recuperaElencoStudenti()
        visualizzazioneElencoStudentiController.recuperaElencoStudenti();
    }

    public void cliccaVisualizza() {
        Long portfolio_id = leggiPortfolioId();

        // <<create>> VisualizzazionePortfolioPubblicoController
        VisualizzazionePortfolioPubblicoController visualizzazionePortfolioPubblicoController =
                new VisualizzazionePortfolioPubblicoController(this, dbmsBoundary);

        // mandaIdPortfolio(portfolio_id)
        visualizzazionePortfolioPubblicoController.mandaIdPortfolio(portfolio_id);
    }

    private Long leggiPortfolioId() {
        try {
            String valore = portfolioIdField.getText();

            if (valore == null || valore.trim().isEmpty()) {
                return null;
            }

            return Long.parseLong(valore.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}