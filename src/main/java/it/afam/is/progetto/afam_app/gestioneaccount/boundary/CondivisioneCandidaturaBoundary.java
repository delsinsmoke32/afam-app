package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.boundary.DBMSBoundary;
import it.afam.is.progetto.afam_app.gestioneaccount.control.GenerazioneURLController;

public class CondivisioneCandidaturaBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;
    private final Long portfolio_id;

    private String urlGenerato;

    public CondivisioneCandidaturaBoundary(DBMSBoundary dbmsBoundary, Long portfolio_id) {
        this.dbmsBoundary = dbmsBoundary;
        this.portfolio_id = portfolio_id;
    }

    public void mostraPaginaCondivisione() {
        setTitle("Condivisione Candidatura");
        setSize(550, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        JLabel titolo = new JLabel("Condivisione Candidatura");

        JButton condividiPortfolioButton = new JButton("Condividi portfolio");
        condividiPortfolioButton.addActionListener(e -> cliccaCondividiPortfolio());

        panel.add(titolo);
        panel.add(condividiPortfolioButton);

        if (urlGenerato != null) {
            panel.add(new JLabel("URL generato: " + urlGenerato));

            JButton copiaButton = new JButton("Copia URL");
            copiaButton.addActionListener(e -> copiaURL());
            panel.add(copiaButton);
        }

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void cliccaCondividiPortfolio() {
        // <<create>> GenerazioneURLController
        GenerazioneURLController generazioneURLController =
                new GenerazioneURLController(this, dbmsBoundary);

        // mandaIdPortfolio(portfolio_id)
        generazioneURLController.mandaIdPortfolio(portfolio_id);
    }

    public void mostraUrlGenerato(String URL) {
        this.urlGenerato = URL;

        // Dopo mostraUrlGenerato(URL), il sequence mostra la pagina condivisione
        mostraPaginaCondivisione();
    }

    public void copiaURL() {
        if (urlGenerato == null) {
            return;
        }

        StringSelection selection = new StringSelection(urlGenerato);
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(selection, null);

        // mostraPaginaCondivisione()
        mostraPaginaCondivisione();
    }
}