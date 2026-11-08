package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.Evento;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    private static final String COLUNAS = """
            id_evento, id_organizador, id_tipo_evento, titulo, descricao,
            `local`, vagas, data_inicio, data_fim, carga_horaria,
            modalidade, status, data_criacao
            """;

    public boolean cadastrar(Evento evento) {

        String sql = """
                INSERT INTO evento (id_organizador, id_tipo_evento, titulo,
                    descricao, `local`, vagas, data_inicio, data_fim,
                    carga_horaria, modalidade, status, data_criacao)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            c.setInt(1, evento.getIdOrganizador());
            c.setInt(2, evento.getIdTipoEvento());
            c.setString(3, evento.getTitulo());
            c.setString(4, evento.getDescricao());
            c.setString(5, evento.getLocal());
            c.setInt(6, evento.getVagas());
            c.setDate(7, Date.valueOf(evento.getDataInicio()));
            c.setDate(8, Date.valueOf(evento.getDataFim()));
            c.setInt(9, evento.getCargaHoraria());
            c.setString(10, evento.getModalidade());
            c.setString(11, evento.getStatus());
            c.setTimestamp(12, Timestamp.valueOf(evento.getDataCriacao()));

            c.executeUpdate();

            try (ResultSet chaves = c.getGeneratedKeys()) {
                if (chaves.next()) {
                    evento.setIdEvento(chaves.getInt(1));
                }
            }

            return true;

        } catch (SQLException erro) {
            System.out.println("Erro ao cadastrar evento: " + erro.getMessage());
            return false;
        }
    }

    // Atualiza os dados editáveis (o status tem métodos próprios).
    public boolean atualizar(Evento evento) {

        String sql = """
                UPDATE evento SET titulo = ?, descricao = ?, `local` = ?,
                    vagas = ?, data_inicio = ?, data_fim = ?,
                    carga_horaria = ?, modalidade = ?
                WHERE id_evento = ?
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setString(1, evento.getTitulo());
            c.setString(2, evento.getDescricao());
            c.setString(3, evento.getLocal());
            c.setInt(4, evento.getVagas());
            c.setDate(5, Date.valueOf(evento.getDataInicio()));
            c.setDate(6, Date.valueOf(evento.getDataFim()));
            c.setInt(7, evento.getCargaHoraria());
            c.setString(8, evento.getModalidade());
            c.setInt(9, evento.getIdEvento());

            return c.executeUpdate() > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar evento: " + erro.getMessage());
            return false;
        }
    }

    public boolean atualizarStatus(int idEvento, String status) {

        String sql = "UPDATE evento SET status = ? WHERE id_evento = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setString(1, status);
            c.setInt(2, idEvento);

            return c.executeUpdate() > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar status do evento: " + erro.getMessage());
            return false;
        }
    }

    // Cancela o evento e todas as inscrições confirmadas dele.
    public boolean cancelar(int idEvento) {

        try (Connection conexao = Conexao.conectar()) {
            conexao.setAutoCommit(false);

            try {
                try (PreparedStatement c = conexao.prepareStatement(
                        "UPDATE evento SET status = 'cancelado' WHERE id_evento = ?")) {
                    c.setInt(1, idEvento);
                    c.executeUpdate();
                }

                try (PreparedStatement c = conexao.prepareStatement("""
                        UPDATE inscricao SET status = 'cancelada'
                        WHERE id_evento = ? AND status = 'confirmada'
                        """)) {
                    c.setInt(1, idEvento);
                    c.executeUpdate();
                }

                conexao.commit();
                return true;

            } catch (SQLException | RuntimeException erro) {
                conexao.rollback();
                System.out.println("Erro ao cancelar evento: " + erro.getMessage());
                return false;
            }

        } catch (SQLException erro) {
            System.out.println("Erro na conexão com o banco: " + erro.getMessage());
            return false;
        }
    }

    public Evento buscarPorId(int idEvento) {

        List<Evento> eventos = consultar("WHERE id_evento = ?", idEvento);

        return eventos.isEmpty() ? null : eventos.get(0);
    }

    public List<Evento> listarPorOrganizador(int idOrganizador) {
        return consultar("WHERE id_organizador = ? ORDER BY data_inicio",
                idOrganizador);
    }

    public List<Evento> listarPorStatus(String status) {
        return consultar("WHERE status = ? ORDER BY data_criacao", status);
    }

    // Eventos aprovados que ainda não terminaram.
    public List<Evento> listarDisponiveis() {
        return consultar("""
                WHERE status = 'aprovado' AND data_fim >= CURDATE()
                ORDER BY data_inicio
                """);
    }

    // Quantidade de vagas ocupadas (inscrições não canceladas).
    public int contarInscritos(int idEvento) {

        String sql = """
                SELECT COUNT(*) FROM inscricao
                WHERE id_evento = ? AND status <> 'cancelada'
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setInt(1, idEvento);

            try (ResultSet r = c.executeQuery()) {
                return r.next() ? r.getInt(1) : 0;
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao contar inscritos: " + erro.getMessage());
            return 0;
        }
    }

    private List<Evento> consultar(String complemento, Object... parametros) {

        String sql = "SELECT " + COLUNAS + " FROM evento " + complemento;
        List<Evento> eventos = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            for (int i = 0; i < parametros.length; i++) {
                c.setObject(i + 1, parametros[i]);
            }

            try (ResultSet r = c.executeQuery()) {
                while (r.next()) {
                    eventos.add(mapear(r));
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao consultar eventos: " + erro.getMessage());
        }

        return eventos;
    }

    private Evento mapear(ResultSet r) throws SQLException {
        return new Evento(
                r.getInt("id_evento"),
                r.getInt("id_organizador"),
                r.getInt("id_tipo_evento"),
                r.getString("titulo"),
                r.getString("descricao"),
                r.getString("local"),
                r.getInt("vagas"),
                r.getDate("data_inicio").toLocalDate(),
                r.getDate("data_fim").toLocalDate(),
                r.getInt("carga_horaria"),
                r.getString("modalidade"),
                r.getString("status"),
                r.getTimestamp("data_criacao").toLocalDateTime()
        );
    }
}