package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.AvaliacaoEventoDAO;
import br.com.nexusacademico.dao.EventoDAO;
import br.com.nexusacademico.dao.ModeradorDAO;
import br.com.nexusacademico.dao.UsuarioDAO;
import br.com.nexusacademico.model.AvaliacaoEvento;
import br.com.nexusacademico.model.Evento;
import br.com.nexusacademico.model.Moderador;
import br.com.nexusacademico.util.ValidacaoUtil;

import java.util.List;

public class ModeradorController {

    private final ModeradorDAO moderadorDAO = new ModeradorDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EventoDAO eventoDAO = new EventoDAO();
    private final AvaliacaoEventoDAO avaliacaoDAO = new AvaliacaoEventoDAO();

    // Informa se já existe algum moderador (se não, o primeiro acesso é livre).
    public boolean existeModerador() {
        return moderadorDAO.existeAlgum();
    }

    // Cadastra um moderador.
    // - Sem nenhum moderador no sistema: qualquer pessoa pode criar o
    //   primeiro, que obrigatoriamente é administrador.
    // - Depois disso: somente um moderador administrador (solicitante).
    public boolean cadastrarModerador(
            Moderador solicitante,
            String nome,
            String email,
            String senha,
            String nivelAcesso
    ) {

        boolean primeiroAcesso = !moderadorDAO.existeAlgum();

        if (!primeiroAcesso
                && (solicitante == null || !solicitante.isAdm())) {
            System.out.println(
                    "Somente moderadores administradores podem cadastrar moderadores."
            );
            return false;
        }

        if (primeiroAcesso) {
            nivelAcesso = "adm";
        }

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
            return moderadorDAO.cadastrar(
                    new Moderador(nome, email, senha, nivelAcesso)
            );

        } catch (IllegalArgumentException erro) {
            System.out.println(erro.getMessage());
            return false;
        }
    }

    // Eventos aguardando avaliação.
    public List<Evento> listarPendentes() {
        return eventoDAO.listarPorStatus("pendente");
    }

    public boolean aprovarEvento(Moderador moderador, int idEvento) {
        return avaliar(moderador, idEvento, true);
    }

    public boolean rejeitarEvento(Moderador moderador, int idEvento) {
        return avaliar(moderador, idEvento, false);
    }

    private boolean avaliar(Moderador moderador, int idEvento, boolean aprovar) {

        try {
            Evento evento = eventoDAO.buscarPorId(idEvento);

            if (evento == null) {
                System.out.println("Evento não encontrado.");
                return false;
            }

            AvaliacaoEvento avaliacao = aprovar
                    ? moderador.aprovarEvento(evento)
                    : moderador.rejeitarEvento(evento);

            return avaliacaoDAO.registrar(avaliacao);

        } catch (IllegalStateException | IllegalArgumentException erro) {
            System.out.println(erro.getMessage());
            return false;
        }
    }
}