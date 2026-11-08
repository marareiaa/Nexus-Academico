package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.EncontroEventoDAO;
import br.com.nexusacademico.dao.EventoDAO;
import br.com.nexusacademico.dao.PresencaDAO;
import br.com.nexusacademico.model.CriadorEvento;
import br.com.nexusacademico.model.EncontroEvento;
import br.com.nexusacademico.model.Evento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EncontroController {

    private final EncontroEventoDAO encontroDAO = new EncontroEventoDAO();
    private final EventoDAO eventoDAO = new EventoDAO();
    private final PresencaDAO presencaDAO = new PresencaDAO();

    // Adiciona um encontro (dia e horário) a um evento do criador.
    public EncontroEvento adicionarEncontro(
            CriadorEvento criador,
            int idEvento,
            LocalDate data,
            LocalTime horaInicio,
            LocalTime horaFim
    ) {

        try {
            Evento evento = eventoDoCriador(criador, idEvento);

            if ("cancelado".equals(evento.getStatus())) {
                throw new IllegalStateException(
                        "Não é possível adicionar encontros a um evento cancelado."
                );
            }

            if (data == null
                    || data.isBefore(evento.getDataInicio())
                    || data.isAfter(evento.getDataFim())) {
                throw new IllegalArgumentException(
                        "A data do encontro deve estar entre o início e o fim do evento."
                );
            }

            EncontroEvento encontro =
                    new EncontroEvento(idEvento, data, horaInicio, horaFim);

            return encontroDAO.cadastrar(encontro) ? encontro : null;

        } catch (IllegalArgumentException | IllegalStateException erro) {
            System.out.println(erro.getMessage());
            return null;
        }
    }

    public List<EncontroEvento> listarPorEvento(int idEvento) {
        return encontroDAO.listarPorEvento(idEvento);
    }

    // Remove um encontro, desde que ainda não tenha presenças registradas.
    public boolean removerEncontro(CriadorEvento criador, int idEncontro) {

        try {
            EncontroEvento encontro = encontroDAO.buscarPorId(idEncontro);

            if (encontro == null) {
                System.out.println("Encontro não encontrado.");
                return false;
            }

            eventoDoCriador(criador, encontro.getIdEvento());

            if (presencaDAO.existeParaEncontro(idEncontro)) {
                System.out.println(
                        "Este encontro já possui presenças registradas e não pode ser removido."
                );
                return false;
            }

            return encontroDAO.excluir(idEncontro);

        } catch (IllegalStateException erro) {
            System.out.println(erro.getMessage());
            return false;
        }
    }

    private Evento eventoDoCriador(CriadorEvento criador, int idEvento) {

        Evento evento = eventoDAO.buscarPorId(idEvento);

        if (evento == null) {
            throw new IllegalStateException("Evento não encontrado.");
        }

        if (evento.getIdOrganizador() != criador.getIdUsuario()) {
            throw new IllegalStateException("Este evento não pertence a você.");
        }

        return evento;
    }
}