package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.EventoDAO;
import br.com.nexusacademico.dao.TipoEventoDAO;
import br.com.nexusacademico.model.CriadorEvento;
import br.com.nexusacademico.model.Evento;

import java.time.LocalDate;
import java.util.List;

public class EventoController {

    private final EventoDAO eventoDAO = new EventoDAO();
    private final TipoEventoDAO tipoDAO = new TipoEventoDAO();

    // Cria o evento (status "pendente", aguardando um moderador).
    public Evento criarEvento(
            CriadorEvento criador,
            int idTipoEvento,
            String titulo,
            String descricao,
            String local,
            int vagas,
            LocalDate dataInicio,
            LocalDate dataFim,
            int cargaHoraria,
            String modalidade
    ) {

        try {
            if (tipoDAO.buscarPorId(idTipoEvento) == null) {
                System.out.println("Tipo de evento não encontrado.");
                return null;
            }

            Evento evento = criador.criarEvento(
                    idTipoEvento, titulo, descricao, local, vagas,
                    dataInicio, dataFim, cargaHoraria, modalidade
            );

            if (evento.getDataInicio().isBefore(LocalDate.now())) {
                System.out.println("A data de início não pode estar no passado.");
                return null;
            }

            return eventoDAO.cadastrar(evento) ? evento : null;

        } catch (IllegalArgumentException | IllegalStateException erro) {
            System.out.println(erro.getMessage());
            return null;
        }
    }

    // Edita um evento do próprio criador.
    public boolean editarEvento(
            CriadorEvento criador,
            int idEvento,
            String titulo,
            String descricao,
            String local,
            int vagas,
            LocalDate dataInicio,
            LocalDate dataFim,
            int cargaHoraria,
            String modalidade
    ) {

        try {
            Evento evento = eventoDAO.buscarPorId(idEvento);

            if (evento == null) {
                System.out.println("Evento não encontrado.");
                return false;
            }

            int inscritos = eventoDAO.contarInscritos(idEvento);

            if (vagas < inscritos) {
                System.out.println(
                        "O número de vagas não pode ser menor que o de inscritos ("
                                + inscritos + ")."
                );
                return false;
            }

            criador.editarEvento(
                    evento, titulo, descricao, local, vagas,
                    dataInicio, dataFim, cargaHoraria, modalidade
            );

            return eventoDAO.atualizar(evento);

        } catch (IllegalArgumentException | IllegalStateException erro) {
            System.out.println(erro.getMessage());
            return false;
        }
    }

    // Cancela um evento do próprio criador e as inscrições confirmadas.
    public boolean cancelarEvento(CriadorEvento criador, int idEvento) {

        try {
            Evento evento = eventoDAO.buscarPorId(idEvento);

            if (evento == null) {
                System.out.println("Evento não encontrado.");
                return false;
            }

            criador.cancelarEvento(evento);

            return eventoDAO.cancelar(idEvento);

        } catch (IllegalArgumentException | IllegalStateException erro) {
            System.out.println(erro.getMessage());
            return false;
        }
    }

    // Reenvia um evento rejeitado para nova avaliação.
    public boolean reenviarParaAvaliacao(CriadorEvento criador, int idEvento) {

        try {
            Evento evento = eventoDAO.buscarPorId(idEvento);

            if (evento == null) {
                System.out.println("Evento não encontrado.");
                return false;
            }

            if (evento.getIdOrganizador() != criador.getIdUsuario()) {
                System.out.println("Este evento não pertence a você.");
                return false;
            }

            evento.publicar();

            return eventoDAO.atualizarStatus(idEvento, evento.getStatus());

        } catch (IllegalArgumentException | IllegalStateException erro) {
            System.out.println(erro.getMessage());
            return false;
        }
    }

    public Evento buscarPorId(int idEvento) {
        return eventoDAO.buscarPorId(idEvento);
    }

    public List<Evento> listarMeusEventos(CriadorEvento criador) {
        return eventoDAO.listarPorOrganizador(criador.getIdUsuario());
    }

    // Eventos aprovados e ainda não encerrados.
    public List<Evento> listarDisponiveis() {
        return eventoDAO.listarDisponiveis();
    }

    public int vagasRestantes(Evento evento) {
        return evento.getVagas() - eventoDAO.contarInscritos(evento.getIdEvento());
    }
}