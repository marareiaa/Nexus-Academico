package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.AvaliacaoEvento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AvaliacaoEventoDAO {

    // Registra a avaliação e muda o status do evento na mesma transação.
    // Só avalia eventos pendentes (IllegalStateException caso contrário).
    public boolean registrar(AvaliacaoEvento avaliacao) {

        try (Connection conexao = Conexao.conectar()) {
            conexao.setAutoCommit(false);

            try {
                String statusAtual = null;

                try (PreparedStatement c = conexao.prepareStatement(
                        "SELECT status FROM evento WHERE id_evento = ? FOR UPDATE")) {
                    c.setInt(1, avaliacao.getIdEvento());

                    try (ResultSet r = c.executeQuery()) {
                        if (!r.next()) {
                            throw new IllegalStateException(
                                    "Evento não encontrado."
                            );
                        }
                        statusAtual = r.getString("status");
                    }
                }

                if (!"pendente".equals(statusAtual)) {
                    throw new IllegalStateException(
                            "Somente eventos pendentes podem ser avaliados "
                                    + "(status atual: " + statusAtual + ")."
                    );
                }

                try (PreparedStatement c = conexao.prepareStatement("""
                        INSERT INTO avaliacao_evento
                            (id_moderador, id_evento, decisao, data_avaliacao)
                        VALUES (?, ?, ?, ?)
                        """, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    c.setInt(1, avaliacao.getIdModerador());
                    c.setInt(2, avaliacao.getIdEvento());
                    c.setString(3, avaliacao.getDecisao());
                    c.setTimestamp(4,
                            Timestamp.valueOf(avaliacao.getDataAvaliacao()));
                    c.executeUpdate();

                    try (ResultSet chaves = c.getGeneratedKeys()) {
                        if (chaves.next()) {
                            avaliacao.setIdAvaliacao(chaves.getInt(1));
                        }
                    }
                }

                try (PreparedStatement c = conexao.prepareStatement(
                        "UPDATE evento SET status = ? WHERE id_evento = ?")) {
                    c.setString(1, avaliacao.getDecisao());
                    c.setInt(2, avaliacao.getIdEvento());
                    c.executeUpdate();
                }

                conexao.commit();
                return true;

            } catch (SQLException | RuntimeException erro) {
                conexao.rollback();
                throw erro;
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao registrar avaliação: " + erro.getMessage());
            return false;
        }
    }
}