package it.afam.is.progetto.afam_app.boundary;

import org.springframework.stereotype.Service;

@Service
public class EmailBoundary {

    public void InviaEmail(String destinatario, String oggetto, String corpo) {
        // mandaMail(destinatario, oggetto, corpo)
        mandaMail(destinatario, oggetto, corpo);
    }

    public void mandaMail(String destinatario, String oggetto, String corpo) {
        System.out.println("Email inviata a: " + destinatario);
        System.out.println("Oggetto: " + oggetto);
        System.out.println("Corpo: " + corpo);
    }
}



