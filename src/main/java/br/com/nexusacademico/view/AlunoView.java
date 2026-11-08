package br.com.nexusacademico.view;

import br.com.nexusacademico.controller.AlunoController;
import br.com.nexusacademico.controller.CertificadoController;
import br.com.nexusacademico.controller.InscricaoController;
import br.com.nexusacademico.model.Aluno;
import br.com.nexusacademico.model.CertificadoEvento;
import br.com.nexusacademico.model.Evento;
import br.com.nexusacademico.model.InscricaoEvento;
import br.com.nexusacademico.util.EntradaConsole;

import java.util.List;

public class AlunoView extends BaseView {

    private final Aluno aluno;
    private final AlunoController alunoController = new AlunoController();
    private final InscricaoController inscricaoController = new InscricaoController();
    private final CertificadoController certificadoController = new CertificadoController();

    public AlunoView(EntradaConsole entrada, Aluno aluno) {
        super(entrada);
        this.aluno = aluno;
    }

    public void exibirMenu() {
        int opcao;

        do {
            titulo("ALUNO - " + aluno.getNome());
            System.out.println("1 - Ver eventos disponíveis");
            System.out.println("2 - Inscrever-se em um evento");
            System.out.println("3 - Minhas inscrições");
            System.out.println("4 - Cancelar inscrição");
            System.out.println("5 - Meus certificados");
            System.out.println("6 - Atualizar dados acadêmicos");
            System.out.println("7 - Atualizar perfil");
            System.out.println("8 - Alterar senha");
            System.out.println("0 - Sair (logout)");

            opcao = entrada.lerOpcao();

            switch (opcao) {
                case 1 -> aluno.visualizarEventos();
                case 2 -> inscrever();
                case 3 -> minhasInscricoes();
                case 4 -> cancelarInscricao();
                case 5 -> meusCertificados();
                case 6 -> atualizarDadosAcademicos();
                case 7 -> atualizarPerfil(aluno);
                case 8 -> alterarSenha(aluno);
                case 0 -> System.out.println("Sessão encerrada.");
                default -> System.out.println("Opção inválida.");
            }

            if (opcao == 1) {
                listarEventosDisponiveis();
            }

        } while (opcao != 0);
    }

    private void listarEventosDisponiveis() {
        titulo("EVENTOS DISPONÍVEIS");

        List<Evento> eventos = eventoController.listarDisponiveis();

        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento disponível no momento.");
            return;
        }

        eventos.forEach(this::imprimirEvento);
    }

    private void inscrever() {
        listarEventosDisponiveis();

        int idEvento = entrada.lerInt("\nID do evento");
        InscricaoEvento inscricao = inscricaoController.inscrever(aluno, idEvento);

        if (inscricao != null) {
            System.out.println("\nInscrição confirmada! (nº " + inscricao.getIdInscricao() + ")");
        } else {
            System.out.println("\nNão foi possível concluir a inscrição.");
        }
    }

    private void minhasInscricoes() {
        titulo("MINHAS INSCRIÇÕES");

        List<InscricaoEvento> inscricoes =
                inscricaoController.listarMinhasInscricoes(aluno);

        if (inscricoes.isEmpty()) {
            System.out.println("Você ainda não possui inscrições.");
            return;
        }

        for (InscricaoEvento i : inscricoes) {
            Evento evento = eventoController.buscarPorId(i.getIdEvento());
            String titulo = evento == null ? "(evento removido)" : evento.getTitulo();

            System.out.println("Inscrição nº " + i.getIdInscricao()
                    + " | " + titulo
                    + " | " + i.getStatus()
                    + " | em " + i.getDataInscricao().format(EntradaConsole.DATA_HORA));
        }
    }

    private void cancelarInscricao() {
        minhasInscricoes();

        int id = entrada.lerInt("\nNº da inscrição a cancelar");

        resultado(
                inscricaoController.cancelarInscricao(aluno, id),
                "Inscrição cancelada.",
                "Não foi possível cancelar a inscrição."
        );
    }

    private void meusCertificados() {
        titulo("MEUS CERTIFICADOS");

        List<CertificadoEvento> certificados =
                certificadoController.listarMeusCertificados(aluno);

        if (certificados.isEmpty()) {
            System.out.println("Você ainda não possui certificados.");
            return;
        }

        for (CertificadoEvento c : certificados) {
            System.out.println("----------------------------------------");
            System.out.println("Evento: " + certificadoController.tituloDoEvento(c));
            System.out.println("Código de verificação: " + c.getCodigoVerificacao());
            System.out.println("Carga horária: " + c.getCargaHoraria() + "h");
            System.out.println("Emitido em: "
                    + c.getDataEmissao().format(EntradaConsole.DATA_HORA)
                    + " | Status: " + c.getStatus());
            System.out.println("Arquivo: " + c.getUrl());
        }
    }

    private void atualizarDadosAcademicos() {
        titulo("ATUALIZAR DADOS ACADÊMICOS");

        String curso = entrada.lerTexto("Novo curso [" + aluno.getCurso() + "]");
        String periodo = entrada.lerTexto("Novo período [" + aluno.getPeriodo() + "]");
        String turma = entrada.lerTexto("Nova turma [" + aluno.getTurma() + "]");

        if (curso.isBlank()) curso = aluno.getCurso();
        if (periodo.isBlank()) periodo = aluno.getPeriodo();
        if (turma.isBlank()) turma = aluno.getTurma();

        boolean ok = alunoController.atualizarDadosAcademicos(
                aluno.getRa(), curso, periodo, turma
        );

        if (ok) {
            aluno.atualizarDadosAcademicos(curso, periodo, turma.toUpperCase());
        }

        resultado(ok, "Dados acadêmicos atualizados com sucesso!",
                "Não foi possível atualizar os dados.");
    }
}