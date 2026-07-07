package it.afam.is.progetto.afam_app.gestioneaccount.boundary;

import java.util.HashMap;
import java.util.Map;

public class ProviderBoundary {

    public Map<String, String> reindirizzaAProvider(String provider_id) {
        // reindirizza()
        return reindirizza(provider_id);
    }

    public Map<String, String> reindirizza(String provider_id) {
        /*
         * IdentityProviderEsterno simulato.
         * Return: attestazione()
         */

        Map<String, String> attestazione = new HashMap<>();

        attestazione.put("valid", "true");
        attestazione.put("provider", provider_id);

        attestazione.put("nome", "Mario");
        attestazione.put("cognome", "Rossi");
        attestazione.put("email", "mario.rossi.spid@afam.it");
        attestazione.put("codiceFiscale", "RSSMRA00A01H501U");

        return attestazione;
    }
}

