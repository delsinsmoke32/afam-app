package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.SezioneEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.control.PortfolioCondivisoController;

public class PortfolioCondivisoBoundary extends JFrame {

    private final PortfolioCondivisoController portfolioCondivisoController;
    private final List<SezioneEntity> sezioniVisibili;
    private final List<AllegatoEntity> allegatiVisibili;

    private PortfolioEntity portfolio;

    public PortfolioCondivisoBoundary(
            PortfolioCondivisoController portfolioCondivisoController,
            List<SezioneEntity> sezioniVisibili,
            List<AllegatoEntity> allegatiVisibili
    ) {
        this.portfolioCondivisoController = portfolioCondivisoController;
        this.sezioniVisibili = sezioniVisibili;
        this.allegatiVisibili = allegatiVisibili;
    }

    public void mostraPortfolioCondiviso(PortfolioEntity portfolio) {
        this.portfolio = portfolio;

        setTitle("Portfolio Condiviso");
        setSize(750, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        if (portfolio == null) {
            panel.add(new JLabel("Portfolio non disponibile."));
            setContentPane(panel);
            setVisible(true);
            return;
        }

        panel.add(new JLabel("Portfolio: " + portfolio.getTitolo()));
        panel.add(new JLabel("Descrizione: " + portfolio.getDescrizione()));

        panel.add(new JLabel("Sezioni visibili:"));

        if (sezioniVisibili == null || sezioniVisibili.isEmpty()) {
            panel.add(new JLabel("Nessuna sezione visibile."));
        } else {
            for (SezioneEntity sezione : sezioniVisibili) {
                panel.add(new JLabel("Sezione: " + sezione.getTitolo()));
                panel.add(new JLabel("Testo: " + sezione.getCorpoTesto()));
            }
        }

        panel.add(new JLabel("File scaricabili:"));

        if (allegatiVisibili == null || allegatiVisibili.isEmpty()) {
            panel.add(new JLabel("Nessun file disponibile."));
        } else {
            for (AllegatoEntity allegato : allegatiVisibili) {
                String titolo = allegato.getTitoloOpera();

                if (titolo == null || titolo.trim().isEmpty()) {
                    titolo = "Allegato " + allegato.getId();
                }

                JButton scaricaButton = new JButton("Scarica: " + titolo);

                Long allegato_id = allegato.getId();

                // cliccaScarica()
                scaricaButton.addActionListener(e -> cliccaScarica(allegato_id));

                panel.add(scaricaButton);
            }
        }

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void mostraPortfolio() {
        mostraPortfolioCondiviso(portfolio);
    }

    public void cliccaScarica(Long allegato_id) {
        // controllaDownload(allegato_id)
        portfolioCondivisoController.controllaDownload(allegato_id);
    }

    public String apriSelettoreSalvataggio() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona cartella di salvataggio");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int risultato = fileChooser.showSaveDialog(this);

        if (risultato == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }

        return null;
    }
}