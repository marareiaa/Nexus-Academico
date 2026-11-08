package br.com.nexusacademico.view;

import br.com.nexusacademico.controller.AlunoController;
import br.com.nexusacademico.controller.CertificadoController;
import br.com.nexusacademico.controller.CriadorEventoController;
import br.com.nexusacademico.controller.ModeradorController;
import br.com.nexusacademico.model.Aluno;
import br.com.nexusacademico.model.CertificadoEvento;
import br.com.nexusacademico.model.CriadorEvento;
import br.com.nexusacademico.model.Moderador;
import br.com.nexusacademico.model.Usuario;
import br.com.nexusacademico.util.EntradaConsole;

import java.util.Scanner;

// Menu inicial: cadastro, login e validação pública de certificados.
public class UsuarioView extends BaseView {

    private final AlunoController alunoController = new AlunoController();
    private final CriadorEventoController criadorController = new CriadorEventoController();
    private final ModeradorController moderadorController = new ModeradorController();
    private final CertificadoController certificadoController = new CertificadoController();

    public UsuarioView() {
        super(new EntradaConsole(new Scanner(System.in)));
    }

    // Exibe o menu principal e direciona as opções escolhidas.
    public void exibirMenu() {
        int opcao;

        do {
            titulo("NEXUS ACADÊMICO");
            System.out.println("1 - Realizar login");
            System.out.println("2 - Cadastrar aluno");
            System.out.println("3 - Cadastrar criador de eventos");
            System.out.println("4 - Validar certificado");
            if (!moderadorController.existeModerador()) {
                System.out.println("5 - Primeiro acesso: cadastrar moderador administrador");
            }
            System.out.println("0 - Sair");

            opcao = entrada.lerOpcao();

            switch (opcao) {
                case 1 -> realizarLogin();
                case 2 -> cadastrarAluno();
                case 3 -> cadastrarCriador();
                case 4 -> validarCertificado();
                case 5 -> primeiroAcesso();
                case 0 -> System.out.println("Encerrando o Nexus Acadêmico...");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }

        } while (opcao != 0);
    }

    private void cadastrarAluno() {
        titulo("CADASTRO DE ALUNO");

        String nome = entrada.lerTexto("Nome");
        String email = entrada.lerTexto("E-mail");
        String senha = entrada.lerTexto("Senha");
        String ra = entrada.lerTexto("RA");
        String curso = entrada.lerTexto("Curso");
        String periodo = entrada.lerTexto("Período");
        String turma = entrada.lerTexto("Turma");

        resultado(
                alunoController.cadastrarAluno(nome, email, senha, ra, curso, periodo, turma),
                "Aluno cadastrado com sucesso!",
                "Não foi possível cadastrar o aluno."
        );
    }

    private void cadastrarCriador() {
        titulo("CADASTRO DE CRIADOR DE EVENTOS");

        String nome = entrada.lerTexto("Nome");
        String email = entrada.lerTexto("E-mail");
        String senha = entrada.lerTexto("Senha");
        String vinculo = entrada.lerTexto("Vínculo (professor/coordenador/membro_liga)");
        String cargo = entrada.lerTexto("Cargo");

        resultado(
                criadorController.cadastrarCriador(nome, email, senha, vinculo, cargo),
                "Criador de eventos cadastrado com sucesso!",
                "Não foi possível realizar o cadastro."
        );
    }

    private void primeiroAcesso() {
        titulo("PRIMEIRO ACESSO - MODERADOR ADMINISTRADOR");

        String nome = entrada.lerTexto("Nome");
        String email = entrada.lerTexto("E-mail");
        String senha = entrada.lerTexto("Senha");

        resultado(
                moderadorController.cadastrarModerador(null, nome, email, senha, "adm"),
                "Moderador administrador cadastrado com sucesso!",
                "Não foi possível realizar o cadastro."
        );
    }

    // Autentica e abre o menu do perfil correspondente.
    private void realizarLogin() {
        titulo("LOGIN");

        String email = entrada.lerTexto("E-mail");
        String senha = entrada.lerTexto("Senha");

        Usuario usuario = usuarioController.realizarLogin(email, senha);

        if (usuario == null) {
            System.out.println("\nE-mail ou senha inválidos.");
            return;
        }

        System.out.println("\nLogin realizado com sucesso!");
        System.out.println("Bem-vindo(a), " + usuario.getNome() + "!");

        if (usuario instanceof Aluno aluno) {
            new AlunoView(entrada, aluno).exibirMenu();
        } else if (usuario instanceof CriadorEvento criador) {
            new CriadorEventoView(entrada, criador).exibirMenu();
        } else if (usuario instanceof Moderador moderador) {
            new ModeradorView(entrada, moderador).exibirMenu();
        } else {
            System.out.println("Este usuário não possui um perfil de acesso.");
        }
    }

    private void validarCertificado() {
        titulo("VALIDAR CERTIFICADO");

        String codigo = entrada.lerTexto("Código de verificação");

        CertificadoEvento c = certificadoController.validar(codigo);

        if (c == null) {
            System.out.println("\nCertificado INVÁLIDO ou não encontrado.");
            return;
        }

        System.out.println("\nCertificado VÁLIDO.");
        System.out.println("Evento: " + certificadoController.tituloDoEvento(c));
        System.out.println("Carga horária: " + c.getCargaHoraria() + "h");
        System.out.println("Emitido em: "
                + c.getDataEmissao().format(EntradaConsole.DATA_HORA));
    }
}