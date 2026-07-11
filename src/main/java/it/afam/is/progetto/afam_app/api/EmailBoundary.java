package it.afam.is.progetto.afam_app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailBoundary {

    @Autowired
    private JavaMailSender mailSender;

    public void InviaEmail(String destinatario, String oggetto, String corpo) {
        // mandaMail(destinatario, oggetto, corpo)
        mandaMail(destinatario, oggetto, corpo);
    }

    public void mandaMail(String destinatario, String oggetto, String corpo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // L'email del mittente (deve coincidere con username in application.properties)
            message.setFrom("LA_TUA_EMAIL@gmail.com");
            message.setTo(destinatario);
            message.setSubject(oggetto);
            message.setText(corpo);

            mailSender.send(message);

            System.out.println("✅ Email inviata con successo a: " + destinatario);
        } catch (Exception e) {
            System.err.println("❌ Errore durante l'invio dell'email: " + e.getMessage());
        }
    }
}