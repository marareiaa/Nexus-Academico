package br.com.nexusacademico.model;

import java.time.LocalDateTime;

public class Presenca {

    private int idPresenca;
    private int idInscricao;
    private int idEncontro;

    private boolean presente;
    private LocalDateTime dataRegistro;

    public Presenca(int idInscricao, int idEncontro, boolean presente) {

        if (idInscricao <= 0 || idEncontro <= 0) {
            throw new IllegalArgumentException(
                    "Inscrição e encontro devem ser válidos."
            );
        }

        this.idInscricao = idInscricao;
        this.idEncontro = idEncontro;
        this.presente = presente;
        this.dataRegistro = LocalDateTime.now();
    }

    public int getIdPresenca() {
        return idPresenca;
    }

    public void setIdPresenca(int idPresenca) {
        this.idPresenca = idPresenca;
    }

    public int getIdInscricao() {
        return idInscricao;
    }

    public int getIdEncontro() {
        return idEncontro;
    }

    public boolean isPresente() {
        return presente;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    // Corrige o registro (ex.: aluno marcado como ausente por engano).
    public void alterar(boolean presente) {
        this.presente = presente;
        this.dataRegistro = LocalDateTime.now();
    }
}