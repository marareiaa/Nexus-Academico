package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.TipoEvento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoEventoDAO {

    public boolean cadastrar(TipoEvento tipo) {

        String sql = "INSERT INTO tipo_evento (nome, descricao) VALUES (?, ?)";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, tipo.getNome());
            comando.setString(2, tipo.getDescricao());
            comando.executeUpdate();

            try (ResultSet chaves = comando.getGeneratedKeys()) {
                if (chaves.next()) {
                    tipo.setIdTipoEvento(chaves.getInt(1));
                }
            }

            return true;

        } catch (SQLException erro) {
            System.out.println("Erro ao cadastrar tipo de evento: " + erro.getMessage());
            return false;
        }
    }

    public boolean atualizar(TipoEvento tipo) {

        String sql = """
                UPDATE tipo_evento SET nome = ?, descricao = ?
                WHERE id_tipo_evento = ?
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, tipo.getNome());
            comando.setString(2, tipo.getDescricao());
            comando.setInt(3, tipo.getIdTipoEvento());

            return comando.executeUpdate() > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar tipo de evento: " + erro.getMessage());
            return false;
        }
    }

    public TipoEvento buscarPorId(int idTipoEvento) {

        String sql = """
                SELECT id_tipo_evento, nome, descricao
                FROM tipo_evento WHERE id_tipo_evento = ?
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idTipoEvento);

            try (ResultSet r = comando.executeQuery()) {
                if (r.next()) {
                    return mapear(r);
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao buscar tipo de evento: " + erro.getMessage());
        }

        return null;
    }

    public boolean nomeExiste(String nome) {

        String sql = "SELECT 1 FROM tipo_evento WHERE nome = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, nome);

            try (ResultSet r = comando.executeQuery()) {
                return r.next();
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao consultar tipo de evento: " + erro.getMessage());
            return false;
        }
    }

    public List<TipoEvento> listarTodos() {

        String sql = """
                SELECT id_tipo_evento, nome, descricao
                FROM tipo_evento ORDER BY nome
                """;

        List<TipoEvento> tipos = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet r = comando.executeQuery()) {

            while (r.next()) {
                tipos.add(mapear(r));
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar tipos de evento: " + erro.getMessage());
        }

        return tipos;
    }

    private TipoEvento mapear(ResultSet r) throws SQLException {
        return new TipoEvento(
                r.getInt("id_tipo_evento"),
                r.getString("nome"),
                r.getString("descricao")
        );
    }
}