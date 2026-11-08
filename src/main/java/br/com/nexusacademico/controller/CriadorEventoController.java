package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.CriadorEventoDAO;
import br.com.nexusacademico.dao.UsuarioDAO;
import br.com.nexusacademico.model.CriadorEvento;
import br.com.nexusacademico.util.ValidacaoUtil;

public class CriadorEventoController {

    private final CriadorEventoDAO criadorDAO = new CriadorEventoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Valida os dados e cadastra um criador de eventos (organizador).
    public boolean cadastrarCriador(
            String nome,
            String email,
            String senha,
            String vinculo,
            String cargo
    ) {

        if (ValidacaoUtil.textoVazio(nome)) {
            System.out.println("O nome é obrigatório.");
            return false;
        }

        if (!ValidacaoUtil.emailValido(email)) {
            System.out.println("Informe um e-mail válido.");
            return false;
        }

        if (!ValidacaoUtil.senhaValida(senha)) {
            System.out.println("A senha deve possuir pelo menos 8 caracteres.");
            return false;
        }

        if (usuarioDAO.emailExiste(email)) {
            System.out.println("Já existe um usuário com este e-mail.");
            return false;
        }

        try {
            CriadorEvento criador = new CriadorEvento(
                    nome, email, senha, vinculo, cargo
            );

            return criadorDAO.cadastrar(criador);

        } catch (IllegalArgumentException erro) {
            System.out.println(erro.getMessage());
            return false;
        }
    }
}