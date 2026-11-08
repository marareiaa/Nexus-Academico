package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.CertificadoDAO;
import br.com.nexusacademico.dao.EncontroEventoDAO;
import br.com.nexusacademico.dao.EventoDAO;
import br.com.nexusacademico.dao.InscricaoEventoDAO;
import br.com.nexusacademico.dao.PresencaDAO;
import br.com.nexusacademico.model.Aluno;
import br.com.nexusacademico.model.CertificadoEvento;
import br.com.nexusacademico.model.CriadorEvento;
import br.com.nexusacademico.model.Evento;
import br.com.nexusacademico.model.InscricaoEvento;
import br.com.nexusacademico.model.ValidarCertificado;

import java.util.List;

public class CertificadoController {

    private final CertificadoDAO certificadoDAO = new CertificadoDAO();
    private final InscricaoEventoDAO inscricaoDAO = new InscricaoEventoDAO();
    private final EncontroEventoDAO encontroDAO = new EncontroEventoDAO();
    private final PresencaDAO presencaDAO = new PresencaDAO();
    private final EventoDAO eventoDAO = new EventoDAO();
    private final ValidarCertificado validador = new ValidarCertificado();

    // Encerra o evento: cada inscrição confirmada que teve presença em
    // TODOS os encontros vira "concluida" e recebe certificado; as demais
    // viram "ausente". Devolve {certificadosEmitidos, ausentes}
    // (ou null se a operação não for permitida).
    public int[] encerrarEventoEEmitirCertificados(
            CriadorEvento criador,
            int idEvento
    ) {

        try {
            Evento evento = eventoDAO.buscarPorId(idEvento);

            if (evento == null) {
                System.out.println("Evento não encontrado.");
                return null;
            }

            if (evento.getIdOrganizador() != criador.getIdUsuario()) {
                System.out.println("Este evento não pertence a você.");
                return null;
            }

            if (!"aprovado".equals(evento.getStatus())) {
                System.out.println(
                        "Somente eventos aprovados podem ser encerrados "
                                + "(status atual: " + evento.getStatus() + ")."
                );
                return null;
            }

            int totalEncontros = encontroDAO.listarPorEvento(idEvento).size();

            if (totalEncontros == 0) {
                System.out.println(
                        "Cadastre ao menos um encontro e registre as presenças antes de encerrar."
                );
                return null;
            }

            int emitidos = 0;
            int ausentes = 0;

            for (InscricaoEvento inscricao : inscricaoDAO.listarPorEvento(idEvento)) {

                if (!"confirmada".equals(inscricao.getStatus())) {
                    continue;
                }

                int presencas =
                        presencaDAO.contarPresencas(inscricao.getIdInscricao());

                if (presencas == totalEncontros) {

                    if (certificadoDAO.buscarPorInscricao(
                            inscricao.getIdInscricao()) == null) {

                        CertificadoEvento certificado = new CertificadoEvento(
                                inscricao.getIdInscricao(),
                                evento.getCargaHoraria(),
                                null
                        );
                        certificado.gerar();

                        if (!certificadoDAO.inserir(certificado)) {
                            continue;
                        }
                    }

                    inscricao.concluir();
                    emitidos++;

                } else {
                    inscricao.marcarAusente();
                    ausentes++;
                }

                inscricaoDAO.atualizarStatus(
                        inscricao.getIdInscricao(), inscricao.getStatus()
                );
            }

            return new int[]{emitidos, ausentes};

        } catch (IllegalArgumentException | IllegalStateException erro) {
            System.out.println(erro.getMessage());
            return null;
        }
    }

    public List<CertificadoEvento> listarMeusCertificados(Aluno aluno) {
        return certificadoDAO.listarPorAluno(aluno.getIdUsuario());
    }

    // Título do evento ao qual o certificado pertence.
    public String tituloDoEvento(CertificadoEvento certificado) {

        InscricaoEvento inscricao =
                inscricaoDAO.buscarPorId(certificado.getIdInscricao());

        if (inscricao == null) {
            return "(evento desconhecido)";
        }

        Evento evento = eventoDAO.buscarPorId(inscricao.getIdEvento());

        return evento == null ? "(evento desconhecido)" : evento.getTitulo();
    }

    // Validação pública: devolve o certificado se o código for válido.
    public CertificadoEvento validar(String codigo) {

        if (codigo == null || codigo.isBlank()) {
            System.out.println("Informe o código de verificação.");
            return null;
        }

        if (!validador.validarCodigo(codigo)) {
            return null;
        }

        return validador.consultarCertificado(codigo);
    }
}