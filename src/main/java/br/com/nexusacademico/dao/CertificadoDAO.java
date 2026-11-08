package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.CertificadoEvento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CertificadoDAO {

    private static final String COLUNAS = """
            c.id_certificado, c.id_inscricao, c.codigo_verificacao,
            c.data_emissao, c.carga_horaria_total, c.arquivo_url, c.status
            """;

    // Grava um certificado já gerado (gerar() foi chamado).
    public boolean inserir(CertificadoEvento certificado) {

        String sql = """
                INSERT INTO certificado (id_inscricao, codigo_verificacao,
                    carga_horaria_total, data_emissao, arquivo_url, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            c.setInt(1, certificado.getIdInscricao());
            c.setString(2, certificado.getCodigoVerificacao());
            c.setInt(3, certificado.getCargaHoraria());
            c.setTimestamp(4, Timestamp.valueOf(certificado.getDataEmissao()));
            c.setString(5, certificado.getUrl());
            c.setString(6, certificado.getStatus());

            c.executeUpdate();

            try (ResultSet chaves = c.getGeneratedKeys()) {
                if (chaves.next()) {
                    certificado.setIdCertificado(chaves.getInt(1));
                }
            }

            return true;

        } catch (SQLException erro) {
            System.out.println("Erro ao gravar certificado: " + erro.getMessage());
            return false;
        }
    }

    public boolean atualizarStatus(int idCertificado, String status) {

        String sql = "UPDATE certificado SET status = ? WHERE id_certificado = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            c.setString(1, status);
            c.setInt(2, idCertificado);

            return c.executeUpdate() > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar certificado: " + erro.getMessage());
            return false;
        }
    }

    public CertificadoEvento buscarPorCodigo(String codigo) {

        List<CertificadoEvento> lista =
                consultar("WHERE c.codigo_verificacao = ?", codigo);

        return lista.isEmpty() ? null : lista.get(0);
    }

    public CertificadoEvento buscarPorInscricao(int idInscricao) {

        List<CertificadoEvento> lista =
                consultar("WHERE c.id_inscricao = ?", idInscricao);

        return lista.isEmpty() ? null : lista.get(0);
    }

    public List<CertificadoEvento> listarPorAluno(int idAluno) {
        return consultar("""
                INNER JOIN inscricao i ON i.id_inscricao = c.id_inscricao
                WHERE i.id_aluno = ?
                ORDER BY c.data_emissao DESC
                """, idAluno);
    }

    private List<CertificadoEvento> consultar(String complemento,
                                              Object... parametros) {

        String sql = "SELECT " + COLUNAS + " FROM certificado c " + complemento;
        List<CertificadoEvento> lista = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement c = conexao.prepareStatement(sql)) {

            for (int i = 0; i < parametros.length; i++) {
                c.setObject(i + 1, parametros[i]);
            }

            try (ResultSet r = c.executeQuery()) {
                while (r.next()) {
                    lista.add(new CertificadoEvento(
                            r.getInt("id_certificado"),
                            r.getInt("id_inscricao"),
                            r.getString("codigo_verificacao"),
                            r.getTimestamp("data_emissao").toLocalDateTime(),
                            r.getInt("carga_horaria_total"),
                            r.getString("arquivo_url"),
                            r.getString("status")
                    ));
                }
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao consultar certificados: " + erro.getMessage());
        }

        return lista;
    }
}