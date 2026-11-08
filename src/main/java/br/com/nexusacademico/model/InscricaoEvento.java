package br.com.nexusacademico.model;

import java.time.LocalDateTime;

public class InscricaoEvento {

    private int idInscricao;
    private int idAluno;
    private int idEvento;

    private LocalDateTime dataInscricao;
    private String status;

    // Construtor para criar uma nova inscrição.
    public InscricaoEvento(int idAluno, int idEvento) {

        this.idAluno = idAluno;
        this.idEvento = idEvento;
        this.dataInscricao = LocalDateTime.now();
        this.status = "confirmada";
    }

    // Construtor para recuperar uma inscrição do banco de dados.
    public InscricaoEvento(int idInscricao, int idAluno, int idEvento,
                           LocalDateTime dataInscricao, String status) {

        this.idInscricao = idInscricao;
        this.idAluno = idAluno;
        this.idEvento = idEvento;
        this.dataInscricao = dataInscricao;
        this.status = status;
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

    // Só é possível cancelar uma inscrição confirmada.
    public void cancelar() {
        exigirConfirmada("cancelar");
        this.status = "cancelada";
    }

    // Aluno cumpriu a presença exigida e recebe o certificado.
    public void concluir() {
        exigirConfirmada("concluir");
        this.status = "concluida";
    }

    // Aluno não cumpriu a presença exigida.
    public void marcarAusente() {
        exigirConfirmada("marcar como ausente");
        this.status = "ausente";
    }

    private void exigirConfirmada(String acao) {
        if (!"confirmada".equals(status)) {
            throw new IllegalStateException(
                    "Não é possível " + acao + " uma inscrição "
                            + status + "."
            );
        }
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