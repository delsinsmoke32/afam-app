package it.afam.is.progetto.afam_app.autenticazione.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.autenticazione.controller.LoginController;

public class FormLoginBoundary extends JFrame {

    private final LoginController loginController;

    private JTextField mailField;
    private JPasswordField passwordField;

    private String mail;
    private String password;

    public FormLoginBoundary(LoginController loginController) {
        this.loginController = loginController;
    }

    public void MostraFormLogin() {
        setTitle("Login");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        mailField = new JTextField();
        passwordField = new JPasswordField();

        JButton okButton = new JButton("OK");

        panel.add(new JLabel("Mail"));
        panel.add(mailField);

        panel.add(new JLabel("Password"));
        panel.add(passwordField);

        panel.add(new JLabel(""));
        panel.add(okButton);

        okButton.addActionListener(e -> CliccaOk());

        setContentPane(panel);
        setVisible(true);
    }

    public void inserisciCredenziali(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    public void CliccaOk() {
        inserisciCredenziali(
                mailField.getText(),
                new String(passwordField.getPassword())
        );

        loginController.mandaCredenziale(mail, password);

        dispose();
    }
}



