package br.com.nexusacademico.dao;

import br.com.nexusacademico.database.Conexao;
import br.com.nexusacademico.model.Usuario;
import br.com.nexusacademico.util.SenhaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // Cadastra um novo usuário com a senha protegida.
    public boolean cadastrar(Usuario usuario) {

        String sql = """
                INSERT INTO usuario (nome, email, senha_hash)
                VALUES (?, ?, ?)
                """;

        String hashSenha = SenhaUtil.gerarHash(usuario.getSenhaHash());

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getEmail());
            comando.setString(3, hashSenha);

            comando.executeUpdate();

            return true;

        } catch (SQLException erro) {
            System.out.println("Erro ao cadastrar usuário: " + erro.getMessage());
            return false;
        }
    }

    // Busca um usuário pelo e-mail.
    public Usuario buscarPorEmail(String email) {

        String sql = """
                SELECT id_usuario, nome, email, senha_hash, data_cadastro, ativo
                FROM usuario
                WHERE email = ?
                """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, email);

            try (ResultSet resultado = comando.executeQuery()) {

                if (resultado.next()) {
                    return new Usuario(
                            resultado.getInt("id_usuario"),
                            resultado.getString("nome"),
                            resultado.getString("email"),
                            resultado.getString("senha_hash"),
                            resultado.getTimestamp("data_cadastro").toLocalDateTime(),
                            resultado.getBoolean("ativo")
                    );
                }
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

        boolean senhaValida = SenhaUtil.verificarSenha(
                senha,
                usuario.getSenhaHash()
        );

        return usuario.autenticar(senha) ? usuario : null;
    }
}