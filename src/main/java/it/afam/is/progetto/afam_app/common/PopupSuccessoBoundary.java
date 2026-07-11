package it.afam.is.progetto.afam_app.common;

import javax.swing.JOptionPane;

public class PopupSuccessoBoundary {

    public void mostraSuccesso() {
        JOptionPane.showMessageDialog(
                null,
                "Codice OTP inviato con successo.",
                "Successo",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void mostraPopup(String testo) {
        JOptionPane.showMessageDialog(
                null,
                testo,
                "Successo",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}



