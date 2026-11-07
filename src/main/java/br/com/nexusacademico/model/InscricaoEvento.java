package br.com.nexusacademico.model;

import java.time.LocalDateTime;

public class InscricaoEvento {

    private int idInscricao;
    private int idAluno;
    private int idEvento;

    private LocalDateTime dataInscricao;
    private String status;

    public InscricaoEvento(int idAluno, int idEvento) {

        this.idAluno = idAluno;
        this.idEvento = idEvento;
        this.dataInscricao = LocalDateTime.now();
        this.status = "confirmada";
    }

    public int getIdInscricao() {
        return idInscricao;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public LocalDateTime getDataInscricao() {
        return dataInscricao;
    }

    public String getStatus() {
        return status;
    }

    public void setIdInscricao(int idInscricao) {
        this.idInscricao = idInscricao;
    }

    public void confirmar() {
        this.status = "confirmada";
    }

    public void cancelar() {
        this.status = "cancelada";
    }

    // Registra a presença do aluno em um encontro do evento.
    // A presença é guardada na tabela presenca (uma por encontro).
    public Presenca marcarPresenca(int idEncontro) {

        if (!"confirmada".equals(this.status)) {
            throw new IllegalStateException(
                    "Não é possível registrar presença em uma inscrição não confirmada."
            );
        }

        if (idInscricao <= 0) {
            throw new IllegalStateException(
                    "A inscrição precisa estar salva antes de registrar presença."
            );
        }

        return new Presenca(idInscricao, idEncontro, true);
    }
}