package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.gestioneaccount.control.LoginController;

public class FormOTPBoundary extends JFrame {

    private final LoginController loginController;

    private JTextField otpField;
    private String OTP;

    public FormOTPBoundary(LoginController loginController) {
        this.loginController = loginController;
    }

    public void mostraFormOTP() {
        setTitle("Verifica OTP");
        setSize(400, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        otpField = new JTextField();

        JButton okButton = new JButton("OK");

        panel.add(new JLabel("Codice OTP"));
        panel.add(otpField);

        panel.add(new JLabel(""));
        panel.add(okButton);

        okButton.addActionListener(e -> CliccaOk());

        setContentPane(panel);
        setVisible(true);
    }

    public void inserisciOTP(String OTP) {
        this.OTP = OTP;
    }

    public void CliccaOk() {
        inserisciOTP(otpField.getText());

        loginController.mandaOTP(OTP);

        dispose();
    }
}



