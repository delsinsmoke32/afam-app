package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.gestioneaccount.control.SPIDController;

public class FormSPIDBoundary extends JFrame {

    private final SPIDController spidController;

    private JTextField CdSField;
    private JTextField bioField;
    private JTextField linkField;

    private String CdS;
    private String bio;
    private String link;

    public FormSPIDBoundary(SPIDController spidController) {
        this.spidController = spidController;
    }

    public void mostraForm() {
        setTitle("Dati aggiuntivi SPID/eIDAS");
        setSize(450, 240);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));

        CdSField = new JTextField();
        bioField = new JTextField();
        linkField = new JTextField();

        panel.add(new JLabel("CdS"));
        panel.add(CdSField);

        panel.add(new JLabel("Biografia"));
        panel.add(bioField);

        panel.add(new JLabel("Link esterno"));
        panel.add(linkField);

        JButton bottoneOK = new JButton("OK");

        bottoneOK.addActionListener(e -> {
            // compilaCampi(CdS, biografia, link_esterno)
            compilaCampi(
                    CdSField.getText(),
                    bioField.getText(),
                    linkField.getText()
            );

            // cliccaOK()
            cliccaOK();
        });

        add(panel, BorderLayout.CENTER);
        add(bottoneOK, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void compilaCampi(String CdS, String biografia, String link_esterno) {
        this.CdS = CdS;
        this.bio = biografia;
        this.link = link_esterno;
    }

    public void cliccaOK() {
        // mandaDatiAgg(CdS, bio, link)
        mandaDatiAgg(CdS, bio, link);
    }

    public void mandaDatiAgg(String CdS, String bio, String link) {
        spidController.mandaDatiAgg(CdS, bio, link);
    }
}



