package it.afam.is.progetto.afam_app.condivisione_candidatura.boundary;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.condivisione_candidatura.controller.GenerazioneURLController;
import it.afam.is.progetto.afam_app.condivisione_candidatura.controller.ImpostazioniDownloadController;

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
        setSize(600, 340);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        JLabel titolo = new JLabel("Condivisione Candidatura");

        JButton condividiPortfolioButton = new JButton("Condividi portfolio");
        condividiPortfolioButton.addActionListener(e -> cliccaCondividiPortfolio());

        JButton impostazioniDownloadButton = new JButton("Impostazioni download");
        impostazioniDownloadButton.addActionListener(e -> cliccaImpostazioniDownload());

        panel.add(titolo);
        panel.add(condividiPortfolioButton);
        panel.add(impostazioniDownloadButton);

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

    public void cliccaImpostazioniDownload() {
        // <<create>> ImpostazioniDownloadController
        ImpostazioniDownloadController impostazioniDownloadController =
                new ImpostazioniDownloadController(this, dbmsBoundary);

        // avviaImpostazioniDownload(portfolio_id)
        impostazioniDownloadController.avviaImpostazioniDownload(portfolio_id);
    }

    public void mostraUrlGenerato(String URL) {
        this.urlGenerato = URL;

        // mostraPaginaCondivisione()
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