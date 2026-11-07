package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.UsuarioDAO;
import br.com.nexusacademico.model.Usuario;

public class UsuarioController {

    private final UsuarioDAO usuarioDAO;

    // Inicializa o acesso aos dados dos usuários.
    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // Valida e solicita o cadastro de um usuário.
    public boolean cadastrarUsuario(
            String nome,
            String email,
            String senha
    ) {

        if (nome == null || nome.isBlank()) {
            System.out.println("O nome é obrigatório.");
            return false;
        }

        if (email == null || email.isBlank()) {
            System.out.println("O e-mail é obrigatório.");
            return false;
        }

        if (senha == null || senha.isBlank()) {
            System.out.println("A senha é obrigatória.");
            return false;
        }

        Usuario usuario = new Usuario(nome, email, senha);

        return usuarioDAO.cadastrar(usuario);
    }

    // Realiza a autenticação de um usuário.
    public Usuario realizarLogin(String email, String senha) {

        if (email == null || email.isBlank()
                || senha == null || senha.isBlank()) {

            System.out.println("Informe o e-mail e a senha.");
            return null;
        }

        return usuarioDAO.autenticar(email, senha);
    }
}