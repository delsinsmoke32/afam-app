package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;

public class VisualizzazionePortfolioPubblicoBoundary extends JFrame {

    public void mostraPortfolio(PortfolioEntity portfolio) {
        setTitle("Portfolio Pubblico");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Portfolio pubblico"));

        if (portfolio == null) {
            panel.add(new JLabel("Portfolio non trovato."));
            setContentPane(panel);
            setVisible(true);
            return;
        }

        String titolo = portfolio.getTitolo() != null ? portfolio.getTitolo() : "";
        String descrizione = portfolio.getDescrizione() != null ? portfolio.getDescrizione() : "";

        panel.add(new JLabel("Titolo: " + titolo));
        panel.add(new JLabel("Descrizione: " + descrizione));

        if (portfolio.getLicenza() != null) {
            String nomeLicenza = portfolio.getLicenza().getNome() != null
                    ? portfolio.getLicenza().getNome()
                    : "";

            panel.add(new JLabel("Licenza: " + nomeLicenza));
        }

        panel.add(new JLabel("Sezioni pubbliche:"));

        List<SezioneEntity> sezioni = portfolio.getSezioni();

        if (sezioni == null || sezioni.isEmpty()) {
            panel.add(new JLabel("Nessuna sezione pubblica disponibile."));
        } else {
            sezioni.sort(Comparator.comparing(
                    sezione -> sezione.getOrdineVisualizzazione() != null
                            ? sezione.getOrdineVisualizzazione()
                            : 0
            ));

            for (SezioneEntity sezione : sezioni) {
                if (sezione == null) {
                    continue;
                }

                String titoloSezione = sezione.getTitolo() != null ? sezione.getTitolo() : "";
                String corpoTesto = sezione.getCorpoTesto() != null ? sezione.getCorpoTesto() : "";

                panel.add(new JLabel("Sezione: " + titoloSezione));
                panel.add(new JLabel("Descrizione: " + corpoTesto));
            }
        }

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }
}