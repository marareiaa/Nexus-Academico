package br.com.nexusacademico.view;

import br.com.nexusacademico.controller.AlunoController;
import br.com.nexusacademico.controller.ModeradorController;
import br.com.nexusacademico.controller.TipoEventoController;
import br.com.nexusacademico.model.Aluno;
import br.com.nexusacademico.model.Evento;
import br.com.nexusacademico.model.Moderador;
import br.com.nexusacademico.util.EntradaConsole;

import java.util.List;

public class ModeradorView extends BaseView {

    private final Moderador moderador;
    private final ModeradorController moderadorController = new ModeradorController();
    private final AlunoController alunoController = new AlunoController();
    private final TipoEventoController tipoController = new TipoEventoController();

    public ModeradorView(EntradaConsole entrada, Moderador moderador) {
        super(entrada);
        this.moderador = moderador;
    }

    public void exibirMenu() {
        int opcao;

        do {
            titulo("MODERADOR (" + moderador.getNivelAcesso() + ") - " + moderador.getNome());
            System.out.println("1 - Ver eventos pendentes");
            System.out.println("2 - Aprovar evento");
            System.out.println("3 - Rejeitar evento");
            System.out.println("4 - Listar alunos");
            System.out.println("5 - Buscar aluno por RA");
            System.out.println("6 - Cadastrar tipo de evento (adm)");
            System.out.println("7 - Cadastrar moderador (adm)");
            System.out.println("8 - Atualizar perfil");
            System.out.println("9 - Alterar senha");
            System.out.println("0 - Sair (logout)");

            opcao = entrada.lerOpcao();

            switch (opcao) {
                case 1 -> {
                    moderador.visualizarPendentes();
                    listarPendentes();
                }
                case 2 -> avaliar(true);
                case 3 -> avaliar(false);
                case 4 -> listarAlunos();
                case 5 -> buscarAluno();
                case 6 -> cadastrarTipo();
                case 7 -> cadastrarModerador();
                case 8 -> atualizarPerfil(moderador);
                case 9 -> alterarSenha(moderador);
                case 0 -> System.out.println("Sessão encerrada.");
                default -> System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    private boolean listarPendentes() {
        titulo("EVENTOS PENDENTES");

        List<Evento> pendentes = moderadorController.listarPendentes();

        if (pendentes.isEmpty()) {
            System.out.println("Nenhum evento aguardando avaliação.");
            return false;
        }

        pendentes.forEach(this::imprimirEvento);
        return true;
    }

    private void avaliar(boolean aprovar) {
        if (!listarPendentes()) {
            return;
        }

        int id = entrada.lerInt("\nID do evento");

        boolean ok = aprovar
                ? moderadorController.aprovarEvento(moderador, id)
                : moderadorController.rejeitarEvento(moderador, id);

        resultado(ok,
                aprovar ? "Evento aprovado!" : "Evento rejeitado.",
                "Não foi possível registrar a avaliação.");
    }

    private void listarAlunos() {
        titulo("ALUNOS CADASTRADOS");

        List<Aluno> alunos = alunoController.listarAlunos();

        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }

        for (Aluno a : alunos) {
            imprimirAluno(a);
        }

        System.out.println("----------------------------");
        System.out.println("Total de alunos: " + alunos.size());
    }

    private void buscarAluno() {
        titulo("BUSCAR ALUNO");

        Aluno aluno = alunoController.buscarPorRa(entrada.lerTexto("RA do aluno"));

        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }

        imprimirAluno(aluno);
    }

    private void imprimirAluno(Aluno a) {
        System.out.println("----------------------------");
        System.out.println("Nome: " + a.getNome());
        System.out.println("E-mail: " + a.getEmail());
        System.out.println("RA: " + a.getRa());
        System.out.println("Curso: " + a.getCurso());
        System.out.println("Período: " + a.getPeriodo());
        System.out.println("Turma: " + a.getTurma());
    }

    private void cadastrarTipo() {
        if (!moderador.isAdm()) {
            System.out.println("Somente moderadores administradores podem cadastrar tipos.");
            return;
        }

        titulo("CADASTRAR TIPO DE EVENTO");

        String nome = entrada.lerTexto("Nome");
        String descricao = entrada.lerTexto("Descrição");

        resultado(tipoController.cadastrar(nome, descricao),
                "Tipo de evento cadastrado!",
                "Não foi possível cadastrar o tipo de evento.");
    }

    private void cadastrarModerador() {
        if (!moderador.isAdm()) {
            System.out.println("Somente moderadores administradores podem cadastrar moderadores.");
            return;
        }

        titulo("CADASTRAR MODERADOR");

        String nome = entrada.lerTexto("Nome");
        String email = entrada.lerTexto("E-mail");
        String senha = entrada.lerTexto("Senha");
        String nivel = entrada.lerTexto("Nível de acesso (basico/adm)");

        resultado(moderadorController.cadastrarModerador(moderador, nome, email, senha, nivel),
                "Moderador cadastrado com sucesso!",
                "Não foi possível cadastrar o moderador.");
    }
}