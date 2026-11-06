package br.com.nexusacademico.model;
import java.time.LocalDateTime;

public class Usuario {

    private int idUsuario;
    private String nome;
    private String email;
    private String senhaHash;
    private LocalDateTime dataCadastro;

    public Usuario(String nome, String email, String senhaHash) {

        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.dataCadastro = LocalDateTime.now();
    }

    public Usuario(int idUsuario, String nome, String email,
                   String senhaHash, LocalDateTime dataCadastro) {

        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.dataCadastro = dataCadastro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public void atualizarPerfil(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public boolean autenticar(String senhaInformada) {
        return false;
    }

    public void alterarSenha(String novaSenhaHash) {
        this.senhaHash = novaSenhaHash;
    }
}
