package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.InscricaoEvento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InscricaoEventoDAO {

    private static final String COLUNAS =
            "id_inscricao, id_aluno, id_evento, data_inscricao, status";

    // Inscreve o aluno em uma transação que trava o evento, para que
    // duas inscrições simultâneas não ultrapassem o número de vagas.
    // Regras violadas são sinalizadas com IllegalStateException.
    public InscricaoEvento inscrever(int idAluno, int idEvento) {

        try (Connection conexao = Conexao.conectar()) {
            conexao.setAutoCommit(false);

            try {
                String statusEvento = null;
                int vagas = 0;
                LocalDate dataFim = null;

                try (PreparedStatement c = conexao.prepareStatement("""
                        SELECT status, vagas, data_fim FROM evento
                        WHERE id_evento = ? FOR UPDATE
                        """)) {
                    c.setInt(1, idEvento);

                    try (ResultSet r = c.executeQuery()) {
                        if (!r.next()) {
                            throw new IllegalStateException(
                                    "Evento não encontrado."
                            );
                        }

                        statusEvento = r.getString("status");
                        vagas = r.getInt("vagas");
                        dataFim = r.getDate("data_fim").toLocalDate();
                    }
                }

                if (!"aprovado".equals(statusEvento)) {
                    throw new IllegalStateException(
                            "Este evento não está aberto para inscrições."
                    );
                }

                if (dataFim.isBefore(LocalDate.now())) {
                    throw new IllegalStateException(
                            "Este evento já foi encerrado."
                    );
                }

                int ocupadas = 0;

                try (PreparedStatement c = conexao.prepareStatement("""
                        SELECT COUNT(*) FROM inscricao
                        WHERE id_evento = ? AND status <> 'cancelada'
                        """)) {
                    c.setInt(1, idEvento);

                    try (ResultSet r = c.executeQuery()) {
                        if (r.next()) {
                            ocupadas = r.getInt(1);
                        }
                    }
                }

                int idExistente = 0;
                String statusExistente = null;

                try (PreparedStatement c = conexao.prepareStatement("""
                        SELECT id_inscricao, status FROM inscricao
                        WHERE id_aluno = ? AND id_evento = ?
                        """)) {
                    c.setInt(1, idAluno);
                    c.setInt(2, idEvento);

                    try (ResultSet r = c.executeQuery()) {
                        if (r.next()) {
                            idExistente = r.getInt("id_inscricao");
                            statusExistente = r.getString("status");
                        }
                    }
                }

                if (statusExistente != null
                        && !"cancelada".equals(statusExistente)) {
                    throw new IllegalStateException(
                            "Você já possui inscrição neste evento."
                    );
                }

                if (ocupadas >= vagas) {
                    throw new IllegalStateException(
                            "Não há vagas disponíveis neste evento."
                    );
                }

                LocalDateTime agora = LocalDateTime.now();
                InscricaoEvento inscricao;

                if (statusExistente != null) {
                    // Reativa a inscrição cancelada (UNIQUE aluno+evento).
                    try (PreparedStatement c = conexao.prepareStatement("""
                            UPDATE inscricao
                            SET status = 'confirmada', data_inscricao = ?
                            WHERE id_inscricao = ?
                            """)) {
                        c.setTimestamp(1, Timestamp.valueOf(agora));
                        c.setInt(2, idExistente);
                        c.executeUpdate();
                    }

                    inscricao = new InscricaoEvento(
                            idExistente, idAluno, idEvento, agora, "confirmada"
                    );

                } else {
                    inscricao = new InscricaoEvento(idAluno, idEvento);

                    try (PreparedStatement c = conexao.prepareStatement("""
                            INSERT INTO inscricao
                                (id_aluno, id_evento, data_inscricao, status)
                            VALUES (?, ?, ?, ?)
                            """, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        c.setInt(1, idAluno);
                        c.setInt(2, idEvento);
                        c.setTimestamp(3,
                                Timestamp.valueOf(inscricao.getDataInscricao()));
                        c.setString(4, inscricao.getStatus());
                        c.executeUpdate();

                        try (ResultSet chaves = c.getGeneratedKeys()) {
                            if (chaves.next()) {
                                inscricao.setIdInscricao(chaves.getInt(1));
                            }
                        }
                    }
                }

                conexao.commit();
                return inscricao;

            } catch (SQLException | RuntimeException erro) {
                conexao.rollback();
                throw erro;
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao realizar inscrição: " + erro.getMessage());
            return null;
        }
    }

    public boolean atualizarStatus(int idInscricao, String status) {

        String sql = "UPDATE inscricao SET status = ? WHERE id_inscricao = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setString(1, status);
            c.setInt(2, idInscricao);

            return c.executeUpdate() > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar inscrição: " + erro.getMessage());
            return false;
        }
    }

    public InscricaoEvento buscarPorId(int idInscricao) {

        List<InscricaoEvento> lista =
                consultar("WHERE id_inscricao = ?", idInscricao);

        return lista.isEmpty() ? null : lista.get(0);
    }

    public List<InscricaoEvento> listarPorAluno(int idAluno) {
        return consultar("WHERE id_aluno = ? ORDER BY data_inscricao DESC",
                idAluno);
    }

    public List<InscricaoEvento> listarPorEvento(int idEvento) {
        return consultar("WHERE id_evento = ? ORDER BY id_inscricao", idEvento);
    }

    private List<InscricaoEvento> consultar(String complemento,
                                            Object... parametros) {

        String sql = "SELECT " + COLUNAS + " FROM inscricao " + complemento;
        List<InscricaoEvento> lista = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            for (int i = 0; i < parametros.length; i++) {
                c.setObject(i + 1, parametros[i]);
            }

            try (ResultSet r = c.executeQuery()) {
                while (r.next()) {
                    lista.add(new InscricaoEvento(
                            r.getInt("id_inscricao"),
                            r.getInt("id_aluno"),
                            r.getInt("id_evento"),
                            r.getTimestamp("data_inscricao").toLocalDateTime(),
                            r.getString("status")
                    ));
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao consultar inscrições: " + erro.getMessage());
        }

        return lista;
    }
}