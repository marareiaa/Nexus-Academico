package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.EventoDAO;
import br.com.nexusacademico.dao.InscricaoEventoDAO;
import br.com.nexusacademico.model.Aluno;
import br.com.nexusacademico.model.CriadorEvento;
import br.com.nexusacademico.model.Evento;
import br.com.nexusacademico.model.InscricaoEvento;

import java.util.List;

public class InscricaoController {

    private final InscricaoEventoDAO inscricaoDAO = new InscricaoEventoDAO();
    private final EventoDAO eventoDAO = new EventoDAO();

    // Inscreve o aluno logado em um evento aprovado e com vagas.
    public InscricaoEvento inscrever(Aluno aluno, int idEvento) {

        try {
            aluno.inscrever(idEvento);

            return inscricaoDAO.inscrever(aluno.getIdUsuario(), idEvento);

        } catch (IllegalArgumentException | IllegalStateException erro) {
            System.out.println(erro.getMessage());
            return null;
        }
    }

    // Cancela uma inscrição do próprio aluno.
    public boolean cancelarInscricao(Aluno aluno, int idInscricao) {

        try {
            InscricaoEvento inscricao = inscricaoDAO.buscarPorId(idInscricao);

            if (inscricao == null
                    || inscricao.getIdAluno() != aluno.getIdUsuario()) {
                System.out.println("Inscrição não encontrada.");
                return false;
            }

            aluno.cancelarInscricao();
            inscricao.cancelar();

            return inscricaoDAO.atualizarStatus(
                    idInscricao, inscricao.getStatus()
            );

        } catch (IllegalArgumentException | IllegalStateException erro) {
            System.out.println(erro.getMessage());
            return false;
        }
    }

    public List<InscricaoEvento> listarMinhasInscricoes(Aluno aluno) {
        return inscricaoDAO.listarPorAluno(aluno.getIdUsuario());
    }

    // Inscritos de um evento do próprio criador.
    public List<InscricaoEvento> listarInscritos(
            CriadorEvento criador,
            int idEvento
    ) {

        Evento evento = eventoDAO.buscarPorId(idEvento);

        if (evento == null) {
            System.out.println("Evento não encontrado.");
            return null;
        }

        if (evento.getIdOrganizador() != criador.getIdUsuario()) {
            System.out.println("Este evento não pertence a você.");
            return null;
        }

        return inscricaoDAO.listarPorEvento(idEvento);
    }
}