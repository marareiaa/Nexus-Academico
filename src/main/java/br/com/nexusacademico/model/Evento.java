package br.com.nexusacademico.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Evento {

    private static final List<String> MODALIDADES =
            List.of("presencial", "online", "hibrido");

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

    // Construtor para criar um novo evento (nasce "pendente").
    public Evento(int idOrganizador, int idTipoEvento,
                  String titulo, String descricao, String local,
                  int vagas, LocalDate dataInicio, LocalDate dataFim,
                  int cargaHoraria, String modalidade) {

        validarDados(titulo, local, vagas, dataInicio, dataFim,
                cargaHoraria, modalidade);

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

    // Construtor para recuperar um evento do banco de dados.
    public Evento(int idEvento, int idOrganizador, int idTipoEvento,
                  String titulo, String descricao, String local,
                  int vagas, LocalDate dataInicio, LocalDate dataFim,
                  int cargaHoraria, String modalidade,
                  String status, LocalDateTime dataCriacao) {

        this.idEvento = idEvento;
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
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    private static void validarDados(String titulo, String local, int vagas,
                                     LocalDate dataInicio, LocalDate dataFim,
                                     int cargaHoraria, String modalidade) {

        if (titulo == null || titulo.isBlank() || titulo.length() > 100) {
            throw new IllegalArgumentException(
                    "O título é obrigatório (até 100 caracteres)."
            );
        }

        if (local != null && local.length() > 100) {
            throw new IllegalArgumentException(
                    "O local deve ter no máximo 100 caracteres."
            );
        }

        if (vagas <= 0) {
            throw new IllegalArgumentException(
                    "O número de vagas deve ser maior que zero."
            );
        }

        if (dataInicio == null || dataFim == null
                || dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException(
                    "A data de fim não pode ser anterior à data de início."
            );
        }

        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException(
                    "A carga horária deve ser maior que zero."
            );
        }

        if (modalidade == null || !MODALIDADES.contains(modalidade)) {
            throw new IllegalArgumentException(
                    "Modalidade inválida. Use: presencial, online ou hibrido."
            );
        }
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

    // Envia (ou reenvia) o evento para avaliação do moderador.
    public void publicar() {
        if ("cancelado".equals(status) || "aprovado".equals(status)) {
            throw new IllegalStateException(
                    "Não é possível publicar um evento " + status + "."
            );
        }

        this.status = "pendente";
    }

    // Atualiza os dados do evento.
    public void atualizar(String titulo, String descricao,
                          String local, int vagas,
                          LocalDate dataInicio, LocalDate dataFim,
                          int cargaHoraria, String modalidade) {

        if ("cancelado".equals(status)) {
            throw new IllegalStateException(
                    "Um evento cancelado não pode ser editado."
            );
        }

        validarDados(titulo, local, vagas, dataInicio, dataFim,
                cargaHoraria, modalidade);

        this.titulo = titulo;
        this.descricao = descricao;
        this.local = local;
        this.vagas = vagas;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.cargaHoraria = cargaHoraria;
        this.modalidade = modalidade;
    }

    // Só eventos pendentes podem ser avaliados.
    public void aprovar() {
        exigirPendente();
        this.status = "aprovado";
    }

    public void rejeitar() {
        exigirPendente();
        this.status = "rejeitado";
    }

    private void exigirPendente() {
        if (!"pendente".equals(status)) {
            throw new IllegalStateException(
                    "Somente eventos pendentes podem ser avaliados "
                            + "(status atual: " + status + ")."
            );
        }
    }

    public void cancelar() {
        if ("cancelado".equals(status)) {
            throw new IllegalStateException("O evento já está cancelado.");
        }

        this.status = "cancelado";
    }

    public boolean possuiVagas(int quantidadeInscritos) {
        return quantidadeInscritos < vagas;
    }
}