
package br.com.nexusacademico.view;

import br.com.nexusacademico.controller.UsuarioController;
import br.com.nexusacademico.controller.AlunoController;
import br.com.nexusacademico.model.Usuario;
import br.com.nexusacademico.model.Aluno;

import java.util.Scanner;

public class UsuarioView {

    private final Scanner scanner;
    private final UsuarioController usuarioController;
    private final AlunoController alunoController;

    public UsuarioView() {
        this.scanner = new Scanner(System.in);
        this.usuarioController = new UsuarioController();
        this.alunoController = new AlunoController();
    }

    // Exibe o menu principal e direciona as opções escolhidas.
    public void exibirMenu() {
        int opcao;

        do {
            System.out.println("\n================================");
            System.out.println("       NEXUS ACADÊMICO");
            System.out.println("================================");
            System.out.println("1 - Cadastrar aluno");
            System.out.println("2 - Realizar login");
            System.out.println("3 - Listar alunos");
            System.out.println("4 - Buscar aluno por RA");
            System.out.println("5 - Atualizar dados acadêmicos");
            System.out.println("0 - Sair");
            System.out.println("================================");
            System.out.print("Escolha uma opção: ");

            String entrada = scanner.nextLine();

            try {
                opcao = Integer.parseInt(entrada);
            } catch (NumberFormatException erro) {
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> cadastrarAluno();
                case 2 -> realizarLogin();
                case 3 -> listarAlunos();
                case 4 -> buscarAlunoPorRa();
                case 5 -> atualizarDadosAcademicos();
                case 0 -> System.out.println(
                        "Encerrando o Nexus Acadêmico..."
                );
                default -> System.out.println(
                        "Opção inválida. Tente novamente."
                );
            }

        } while (opcao != 0);
    }

    // Coleta os dados e solicita o cadastro de um novo aluno.
    private void cadastrarAluno() {
        System.out.println("\n--- CADASTRO DE ALUNO ---");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        System.out.print("RA: ");
        String ra = scanner.nextLine();

        System.out.print("Curso: ");
        String curso = scanner.nextLine();

        System.out.print("Período: ");
        String periodo = scanner.nextLine();

        System.out.print("Turma: ");
        String turma = scanner.nextLine();

        boolean cadastrado = alunoController.cadastrarAluno(
                nome, email, senha, ra, curso, periodo, turma
        );

        if (cadastrado) {
            System.out.println("\nAluno cadastrado com sucesso!");
        } else {
            System.out.println("\nNão foi possível cadastrar o aluno.");
        }
    }

    // Realiza a autenticação de um usuário.
    private void realizarLogin() {
        System.out.println("\n--- LOGIN ---");

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Usuario usuario = usuarioController.realizarLogin(email, senha);

        if (usuario != null) {
            System.out.println("\nLogin realizado com sucesso!");
            System.out.println("Bem-vindo(a), " + usuario.getNome() + "!");
        } else {
            System.out.println("\nE-mail ou senha inválidos.");
        }
    }

    // Consulta e apresenta todos os alunos cadastrados.
    private void listarAlunos() {
        System.out.println("\n--- ALUNOS CADASTRADOS ---");

        var alunos = alunoController.listarAlunos();

        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }

        for (var aluno : alunos) {
            System.out.println("----------------------------");
            System.out.println("Nome: " + aluno.getNome());
            System.out.println("E-mail: " + aluno.getEmail());
            System.out.println("RA: " + aluno.getRa());
            System.out.println("Curso: " + aluno.getCurso());
            System.out.println("Período: " + aluno.getPeriodo());
            System.out.println("Turma: " + aluno.getTurma());
        }

        System.out.println("----------------------------");
        System.out.println("Total de alunos: " + alunos.size());
    }

    // Localiza um aluno utilizando seu Registro Acadêmico.
    private void buscarAlunoPorRa() {
        System.out.println("\n--- BUSCAR ALUNO ---");

        System.out.print("Informe o RA do aluno: ");
        String ra = scanner.nextLine();

        Aluno aluno = alunoController.buscarPorRa(ra);

        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
            return;
        }

        System.out.println("\n--- DADOS DO ALUNO ---");
        System.out.println("Nome: " + aluno.getNome());
        System.out.println("E-mail: " + aluno.getEmail());
        System.out.println("RA: " + aluno.getRa());
        System.out.println("Curso: " + aluno.getCurso());
        System.out.println("Período: " + aluno.getPeriodo());
        System.out.println("Turma: " + aluno.getTurma());
    }

    // Coleta os novos dados acadêmicos e solicita a atualização.
    private void atualizarDadosAcademicos() {
        System.out.println("\n--- ATUALIZAR DADOS ACADÊMICOS ---");

        System.out.print("RA do aluno: ");
        String ra = scanner.nextLine();

        System.out.print("Novo curso: ");
        String curso = scanner.nextLine();

        System.out.print("Novo período: ");
        String periodo = scanner.nextLine();

        System.out.print("Nova turma: ");
        String turma = scanner.nextLine();

        boolean atualizado = alunoController.atualizarDadosAcademicos(
                ra, curso, periodo, turma
        );

        if (atualizado) {
            System.out.println("\nDados acadêmicos atualizados com sucesso!");
        } else {
            System.out.println("\nNão foi possível atualizar os dados.");
        }
    }
}
