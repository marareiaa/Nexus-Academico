
package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.AlunoDAO;
import br.com.nexusacademico.model.Aluno;

import java.util.List;

public class AlunoController {

    private final AlunoDAO alunoDAO;

    public AlunoController() {
        this.alunoDAO = new AlunoDAO();
    }

    // Valida os dados e solicita o cadastro do aluno.
    public boolean cadastrarAluno(
            String nome,
            String email,
            String senha,
            String ra,
            String curso,
            String periodo,
            String turma
    ) {

        if (nome == null || nome.isBlank()) {
            System.out.println("O nome é obrigatório.");
            return false;
        }

        if (email == null || email.isBlank()
                || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            System.out.println("Informe um e-mail válido.");
            return false;
        }

        if (senha == null || senha.length() < 8) {
            System.out.println("A senha deve possuir pelo menos 8 caracteres.");
            return false;
        }

        if (ra == null || ra.isBlank()) {
            System.out.println("O RA é obrigatório.");
            return false;
        }

        if (curso == null || curso.isBlank()) {
            System.out.println("O curso é obrigatório.");
            return false;
        }

        if (periodo == null || periodo.isBlank()) {
            System.out.println("O período é obrigatório.");
            return false;
        }

        if (turma == null || !turma.matches("[A-Za-z]")) {
            System.out.println("Informe uma turma válida.");
            return false;
        }

        Aluno aluno = new Aluno(
                nome,
                email,
                senha,
                ra,
                curso,
                periodo,
                turma.toUpperCase()
        );

        return alunoDAO.cadastrar(aluno);
    }

    // Consulta um aluno pelo RA.
    public Aluno buscarPorRa(String ra) {

        if (ra == null || ra.isBlank()) {
            System.out.println("Informe o RA do aluno.");
            return null;
        }

        return alunoDAO.buscarPorRa(ra);
    }

    // Retorna a lista de todos os alunos cadastrados.
    public List<Aluno> listarAlunos() {
        return alunoDAO.listarTodos();
    }

    // Valida e solicita a atualização dos dados acadêmicos.
    public boolean atualizarDadosAcademicos(
            String ra,
            String curso,
            String periodo,
            String turma
    ) {

        if (ra == null || ra.isBlank()) {
            System.out.println("O RA é obrigatório.");
            return false;
        }

        if (curso == null || curso.isBlank()) {
            System.out.println("O curso é obrigatório.");
            return false;
        }

        if (periodo == null || periodo.isBlank()) {
            System.out.println("O período é obrigatório.");
            return false;
        }

        if (turma == null || !turma.matches("[A-Za-z]")) {
            System.out.println("Informe uma turma válida.");
            return false;
        }

        Aluno aluno = alunoDAO.buscarPorRa(ra);

        if (aluno == null) {
            System.out.println("Aluno não encontrado.");
            return false;
        }

        aluno.atualizarDadosAcademicos(
                curso,
                periodo,
                turma.toUpperCase()
        );

        return alunoDAO.atualizarDadosAcademicos(aluno);
    }
}
