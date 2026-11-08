package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.EncontroEvento;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class EncontroEventoDAO {

    public boolean cadastrar(EncontroEvento encontro) {

        String sql = """
                INSERT INTO encontro_evento
                    (id_evento, data_encontro, hora_inicio, hora_fim)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            c.setInt(1, encontro.getIdEvento());
            c.setDate(2, Date.valueOf(encontro.getDataEncontro()));
            c.setTime(3, Time.valueOf(encontro.getHoraInicio()));
            c.setTime(4, Time.valueOf(encontro.getHoraFim()));

            c.executeUpdate();

            try (ResultSet chaves = c.getGeneratedKeys()) {
                if (chaves.next()) {
                    encontro.setIdEncontro(chaves.getInt(1));
                }
            }

            return true;

        } catch (SQLException erro) {
            System.out.println("Erro ao cadastrar encontro: " + erro.getMessage());
            return false;
        }
    }

    public EncontroEvento buscarPorId(int idEncontro) {

        String sql = """
                SELECT id_encontro, id_evento, data_encontro, hora_inicio, hora_fim
                FROM encontro_evento WHERE id_encontro = ?
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setInt(1, idEncontro);

            try (ResultSet r = c.executeQuery()) {
                if (r.next()) {
                    return mapear(r);
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao buscar encontro: " + erro.getMessage());
        }

        return null;
    }

    public List<EncontroEvento> listarPorEvento(int idEvento) {

        String sql = """
                SELECT id_encontro, id_evento, data_encontro, hora_inicio, hora_fim
                FROM encontro_evento WHERE id_evento = ?
                ORDER BY data_encontro, hora_inicio
                """;

        List<EncontroEvento> encontros = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setInt(1, idEvento);

            try (ResultSet r = c.executeQuery()) {
                while (r.next()) {
                    encontros.add(mapear(r));
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar encontros: " + erro.getMessage());
        }

        return encontros;
    }

    public boolean excluir(int idEncontro) {

        String sql = "DELETE FROM encontro_evento WHERE id_encontro = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setInt(1, idEncontro);

            return c.executeUpdate() > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao excluir encontro: " + erro.getMessage());
            return false;
        }
    }

    private EncontroEvento mapear(ResultSet r) throws SQLException {
        return new EncontroEvento(
                r.getInt("id_encontro"),
                r.getInt("id_evento"),
                r.getDate("data_encontro").toLocalDate(),
                r.getTime("hora_inicio").toLocalTime(),
                r.getTime("hora_fim").toLocalTime()
        );
    }
}