package br.com.nexusacademico.model;

public class Aluno extends Usuario {

    private String matricula;

    public Aluno(String nome, String email,String senhaHash, String matricula){
        super(nome, email, senhaHash);
        this.matricula = matricula;
    }
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

   // public void visualizarEventos() {}

    //public void inscrever(int evento) {}

    //public void cancelarInscricao() {}

    //public void visualizarCertificados() {}

    @Override
    public boolean autenticar(String senhaInformada) {
        return getSenhaHash().equals(senhaInformada);
    }


}
