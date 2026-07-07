package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import it.afam.is.progetto.afam_app.gestioneaccount.control.PwdRecController;

public class FormRecPwdBoundary extends JFrame {

    private final PwdRecController pwdRecController;

    private JPasswordField passwordField;
    private JPasswordField confPasswordField;

    private String password;
    private String conf_password;

    public FormRecPwdBoundary(PwdRecController pwdRecController) {
        this.pwdRecController = pwdRecController;
    }

    public void mostraFormRecPwd() {
        setTitle("Nuova password");
        setSize(420, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));

        passwordField = new JPasswordField();
        confPasswordField = new JPasswordField();

        panel.add(new JLabel("Password"));
        panel.add(passwordField);

        panel.add(new JLabel("Conferma password"));
        panel.add(confPasswordField);

        JButton bottoneOK = new JButton("OK");

        bottoneOK.addActionListener(e -> {
            // inserisciCredenziali(password, conf_password)
            inserisciCredenziali(
                    new String(passwordField.getPassword()),
                    new String(confPasswordField.getPassword())
            );

            // CliccaOk()
            CliccaOk();
        });

        add(panel, BorderLayout.CENTER);
        add(bottoneOK, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void inserisciCredenziali(String password, String conf_password) {
        this.password = password;
        this.conf_password = conf_password;
    }

    public void CliccaOk() {
        // mandaPwd(password, conf_password)
        mandaPwd(password, conf_password);
    }

    public void mandaPwd(String password, String conf_password) {
        pwdRecController.mandaPwd(password, conf_password);
    }
}



