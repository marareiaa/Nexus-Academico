package br.com.nexusacademico.model;

import java.time.LocalDateTime;

public class ValidacaoCertificado {

    private int idValidacao;
    private int idCertificado;

    private LocalDateTime dataValidacao;
    private String ipOrigem;

    // Valores aceitos: "valido" ou "invalido".
    private String resultado;

    public ValidacaoCertificado(int idCertificado, String ipOrigem,
                                boolean valido) {

        if (ipOrigem != null && ipOrigem.length() > 45) {
            throw new IllegalArgumentException(
                    "O IP de origem deve ter no máximo 45 caracteres."
            );
        }

        this.idCertificado = idCertificado;
        this.ipOrigem = ipOrigem;
        this.resultado = valido ? "valido" : "invalido";
        this.dataValidacao = LocalDateTime.now();
    }

    public int getIdValidacao() {
        return idValidacao;
    }

    public void setIdValidacao(int idValidacao) {
        this.idValidacao = idValidacao;
    }

    public int getIdCertificado() {
        return idCertificado;
    }

    public LocalDateTime getDataValidacao() {
        return dataValidacao;
    }

    public String getIpOrigem() {
        return ipOrigem;
    }

    public String getResultado() {
        return resultado;
    }
}