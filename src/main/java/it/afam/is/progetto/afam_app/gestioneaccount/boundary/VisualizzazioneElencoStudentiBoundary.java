package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.afam.is.progetto.afam_app.entity.StudenteEntity;

public class VisualizzazioneElencoStudentiBoundary extends JFrame {

    public void mostraElencoStudenti(List<StudenteEntity> elencoStudenti) {
        setTitle("Elenco Studenti");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Elenco studenti:"));

        for (StudenteEntity studente : elencoStudenti) {
            if (studente == null) {
                continue;
            }

            String nome = studente.getNome() != null ? studente.getNome() : "";
            String cognome = studente.getCognome() != null ? studente.getCognome() : "";
            String corso = studente.getCorsoDiStudi() != null ? studente.getCorsoDiStudi() : "";

            panel.add(new JLabel(nome + " " + cognome + " - " + corso));
        }

        setContentPane(panel);
        revalidate();
        repaint();
        setVisible(true);
    }
}