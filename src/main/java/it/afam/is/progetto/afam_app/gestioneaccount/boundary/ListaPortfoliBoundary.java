package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.entity.PortfolioEntity;
import it.afam.is.progetto.afam_app.gestioneaccount.control.VisualizzaPortfolioController;

public class ListaPortfoliBoundary extends JFrame {

    private final VisualizzaPortfolioController visualizzaPortfolioController;

    public ListaPortfoliBoundary(VisualizzaPortfolioController visualizzaPortfolioController) {
        this.visualizzaPortfolioController = visualizzaPortfolioController;
    }

    public void mostraListaPortfoli(List<PortfolioEntity> listaPortfoli) {
        setTitle("Lista Portfolio");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Seleziona un portfolio:"));

        for (PortfolioEntity portfolio : listaPortfoli) {
            JButton portfolioButton = new JButton(portfolio.getTitolo());

            portfolioButton.addActionListener(e -> selezionaPortfolio(portfolio.getId()));

            panel.add(portfolioButton);
        }

        setContentPane(panel);
        setVisible(true);
    }

    public void selezionaPortfolio(Long portfolio_id) {
        // recuperaPortfolio(portfolio_id)
        visualizzaPortfolioController.recuperaPortfolio(portfolio_id);
    }
}