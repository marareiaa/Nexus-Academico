package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.Aluno;
import br.com.nexusacademico.model.CriadorEvento;
import br.com.nexusacademico.model.Moderador;
import br.com.nexusacademico.model.Usuario;
import br.com.nexusacademico.util.SenhaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UsuarioDAO {

    // Insere a linha de usuario dentro de uma transação já aberta
    // e devolve o id gerado. Usado pelos DAOs de Aluno, Criador e Moderador.
    static int inserirUsuario(Connection conexao, Usuario usuario)
            throws SQLException {

        String sql = """
                INSERT INTO usuario (nome, email, senha_hash)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement comando = conexao.prepareStatement(
                sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getEmail());
            comando.setString(3, SenhaUtil.gerarHash(usuario.getSenhaHash()));

            comando.executeUpdate();

            try (ResultSet resultado = comando.getGeneratedKeys()) {

                if (!resultado.next()) {
                    throw new SQLException(
                            "Não foi possível recuperar o ID do usuário."
                    );
                }

                return resultado.getInt(1);
            }
        }
    }

    // Cadastra um novo usuário com a senha protegida.
    public boolean cadastrar(Usuario usuario) {

        try (Connection conexao = Conexao.conectar()) {

            int id = inserirUsuario(conexao, usuario);
            usuario.setIdUsuario(id);

            return true;

        } catch (SQLException erro) {
            System.out.println("Erro ao cadastrar usuário: " + erro.getMessage());
            return false;
        }
    }

    // Informa se já existe um usuário com o e-mail.
    public boolean emailExiste(String email) {

        String sql = "SELECT 1 FROM usuario WHERE email = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, email);

            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next();
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao consultar e-mail: " + erro.getMessage());
            return false;
        }
    }

    // Busca um usuário pelo e-mail já no seu perfil correto:
    // Aluno, CriadorEvento, Moderador (ou Usuario, se não tiver perfil).
    public Usuario buscarPorEmail(String email) {

        String sql = """
                SELECT
                    u.id_usuario, u.nome, u.email, u.senha_hash,
                    u.data_cadastro, u.ativo,
                    a.ra, a.curso, a.periodo, a.turma,
                    o.vinculo, o.cargo,
                    m.nivel_acesso
                FROM usuario u
                LEFT JOIN aluno a ON a.id_aluno = u.id_usuario
                LEFT JOIN organizador o ON o.id_organizador = u.id_usuario
                LEFT JOIN moderador m ON m.id_moderador = u.id_usuario
                WHERE u.email = ?
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, email);

            try (ResultSet r = comando.executeQuery()) {

                if (!r.next()) {
                    return null;
                }

                int id = r.getInt("id_usuario");
                String nome = r.getString("nome");
                String emailBanco = r.getString("email");
                String hash = r.getString("senha_hash");
                LocalDateTime cadastro =
                        r.getTimestamp("data_cadastro").toLocalDateTime();

                Usuario usuario;

                if (r.getString("ra") != null) {
                    usuario = new Aluno(id, nome, emailBanco, hash, cadastro,
                            r.getString("ra"), r.getString("curso"),
                            r.getString("periodo"), r.getString("turma"));

                } else if (r.getString("vinculo") != null) {
                    usuario = new CriadorEvento(id, nome, emailBanco, hash,
                            cadastro, r.getString("vinculo"),
                            r.getString("cargo"));

                } else if (r.getString("nivel_acesso") != null) {
                    usuario = new Moderador(id, nome, emailBanco, hash,
                            cadastro, r.getString("nivel_acesso"));

                } else {
                    usuario = new Usuario(id, nome, emailBanco, hash, cadastro);
                }

                usuario.setAtivo(r.getBoolean("ativo"));

                return usuario;
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao buscar usuário: " + erro.getMessage());
        }

        return null;
    }

    // Autentica um usuário utilizando seu e-mail e senha.
    public Usuario autenticar(String email, String senha) {

        Usuario usuario = buscarPorEmail(email);

        if (usuario == null) {
            return null;
        }

        // autenticar() também recusa usuários inativos.
        return usuario.autenticar(senha) ? usuario : null;
    }

    // Atualiza nome e e-mail.
    public boolean atualizarPerfil(Usuario usuario) {

        String sql = "UPDATE usuario SET nome = ?, email = ? WHERE id_usuario = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getEmail());
            comando.setInt(3, usuario.getIdUsuario());

            return comando.executeUpdate() > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar perfil: " + erro.getMessage());
            return false;
        }
    }

    // Grava um novo hash de senha.
    public boolean alterarSenha(int idUsuario, String novoHash) {

        String sql = "UPDATE usuario SET senha_hash = ? WHERE id_usuario = ?";

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, novoHash);
            comando.setInt(2, idUsuario);

            return comando.executeUpdate() > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao alterar senha: " + erro.getMessage());
            return false;
        }
    }
}