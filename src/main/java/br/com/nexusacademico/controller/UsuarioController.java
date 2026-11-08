package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.UsuarioDAO;
import br.com.nexusacademico.model.Usuario;
import br.com.nexusacademico.util.SenhaUtil;
import br.com.nexusacademico.util.ValidacaoUtil;

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

    // Realiza a autenticação. Devolve o usuário já no seu perfil
    // (Aluno, CriadorEvento ou Moderador).
    public Usuario realizarLogin(String email, String senha) {

        if (email == null || email.isBlank()
                || senha == null || senha.isBlank()) {

            System.out.println("Informe o e-mail e a senha.");
            return null;
        }

        return usuarioDAO.autenticar(email, senha);
    }

    // Atualiza nome e e-mail do usuário logado.
    public boolean atualizarPerfil(Usuario usuario, String nome, String email) {

        if (ValidacaoUtil.textoVazio(nome)) {
            System.out.println("O nome é obrigatório.");
            return false;
        }

        if (!ValidacaoUtil.emailValido(email)) {
            System.out.println("Informe um e-mail válido.");
            return false;
        }

        if (!email.equalsIgnoreCase(usuario.getEmail())
                && usuarioDAO.emailExiste(email)) {
            System.out.println("Já existe um usuário com este e-mail.");
            return false;
        }

        String nomeAnterior = usuario.getNome();
        String emailAnterior = usuario.getEmail();

        usuario.atualizarPerfil(nome, email);

        if (usuarioDAO.atualizarPerfil(usuario)) {
            return true;
        }

        usuario.atualizarPerfil(nomeAnterior, emailAnterior);
        return false;
    }

    // Altera a senha do usuário logado, exigindo a senha atual.
    public boolean alterarSenha(
            Usuario usuario,
            String senhaAtual,
            String novaSenha
    ) {

        if (!usuario.autenticar(senhaAtual)) {
            System.out.println("Senha atual incorreta.");
            return false;
        }

        if (!ValidacaoUtil.senhaValida(novaSenha)) {
            System.out.println("A nova senha deve possuir pelo menos 8 caracteres.");
            return false;
        }

        String novoHash = SenhaUtil.gerarHash(novaSenha);

        if (!usuarioDAO.alterarSenha(usuario.getIdUsuario(), novoHash)) {
            return false;
        }

        usuario.alterarSenha(novoHash);
        return true;
    }
}