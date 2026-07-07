package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.gestioneaccount.control.PwdRecController;

public class PwdRecBoundary extends JFrame {

    private final PwdRecController pwdRecController;

    private JTextField mailField;
    private JTextField otpField;

    private String mail;
    private String OTP;

    public PwdRecBoundary(PwdRecController pwdRecController) {
        this.pwdRecController = pwdRecController;
    }

    public void mostraPwdRec() {
        setTitle("Recupero password");
        setSize(400, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(1, 2, 8, 8));

        mailField = new JTextField();

        panel.add(new JLabel("Email"));
        panel.add(mailField);

        JButton bottoneOK = new JButton("OK");

        bottoneOK.addActionListener(e -> {
            // inserisciMail(mail)
            inserisciMail(mailField.getText());

            // cliccaOK()
            cliccaOK();
        });

        add(panel, BorderLayout.CENTER);
        add(bottoneOK, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void inserisciMail(String mail) {
        this.mail = mail;
    }

    public void cliccaOK() {
        // richiediReset(mail)
        richiediReset(mail);
    }

    public void richiediReset(String mail) {
        pwdRecController.richiediReset(mail);
    }

    public void mostraFormOTP() {
        setTitle("Inserisci OTP");
        setSize(400, 180);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(1, 2, 8, 8));

        otpField = new JTextField();

        panel.add(new JLabel("OTP"));
        panel.add(otpField);

        JButton bottoneOK = new JButton("OK");

        bottoneOK.addActionListener(e -> {
            // inserisciOTP(OTP)
            inserisciOTP(otpField.getText());

            // cliccaOK()
            cliccaOKOTP();
        });

        setContentPane(new JPanel(new BorderLayout()));
        add(panel, BorderLayout.CENTER);
        add(bottoneOK, BorderLayout.SOUTH);

        revalidate();
        repaint();
        setVisible(true);
    }

    public void inserisciOTP(String OTP) {
        this.OTP = OTP;
    }

    public void cliccaOKOTP() {
        // mandaOTP(OTP)
        mandaOTP(OTP);
    }

    public void mandaOTP(String OTP) {
        pwdRecController.mandaOTP(OTP);
    }
}

