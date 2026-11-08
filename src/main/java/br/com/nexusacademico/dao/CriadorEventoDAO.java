package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.CriadorEvento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CriadorEventoDAO {

    // Cadastra o usuário e o perfil de organizador em uma única transação.
    public boolean cadastrar(CriadorEvento criador) {

        String sql = """
                INSERT INTO organizador (id_organizador, vinculo, cargo)
                VALUES (?, ?, ?)
                """;

        try (Connection conexao = Conexao.conectar()) {
            conexao.setAutoCommit(false);

            try {
                int idUsuario = UsuarioDAO.inserirUsuario(conexao, criador);

                try (PreparedStatement comando = conexao.prepareStatement(sql)) {
                    comando.setInt(1, idUsuario);
                    comando.setString(2, criador.getVinculo());
                    comando.setString(3, criador.getCargo());
                    comando.executeUpdate();
                }

                conexao.commit();
                criador.setIdUsuario(idUsuario);

                return true;

            } catch (SQLException | RuntimeException erro) {
                conexao.rollback();
                System.out.println(
                        "Erro ao cadastrar criador de evento: " + erro.getMessage()
                );
                return false;
            }

        } catch (SQLException erro) {
            System.out.println("Erro na conexão com o banco: " + erro.getMessage());
            return false;
        }
    }
}