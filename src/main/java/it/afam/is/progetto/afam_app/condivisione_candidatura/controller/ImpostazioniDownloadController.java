package it.afam.is.progetto.afam_app.condivisione_candidatura.controller;

import it.afam.is.progetto.afam_app.api.DBMSBoundary;
import it.afam.is.progetto.afam_app.condivisione_candidatura.boundary.CondivisioneCandidaturaBoundary;
import it.afam.is.progetto.afam_app.condivisione_candidatura.boundary.ImpostazioniDownloadBoundary;

public class ImpostazioniDownloadController {

    private final CondivisioneCandidaturaBoundary condivisioneCandidaturaBoundary;
    private final DBMSBoundary dbmsBoundary;

    private ImpostazioniDownloadBoundary impostazioniDownloadBoundary;

    private Long portfolio_id;

    public ImpostazioniDownloadController(
            CondivisioneCandidaturaBoundary condivisioneCandidaturaBoundary,
            DBMSBoundary dbmsBoundary
    ) {
        this.condivisioneCandidaturaBoundary = condivisioneCandidaturaBoundary;
        this.dbmsBoundary = dbmsBoundary;
    }

    public void avviaImpostazioniDownload(Long portfolio_id) {
        this.portfolio_id = portfolio_id;

        // <<create>> ImpostazioniDownloadBoundary
        impostazioniDownloadBoundary = new ImpostazioniDownloadBoundary(this);

        // recuperaImpostazioneDownload(portfolio_id)
        Boolean impostazioneDownload = dbmsBoundary.recuperaImpostazioneDownload(portfolio_id);

        // mostraImpostazioneCorrente(impostazione)
        impostazioniDownloadBoundary.mostraImpostazioneCorrente(impostazioneDownload);
    }

    public void mandaImpostazione(Boolean impostazione) {
        // salvaImpostazione(impostazione)
        salvaImpostazione(impostazione);
    }

    public void salvaImpostazione(Boolean impostazione) {
        // salvaImpostazione(impostazione)
        dbmsBoundary.salvaImpostazione(portfolio_id, impostazione);

        if (impostazioniDownloadBoundary != null) {
            impostazioniDownloadBoundary.dispose();
            impostazioniDownloadBoundary = null;
        }

        // mostraPaginaCondivisione()
        condivisioneCandidaturaBoundary.mostraPaginaCondivisione();
    }
}