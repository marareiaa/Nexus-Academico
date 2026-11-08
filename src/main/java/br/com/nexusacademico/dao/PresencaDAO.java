package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.Presenca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PresencaDAO {

    // Grava a presença; se já existir registro para a inscrição e o
    // encontro (UNIQUE), apenas atualiza.
    public boolean registrar(Presenca presenca) {

        String sql = """
                INSERT INTO presenca
                    (id_inscricao, id_encontro, presente, data_registro)
                VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    presente = VALUES(presente),
                    data_registro = VALUES(data_registro)
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setInt(1, presenca.getIdInscricao());
            c.setInt(2, presenca.getIdEncontro());
            c.setBoolean(3, presenca.isPresente());
            c.setTimestamp(4, Timestamp.valueOf(presenca.getDataRegistro()));

            c.executeUpdate();

            return true;

        } catch (SQLException erro) {
            System.out.println("Erro ao registrar presença: " + erro.getMessage());
            return false;
        }
    }

    public List<Presenca> listarPorInscricao(int idInscricao) {

        String sql = """
                SELECT id_presenca, id_inscricao, id_encontro, presente,
                       data_registro
                FROM presenca WHERE id_inscricao = ? ORDER BY id_encontro
                """;

        List<Presenca> lista = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setInt(1, idInscricao);

            try (ResultSet r = c.executeQuery()) {
                while (r.next()) {
                    lista.add(new Presenca(
                            r.getInt("id_presenca"),
                            r.getInt("id_inscricao"),
                            r.getInt("id_encontro"),
                            r.getBoolean("presente"),
                            r.getTimestamp("data_registro").toLocalDateTime()
                    ));
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar presenças: " + erro.getMessage());
        }

        return lista;
    }

    // Quantidade de encontros em que o aluno esteve presente.
    public int contarPresencas(int idInscricao) {

        String sql = """
                SELECT COUNT(*) FROM presenca
                WHERE id_inscricao = ? AND presente = TRUE
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setInt(1, idInscricao);

            try (ResultSet r = c.executeQuery()) {
                return r.next() ? r.getInt(1) : 0;
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao contar presenças: " + erro.getMessage());
            return 0;
        }
    }

    // Informa se já há presença registrada para o encontro.
    public boolean existeParaEncontro(int idEncontro) {

        String sql = "SELECT 1 FROM presenca WHERE id_encontro = ? LIMIT 1";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setInt(1, idEncontro);

            try (ResultSet r = c.executeQuery()) {
                return r.next();
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao consultar presenças: " + erro.getMessage());
            return true;
        }
    }
}