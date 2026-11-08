package br.com.nexusacademico.model;

import br.com.nexusacademico.dao.CertificadoDAO;
import br.com.nexusacademico.dao.ValidacaoCertificadoDAO;

public class ValidarCertificado {

    private final CertificadoDAO certificadoDAO = new CertificadoDAO();
    private final ValidacaoCertificadoDAO validacaoDAO =
            new ValidacaoCertificadoDAO();

    // Verifica se o código corresponde a um certificado emitido.
    public boolean validarCodigo(String codigo) {
        return validarCodigo(codigo, "127.0.0.1");
    }

    // Igual a validarCodigo(codigo), registrando o IP de origem.
    // Só é possível registrar a consulta quando o código existe
    // (validacao_certificado exige um certificado).
    public boolean validarCodigo(String codigo, String ipOrigem) {

        CertificadoEvento certificado = consultarCertificado(codigo);

        if (certificado == null) {
            return false;
        }

        boolean valido = certificado.validar(codigo);

        validacaoDAO.registrar(new ValidacaoCertificado(
                certificado.getIdCertificado(), ipOrigem, valido
        ));

        return valido;
    }

    // Retorna o certificado associado ao código informado (ou null).
    public CertificadoEvento consultarCertificado(String codigo) {

        if (codigo == null || codigo.isBlank()) {
            return null;
        }

        return certificadoDAO.buscarPorCodigo(codigo.trim().toUpperCase());
    }
}