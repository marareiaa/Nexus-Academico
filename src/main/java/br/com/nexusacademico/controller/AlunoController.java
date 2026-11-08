package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.AlunoDAO;
import br.com.nexusacademico.dao.UsuarioDAO;
import br.com.nexusacademico.model.Aluno;
import br.com.nexusacademico.util.ValidacaoUtil;

import java.util.List;

public class AlunoController {

    private final AlunoDAO alunoDAO;
    private final UsuarioDAO usuarioDAO;

    public AlunoController() {
        this.alunoDAO = new AlunoDAO();
        this.usuarioDAO = new UsuarioDAO();
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

        if (ValidacaoUtil.textoVazio(nome)) {
            System.out.println("O nome é obrigatório.");
            return false;
        }

        if (!ValidacaoUtil.emailValido(email)) {
            System.out.println("Informe um e-mail válido.");
            return false;
        }

        if (!ValidacaoUtil.senhaValida(senha)) {
            System.out.println("A senha deve possuir pelo menos 8 caracteres.");
            return false;
        }

        if (ValidacaoUtil.textoVazio(ra)) {
            System.out.println("O RA é obrigatório.");
            return false;
        }

        if (ValidacaoUtil.textoVazio(curso)) {
            System.out.println("O curso é obrigatório.");
            return false;
        }

        if (ValidacaoUtil.textoVazio(periodo)) {
            System.out.println("O período é obrigatório.");
            return false;
        }

        if (turma == null || !turma.matches("[A-Za-z]")) {
            System.out.println("Informe uma turma válida.");
            return false;
        }

        if (usuarioDAO.emailExiste(email)) {
            System.out.println("Já existe um usuário com este e-mail.");
            return false;
        }

        if (alunoDAO.buscarPorRa(ra) != null) {
            System.out.println("Já existe um aluno com este RA.");
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

    // Consulta um aluno pelo identificador.
    public Aluno buscarPorId(int idAluno) {
        return alunoDAO.buscarPorId(idAluno);
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

        if (ValidacaoUtil.textoVazio(ra)) {
            System.out.println("O RA é obrigatório.");
            return false;
        }

        if (ValidacaoUtil.textoVazio(curso)) {
            System.out.println("O curso é obrigatório.");
            return false;
        }

        if (ValidacaoUtil.textoVazio(periodo)) {
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