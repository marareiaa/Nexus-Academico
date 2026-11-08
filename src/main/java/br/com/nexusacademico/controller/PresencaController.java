package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.EncontroEventoDAO;
import br.com.nexusacademico.dao.EventoDAO;
import br.com.nexusacademico.dao.InscricaoEventoDAO;
import br.com.nexusacademico.dao.PresencaDAO;
import br.com.nexusacademico.model.CriadorEvento;
import br.com.nexusacademico.model.EncontroEvento;
import br.com.nexusacademico.model.Evento;
import br.com.nexusacademico.model.InscricaoEvento;
import br.com.nexusacademico.model.Presenca;

import java.util.List;

public class PresencaController {

    private final PresencaDAO presencaDAO = new PresencaDAO();
    private final InscricaoEventoDAO inscricaoDAO = new InscricaoEventoDAO();
    private final EncontroEventoDAO encontroDAO = new EncontroEventoDAO();
    private final EventoDAO eventoDAO = new EventoDAO();

    // Registra presença (ou ausência) de um aluno em um encontro.
    public boolean registrarPresenca(
            CriadorEvento criador,
            int idInscricao,
            int idEncontro,
            boolean presente
    ) {

        try {
            InscricaoEvento inscricao = inscricaoDAO.buscarPorId(idInscricao);

            if (inscricao == null) {
                System.out.println("Inscrição não encontrada.");
                return false;
            }

            Evento evento = eventoDAO.buscarPorId(inscricao.getIdEvento());

            if (evento == null
                    || evento.getIdOrganizador() != criador.getIdUsuario()) {
                System.out.println("Este evento não pertence a você.");
                return false;
            }

            EncontroEvento encontro = encontroDAO.buscarPorId(idEncontro);

            if (encontro == null || encontro.getIdEvento() != evento.getIdEvento()) {
                System.out.println("Encontro não pertence a este evento.");
                return false;
            }

            Presenca presenca = presente
                    ? criador.registrarPresenca(inscricao, idEncontro)
                    : ausencia(inscricao, idEncontro);

            return presencaDAO.registrar(presenca);

        } catch (IllegalArgumentException | IllegalStateException erro) {
            System.out.println(erro.getMessage());
            return false;
        }
    }

    private Presenca ausencia(InscricaoEvento inscricao, int idEncontro) {

        if (!"confirmada".equals(inscricao.getStatus())) {
            throw new IllegalStateException(
                    "Não é possível registrar presença em uma inscrição não confirmada."
            );
        }

        return new Presenca(inscricao.getIdInscricao(), idEncontro, false);
    }

    public List<Presenca> listarPresencas(int idInscricao) {
        return presencaDAO.listarPorInscricao(idInscricao);
    }
}