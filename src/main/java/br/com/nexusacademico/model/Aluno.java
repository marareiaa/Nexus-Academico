package br.com.nexusacademico.model;

import java.time.LocalDateTime;

public class Aluno extends Usuario {

    // Registro Acadêmico (coluna aluno.ra no banco).
    private String ra;

    // Dados acadêmicos utilizados pelo banco de dados.
    private String curso;
    private String periodo;
    private String turma;

    // Construtor para cadastrar um novo aluno.
    public Aluno(
            String nome,
            String email,
            String senhaHash,
            String ra,
            String curso,
            String periodo,
            String turma
    ) {
        super(nome, email, senhaHash);

        this.ra = ra;
        this.curso = curso;
        this.periodo = periodo;
        this.turma = turma;
    }

    // Construtor para recuperar um aluno do banco de dados.
    public Aluno(
            int idUsuario,
            String nome,
            String email,
            String senhaHash,
            LocalDateTime dataCadastro,
            String ra,
            String curso,
            String periodo,
            String turma
    ) {
        super(idUsuario, nome, email, senhaHash, dataCadastro);

        this.ra = ra;
        this.curso = curso;
        this.periodo = periodo;
        this.turma = turma;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getCurso() {
        return curso;
    }

    public String getPeriodo() {
        return periodo;
    }

    public String getTurma() {
        return turma;
    }

    // Atualiza os dados acadêmicos do aluno.
    public void atualizarDadosAcademicos(
            String curso,
            String periodo,
            String turma
    ) {
        this.curso = curso;
        this.periodo = periodo;
        this.turma = turma;
    }

    // Implementa a autenticação herdada de Usuario.
    @Override
    public boolean autenticar(String senhaInformada) {
        return super.autenticar(senhaInformada);
    }

    // Operação de visualização dos eventos disponíveis.
    public void visualizarEventos() {
        // A consulta e a apresentação serão realizadas
        // pelas camadas Controller, DAO e View.
    }

    // Solicita a inscrição do aluno em um evento.
    public void inscrever(int idEvento) {
        if (idEvento <= 0) {
            throw new IllegalArgumentException(
                    "O identificador do evento deve ser válido."
            );
        }

        // O registro da inscrição será realizado pelo DAO.
    }

    // Solicita o cancelamento de uma inscrição.
    public void cancelarInscricao() {
        // O cancelamento será realizado pelo Controller e DAO.
    }

    // Operação de visualização dos certificados do aluno.
    public void visualizarCertificados() {
        // A consulta e a apresentação serão realizadas
        // pelas camadas Controller, DAO e View.
    }
}