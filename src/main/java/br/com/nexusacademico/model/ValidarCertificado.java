package br.com.nexusacademico.model;

public class ValidarCertificado {

    // Verifica se o código corresponde a um certificado emitido.
    public boolean validarCodigo(String codigo) {
        // A consulta ao banco será feita futuramente por um DAO.
        return false;
    }

    // Retorna o certificado associado ao código informado.
    public CertificadoEvento consultarCertificado(String codigo) {
        // A consulta ao banco será feita futuramente por um DAO.
        return null;
    }
}