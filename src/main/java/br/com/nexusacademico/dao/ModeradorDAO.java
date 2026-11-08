package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.Moderador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModeradorDAO {

    // Cadastra o usuário e o perfil de moderador em uma única transação.
    public boolean cadastrar(Moderador moderador) {

        String sql = """
                INSERT INTO moderador (id_moderador, nivel_acesso)
                VALUES (?, ?)
                """;

        try (Connection conexao = Conexao.conectar()) {
            conexao.setAutoCommit(false);

            try {
                int idUsuario = UsuarioDAO.inserirUsuario(conexao, moderador);

                try (PreparedStatement comando = conexao.prepareStatement(sql)) {
                    comando.setInt(1, idUsuario);
                    comando.setString(2, moderador.getNivelAcesso());
                    comando.executeUpdate();
                }

                conexao.commit();
                moderador.setIdUsuario(idUsuario);

                return true;

            } catch (SQLException | RuntimeException erro) {
                conexao.rollback();
                System.out.println(
                        "Erro ao cadastrar moderador: " + erro.getMessage()
                );
                return false;
            }

        } catch (SQLException erro) {
            System.out.println("Erro na conexão com o banco: " + erro.getMessage());
            return false;
        }
    }

    // Informa se já existe algum moderador cadastrado.
    public boolean existeAlgum() {

        String sql = "SELECT 1 FROM moderador LIMIT 1";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            return resultado.next();

        } catch (SQLException erro) {
            System.out.println("Erro ao consultar moderadores: " + erro.getMessage());
            return true; // na dúvida, bloqueia o cadastro livre
        }
    }
}