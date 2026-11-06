package br.com.nexusacademico.model;

import java.time.LocalDateTime;

public class Moderador extends Usuario {

    private String nivelAcesso;

    public Moderador(String nome, String email, String senhaHash, String nivelAcesso) {

        super(nome, email, senhaHash);

        this.nivelAcesso = nivelAcesso;
    }

    public Moderador(int idUsuario, String nome, String email,
                     String senhaHash, LocalDateTime dataCadastro,
                     String nivelAcesso) {

        super(idUsuario, nome, email, senhaHash, dataCadastro);

        this.nivelAcesso = nivelAcesso;
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }
}
