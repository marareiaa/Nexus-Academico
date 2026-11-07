package br.com.nexusacademico.model;

import java.time.LocalDateTime;

public class AvaliacaoEvento {

    private int idAvaliacao;
    private int idModerador;
    private int idEvento;

    // Valores aceitos: "aprovado" ou "rejeitado".
    private String decisao;
    private LocalDateTime dataAvaliacao;

    public AvaliacaoEvento(int idModerador, int idEvento, String decisao) {

        if (!"aprovado".equals(decisao) && !"rejeitado".equals(decisao)) {
            throw new IllegalArgumentException(
                    "Decisão inválida. Use: aprovado ou rejeitado."
            );
        }

        this.idModerador = idModerador;
        this.idEvento = idEvento;
        this.decisao = decisao;
        this.dataAvaliacao = LocalDateTime.now();
    }

    public int getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(int idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public int getIdModerador() {
        return idModerador;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public String getDecisao() {
        return decisao;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }
}