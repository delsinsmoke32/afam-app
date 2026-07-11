package it.afam.is.progetto.afam_app.consult_cat_est.boundary;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.entity.StudenteEntity;
import it.afam.is.progetto.afam_app.consult_cat_est.controller.VisualizzazionePortfolioPubblicoController;

public class PaginaRicercaBoundary extends JFrame {

    private final DBMSBoundary dbmsBoundary;

    public PaginaRicercaBoundary(DBMSBoundary dbmsBoundary) {
        this.dbmsBoundary = dbmsBoundary;
    }

    public void mostraPaginaRicerca(List<StudenteEntity> risultati) {
        setTitle("Risultati ricerca studenti");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Risultati ricerca:"));

        if (risultati == null || risultati.isEmpty()) {
            panel.add(new JLabel("Nessuno studente trovato."));
        } else {
            for (StudenteEntity studente : risultati) {
                if (studente == null) {
                    continue;
                }

                String nome = studente.getNome() != null ? studente.getNome() : "";
                String cognome = studente.getCognome() != null ? studente.getCognome() : "";
                String corso = studente.getCorsoDiStudi() != null ? studente.getCorsoDiStudi() : "";

                panel.add(new JLabel(nome + " " + cognome + " - " + corso));

                JButton visualizzaButton = new JButton("Visualizza portfolio pubblico");
                visualizzaButton.addActionListener(e -> cliccaVisualizza(studente));

                panel.add(visualizzaButton);
            }
        }

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void cliccaVisualizza(StudenteEntity studente) {
        Long portfolio_id = recuperaPrimoPortfolioId(studente);

        // <<create>> VisualizzazionePortfolioPubblicoController
        VisualizzazionePortfolioPubblicoController controller =
                new VisualizzazionePortfolioPubblicoController(this, dbmsBoundary);

        // mandaIdPortfolio(portfolio_id)
        controller.mandaIdPortfolio(portfolio_id);
    }

    private Long recuperaPrimoPortfolioId(StudenteEntity studente) {
        if (studente == null || studente.getId() == null) {
            return null;
        }

        List<PortfolioEntity> portfoli = dbmsBoundary.recuperaListaPortfoli(studente.getId());

        if (portfoli == null || portfoli.isEmpty()) {
            return null;
        }

        PortfolioEntity portfolio = portfoli.get(0);

        if (portfolio == null) {
            return null;
        }

        return portfolio.getId();
    }
}