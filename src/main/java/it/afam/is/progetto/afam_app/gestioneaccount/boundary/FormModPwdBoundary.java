package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import it.afam.is.progetto.afam_app.gestioneaccount.control.ModificaPwdController;

public class FormModPwdBoundary extends JFrame {

    private final ModificaPwdController modificaPwdController;

    private JPasswordField vecchiaPwdField;
    private JPasswordField nuovaPwdField;
    private JPasswordField confPwdField;

    private String vecchia_pwd;
    private String nuova_pwd;
    private String conf_pwd;

    public FormModPwdBoundary(ModificaPwdController modificaPwdController) {
        this.modificaPwdController = modificaPwdController;
    }

    public void mostraForm() {
        setTitle("Modifica Password");
        setSize(450, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        vecchiaPwdField = new JPasswordField();
        nuovaPwdField = new JPasswordField();
        confPwdField = new JPasswordField();

        JButton salvaButton = new JButton("Salva");

        panel.add(new JLabel("Vecchia password"));
        panel.add(vecchiaPwdField);

        panel.add(new JLabel("Nuova password"));
        panel.add(nuovaPwdField);

        panel.add(new JLabel("Conferma password"));
        panel.add(confPwdField);

        panel.add(new JLabel(""));
        panel.add(salvaButton);

        salvaButton.addActionListener(e -> cliccaSalva());

        setContentPane(panel);
        setVisible(true);
    }

    public void inserisciDati(String vecchia_pwd, String nuova_pwd, String conf_pwd) {
        this.vecchia_pwd = vecchia_pwd;
        this.nuova_pwd = nuova_pwd;
        this.conf_pwd = conf_pwd;
    }

    public void cliccaSalva() {
        inserisciDati(
                new String(vecchiaPwdField.getPassword()),
                new String(nuovaPwdField.getPassword()),
                new String(confPwdField.getPassword())
        );

        modificaPwdController.mandaDati(vecchia_pwd, nuova_pwd, conf_pwd);
    }

    public void chiudiForm() {
        dispose();
    }
}



