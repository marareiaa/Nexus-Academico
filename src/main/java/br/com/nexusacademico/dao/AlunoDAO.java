
package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.Aluno;
import br.com.nexusacademico.util.SenhaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    // Cadastra o usuário e seus dados acadêmicos em uma única transação.
    public boolean cadastrar(Aluno aluno) {

        String sqlUsuario = """
                INSERT INTO usuario (nome, email, senha_hash)
                VALUES (?, ?, ?)
                """;

        String sqlAluno = """
                INSERT INTO aluno (id_aluno, ra, curso, periodo, turma)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conexao = Conexao.conectar()) {
            conexao.setAutoCommit(false);

            try {
                int idUsuario;

                try (PreparedStatement comandoUsuario =
                             conexao.prepareStatement(
                                     sqlUsuario,
                                     PreparedStatement.RETURN_GENERATED_KEYS
                             )) {

                    comandoUsuario.setString(1, aluno.getNome());
                    comandoUsuario.setString(2, aluno.getEmail());
                    comandoUsuario.setString(
                            3,
                            SenhaUtil.gerarHash(aluno.getSenhaHash())
                    );

                    comandoUsuario.executeUpdate();

                    try (ResultSet resultado = comandoUsuario.getGeneratedKeys()) {

                        if (!resultado.next()) {
                            throw new SQLException(
                                    "Não foi possível recuperar o ID do usuário."
                            );
                        }

                        idUsuario = resultado.getInt(1);
                    }
                }

                try (PreparedStatement comandoAluno =
                             conexao.prepareStatement(sqlAluno)) {

                    comandoAluno.setInt(1, idUsuario);
                    comandoAluno.setString(2, aluno.getRa());
                    comandoAluno.setString(3, aluno.getCurso());
                    comandoAluno.setString(4, aluno.getPeriodo());
                    comandoAluno.setString(5, aluno.getTurma());

                    comandoAluno.executeUpdate();
                }

                conexao.commit();
                aluno.setIdUsuario(idUsuario);

                return true;

            } catch (SQLException | RuntimeException erro) {
                conexao.rollback();

                System.out.println(
                        "Erro ao cadastrar aluno: " + erro.getMessage()
                );

                return false;
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro na conexão com o banco: " + erro.getMessage()
            );

            return false;
        }
    }

    // Busca um aluno pelo seu Registro Acadêmico.
    public Aluno buscarPorRa(String ra) {

        String sql = """
                SELECT
                    u.id_usuario,
                    u.nome,
                    u.email,
                    u.senha_hash,
                    u.data_cadastro,
                    u.ativo,
                    a.ra,
                    a.curso,
                    a.periodo,
                    a.turma
                FROM usuario u
                INNER JOIN aluno a
                    ON u.id_usuario = a.id_aluno
                WHERE a.ra = ?
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, ra);

            try (ResultSet resultado = comando.executeQuery()) {

                if (resultado.next()) {
                    Aluno aluno = new Aluno(

                            resultado.getInt("id_usuario"),
                            resultado.getString("nome"),
                            resultado.getString("email"),
                            resultado.getString("senha_hash"),
                            resultado.getTimestamp("data_cadastro")
                                    .toLocalDateTime(),
                            resultado.getString("ra"),
                            resultado.getString("curso"),
                            resultado.getString("periodo"),
                            resultado.getString("turma")
                    );

                    aluno.setAtivo(resultado.getBoolean("ativo"));
                    return aluno;
                }
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao buscar aluno: " + erro.getMessage()
            );
        }

        return null;
    }

    // Retorna todos os alunos cadastrados, organizados pelo nome.
    public List<Aluno> listarTodos() {

        String sql = """
                SELECT
                    u.id_usuario,
                    u.nome,
                    u.email,
                    u.senha_hash,
                    u.data_cadastro,
                     u.ativo,
                    a.ra,
                    a.curso,
                    a.periodo,
                    a.turma
                FROM usuario u
                INNER JOIN aluno a
                    ON u.id_usuario = a.id_aluno
                ORDER BY u.nome
                """;

        List<Aluno> alunos = new ArrayList<>();

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {

                Aluno aluno = new Aluno(
                        resultado.getInt("id_usuario"),
                        resultado.getString("nome"),
                        resultado.getString("email"),
                        resultado.getString("senha_hash"),
                        resultado.getTimestamp("data_cadastro")
                                .toLocalDateTime(),
                        resultado.getString("ra"),
                        resultado.getString("curso"),
                        resultado.getString("periodo"),
                        resultado.getString("turma")
                );
                aluno.setAtivo(resultado.getBoolean("ativo"));
                alunos.add(aluno);
            }

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao listar alunos: " + erro.getMessage()
            );
        }

        return alunos;
    }

    // Atualiza os dados acadêmicos de um aluno.
    public boolean atualizarDadosAcademicos(Aluno aluno) {

        String sql = """
                UPDATE aluno
                SET curso = ?,
                    periodo = ?,
                    turma = ?
                WHERE ra = ?
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, aluno.getCurso());
            comando.setString(2, aluno.getPeriodo());
            comando.setString(3, aluno.getTurma());
            comando.setString(4, aluno.getRa());

            return comando.executeUpdate() > 0;

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao atualizar dados acadêmicos: "
                            + erro.getMessage()
            );

            return false;
        }
    }

    // Exclui um aluno e seu usuário correspondente.
    public boolean excluir(int idAluno) {

        String sql = """
                DELETE FROM usuario
                WHERE id_usuario = ?
                AND EXISTS (
                    SELECT 1
                    FROM aluno
                    WHERE aluno.id_aluno = usuario.id_usuario
                )
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idAluno);

            return comando.executeUpdate() > 0;

        } catch (SQLException erro) {
            System.out.println(
                    "Erro ao excluir aluno: " + erro.getMessage()
            );

            return false;
        }
    }
}
