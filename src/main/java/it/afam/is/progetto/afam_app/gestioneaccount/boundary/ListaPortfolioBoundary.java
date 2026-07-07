package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.control.CancellazionePortfolioController;
import it.afam.is.progetto.afam_app.gestioneaccount.control.VisualizzaPortfolioController;

public class ListaPortfolioBoundary extends JFrame {

    private VisualizzaPortfolioController visualizzaPortfolioController;
    private CancellazionePortfolioController cancellazionePortfolioController;

    public ListaPortfolioBoundary(VisualizzaPortfolioController visualizzaPortfolioController) {
        this.visualizzaPortfolioController = visualizzaPortfolioController;
    }

    public ListaPortfolioBoundary(CancellazionePortfolioController cancellazionePortfolioController) {
        this.cancellazionePortfolioController = cancellazionePortfolioController;
    }

    public void mostraListaPortfoli(List<PortfolioEntity> listaPortfoli) {
        setTitle("Lista Portfolio");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        for (PortfolioEntity portfolio : listaPortfoli) {
            JButton portfolioButton = new JButton(portfolio.getTitolo());

            portfolioButton.addActionListener(e -> selezionaPortfolio(portfolio.getId()));

            panel.add(portfolioButton);
        }

        setContentPane(panel);
        setVisible(true);
    }

    public void selezionaPortfolio(Long portfolio_id) {
        if (visualizzaPortfolioController != null) {
            visualizzaPortfolioController.recuperaPortfolio(portfolio_id);
        }

        if (cancellazionePortfolioController != null) {
            cancellazionePortfolioController.cancellaPortfolio(portfolio_id);
        }
    }
}