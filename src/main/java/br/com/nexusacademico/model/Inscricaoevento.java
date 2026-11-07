package br.com.nexusacademico.model;

import java.time.LocalDateTime;

public class Inscricaoevento {
    private LocalDateTime dataInscricao;
    private String status;
    private boolean presenca;

    public InscricaoEvento() {
        this.dataInscricao = LocalDateTime.now();
        this.status = "confirmada";
        this.presenca = false;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public String getStatus() {
        return status;
    }

    public boolean isPresenca() {
        return presenca;
    }

    public void confirmar() {
        this.status = "confirmada";
    }

    public void cancelar() {
        this.status = "cancelada";
    }

    public void marcaPresenca() {
        this.presenca = true;
    }

}
