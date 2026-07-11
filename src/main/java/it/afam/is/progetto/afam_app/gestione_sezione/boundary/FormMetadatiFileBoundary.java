package it.afam.is.progetto.afam_app.gestione_sezione.boundary;

import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import it.afam.is.progetto.afam_app.entity.AllegatoEntity;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.GestioneAllegatiController;
import it.afam.is.progetto.afam_app.gestione_sezione.controller.ModificaMetadatiController;

public class FormMetadatiFileBoundary extends JFrame {

    private GestioneAllegatiController gestioneAllegatiController;
    private ModificaMetadatiController modificaMetadatiController;

    private JTextField titoloField;
    private JTextField autoriField;
    private JTextField descrizioneField;
    private JTextField dataCreazioneField;

    public FormMetadatiFileBoundary(GestioneAllegatiController gestioneAllegatiController) {
        this.gestioneAllegatiController = gestioneAllegatiController;
    }

    public FormMetadatiFileBoundary(ModificaMetadatiController modificaMetadatiController) {
        this.modificaMetadatiController = modificaMetadatiController;
    }

    public void mostraForm() {
        setTitle("Metadati File");
        costruisciForm(false);

        JButton salvaButton = new JButton("Salva");
        salvaButton.addActionListener(e -> cliccaSalva());

        aggiungiBottoneSalva(salvaButton);
    }

    public void mostraFormMod(AllegatoEntity metadati) {
        setTitle("Modifica Metadati File");
        costruisciForm(true);

        if (metadati != null) {
            modificaMetadati(
                    metadati.getTitoloOpera(),
                    metadati.getDescrizioneBreve(),
                    metadati.getAutoreOpera()
            );
        }

        JButton salvaButton = new JButton("Salva");
        salvaButton.addActionListener(e -> cliccaSalvaModifica());

        aggiungiBottoneSalva(salvaButton);
    }

    private void costruisciForm(boolean modifica) {
        setSize(500, modifica ? 230 : 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(modifica ? 4 : 5, 2));

        titoloField = new JTextField();
        autoriField = new JTextField();
        descrizioneField = new JTextField();
        dataCreazioneField = new JTextField();

        panel.add(new JLabel("Titolo:"));
        panel.add(titoloField);

        panel.add(new JLabel("Autori:"));
        panel.add(autoriField);

        panel.add(new JLabel("Descrizione:"));
        panel.add(descrizioneField);

        if (!modifica) {
            panel.add(new JLabel("Data creazione yyyy-MM-dd:"));
            panel.add(dataCreazioneField);
        }

        setContentPane(panel);
        setVisible(true);
    }

    private void aggiungiBottoneSalva(JButton salvaButton) {
        JPanel panel = (JPanel) getContentPane();

        panel.add(new JLabel());
        panel.add(salvaButton);

        revalidate();
        repaint();
    }

    public void inserisciMetadati(String titolo, String autori, String descrizione, LocalDate dataCreazione) {
        titoloField.setText(titolo);
        autoriField.setText(autori);
        descrizioneField.setText(descrizione);

        if (dataCreazione != null) {
            dataCreazioneField.setText(dataCreazione.toString());
        }
    }

    public void modificaMetadati(String titolo, String descrizione, String autori) {
        titoloField.setText(titolo);
        descrizioneField.setText(descrizione);
        autoriField.setText(autori);
    }

    public void cliccaSalva() {
        String titolo = titoloField.getText();
        String autori = autoriField.getText();
        String descrizione = descrizioneField.getText();
        LocalDate dataCreazione = leggiDataCreazione();

        // mandaDati(titolo, autori, descrizione, dataCreazione)
        gestioneAllegatiController.mandaDati(titolo, autori, descrizione, dataCreazione);

        dispose();
    }

    public void cliccaSalvaModifica() {
        String titolo = titoloField.getText();
        String descrizione = descrizioneField.getText();
        String autori = autoriField.getText();

        // mandaDati(titolo, descrizione, autori)
        modificaMetadatiController.mandaDati(titolo, descrizione, autori);

        dispose();
    }

    private LocalDate leggiDataCreazione() {
        String valore = dataCreazioneField.getText();

        if (valore == null || valore.trim().isEmpty()) {
            return LocalDate.now();
        }

        try {
            return LocalDate.parse(valore.trim());
        } catch (Exception e) {
            return LocalDate.now();
        }
    }
}