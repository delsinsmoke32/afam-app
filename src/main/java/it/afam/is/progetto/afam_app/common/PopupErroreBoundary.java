package it.afam.is.progetto.afam_app.common;

import javax.swing.JOptionPane;

import org.springframework.stereotype.Component;

@Component
public class PopupErroreBoundary {

    public void mostraPopupErrore() {
        mostraPopupErrore("Errore.");
    }

    public void mostraPopupErrore(String messaggio) {
        JOptionPane.showMessageDialog(
                null,
                messaggio,
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void mostraPopup(String testo) {
        JOptionPane.showMessageDialog(
                null,
                testo,
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void mostraPopupSuccesso(String messaggio) {
        JOptionPane.showMessageDialog(
                null,
                messaggio,
                "Operazione completata",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}



