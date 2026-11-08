package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.ValidacaoCertificado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ValidacaoCertificadoDAO {

    public boolean registrar(ValidacaoCertificado validacao) {

        String sql = """
                INSERT INTO validacao_certificado
                    (id_certificado, data_validacao, ip_origem, resultado)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            c.setInt(1, validacao.getIdCertificado());
            c.setTimestamp(2, Timestamp.valueOf(validacao.getDataValidacao()));
            c.setString(3, validacao.getIpOrigem());
            c.setString(4, validacao.getResultado());

            c.executeUpdate();

            try (ResultSet chaves = c.getGeneratedKeys()) {
                if (chaves.next()) {
                    validacao.setIdValidacao(chaves.getInt(1));
                }
            }

            return true;

        } catch (SQLException erro) {
            System.out.println("Erro ao registrar validação: " + erro.getMessage());
            return false;
        }
    }
}