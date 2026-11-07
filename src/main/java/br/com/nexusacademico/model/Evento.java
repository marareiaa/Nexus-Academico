package br.com.nexusacademico.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Evento {

    private int idEvento;
    private int idOrganizador;
    private int idTipoEvento;

    private String titulo;
    private String descricao;
    private String local;

    private int vagas;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int cargaHoraria;

    private String modalidade;
    private String status;

    private LocalDateTime dataCriacao;

    public Evento(int idOrganizador, int idTipoEvento,
                  String titulo, String descricao, String local,
                  int vagas, LocalDate dataInicio, LocalDate dataFim,
                  int cargaHoraria, String modalidade) {

        this.idOrganizador = idOrganizador;
        this.idTipoEvento = idTipoEvento;
        this.titulo = titulo;
        this.descricao = descricao;
        this.local = local;
        this.vagas = vagas;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.cargaHoraria = cargaHoraria;
        this.modalidade = modalidade;
        this.status = "pendente";
        this.dataCriacao = LocalDateTime.now();
    }

    public int getIdEvento() {
        return idEvento;
    }

    public int getIdOrganizador() {
        return idOrganizador;
    }

    public int getIdTipoEvento() {
        return idTipoEvento;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLocal() {
        return local;
    }

    public int getVagas() {
        return vagas;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public String getModalidade() {
        return modalidade;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    // Envia o evento para avaliação do moderador (status "pendente").
    public void publicar() {
        if ("cancelado".equals(status) || "aprovado".equals(status)) {
            throw new IllegalStateException(
                    "Não é possível publicar um evento " + status + "."
            );
        }

        this.status = "pendente";
    }

    // Atualiza os dados do evento (antes chamado atualizarDados).
    public void atualizar(String titulo, String descricao,
                          String local, int vagas,
                          LocalDate dataInicio, LocalDate dataFim,
                          int cargaHoraria, String modalidade) {

        this.titulo = titulo;
        this.descricao = descricao;
        this.local = local;
        this.vagas = vagas;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.cargaHoraria = cargaHoraria;
        this.modalidade = modalidade;
    }

    public void aprovar() {
        if ("cancelado".equals(status)) {
            throw new IllegalStateException(
                    "Não é possível aprovar um evento cancelado."
            );
        }

        this.status = "aprovado";
    }

    public void rejeitar() {
        if ("cancelado".equals(status)) {
            throw new IllegalStateException(
                    "Não é possível rejeitar um evento cancelado."
            );
        }

        this.status = "rejeitado";
    }

    // Exige o valor 'cancelado' no ENUM evento.status do banco.
    public void cancelar() {
        this.status = "cancelado";
    }

    public boolean possuiVagas(int quantidadeInscritos) {
        return quantidadeInscritos < vagas;
    }
}