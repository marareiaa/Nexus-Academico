package br.com.nexusacademico.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class CertificadoEvento {

    private String codigoVerificacao;
    private LocalDateTime dataEmissao;
    private int cargaHoraria;
    private String url;

    public CertificadoEvento(int cargaHoraria, String url) {
        this.cargaHoraria = cargaHoraria;
        this.url = url;
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

    public void gerar() {
        this.codigoVerificacao = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        this.dataEmissao = LocalDateTime.now();
    }

    public boolean validar(String codigo) {
        return codigoVerificacao != null && codigoVerificacao.equalsIgnoreCase(codigo);
    }
}

