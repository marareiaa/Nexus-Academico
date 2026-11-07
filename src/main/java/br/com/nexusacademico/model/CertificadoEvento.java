package br.com.nexusacademico.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class CertificadoEvento {

    private int idCertificado;
    private int idInscricao;

    private String codigoVerificacao;
    private LocalDateTime dataEmissao;
    private int cargaHoraria;
    private String url;
    private String status;

    public CertificadoEvento(int idInscricao, int cargaHoraria, String url) {

        if (idInscricao <= 0) {
            throw new IllegalArgumentException(
                    "A inscrição do certificado deve ser válida."
            );
        }

        if (cargaHoraria <= 0) {
            throw new IllegalArgumentException(
                    "A carga horária deve ser maior que zero."
            );
        }

        this.idInscricao = idInscricao;
        this.cargaHoraria = cargaHoraria;
        this.url = url;
    }

    public int getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(int idCertificado) {
        this.idCertificado = idCertificado;
    }

    public int getIdInscricao() {
        return idInscricao;
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public String getUrl() {
        return url;
    }

    // Valores possíveis: "emitido" ou "cancelado" (null antes de gerar).
    public String getStatus() {
        return status;
    }

    public void gerar() {

        if (codigoVerificacao != null) {
            throw new IllegalStateException(
                    "Este certificado já foi gerado."
            );
        }

        this.codigoVerificacao = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();

        this.dataEmissao = LocalDateTime.now();
        this.status = "emitido";
    }

    public void cancelar() {
        this.status = "cancelado";
    }

    public boolean validar(String codigo) {

        return "emitido".equals(status)
                && codigoVerificacao != null
                && codigo != null
                && codigoVerificacao.equalsIgnoreCase(codigo.trim());
    }
}