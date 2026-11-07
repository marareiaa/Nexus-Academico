
package br.com.nexusacademico.model;

import br.com.nexusacademico.util.SenhaUtil;

import java.time.LocalDateTime;

public class Usuario {

    // Atributos da classe Usuario.
    private int idUsuario;
    private String nome;
    private String email;
    private String senhaHash;
    private LocalDateTime dataCadastro;
    private boolean ativo;

    // Construtor utilizado no cadastro de um novo usuário.
    public Usuario(String nome, String email, String senhaHash) {

        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
    }

    // Construtor utilizado para recuperar um usuário do banco de dados.
    public Usuario(
            int idUsuario,
            String nome,
            String email,
            String senhaHash,
            LocalDateTime dataCadastro
    ) {

        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.dataCadastro = dataCadastro;
        this.ativo = true;
    }

    // Construtor completo, incluindo o estado de ativação.
    public Usuario(
            int idUsuario,
            String nome,
            String email,
            String senhaHash,
            LocalDateTime dataCadastro,
            boolean ativo
    ) {

        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.dataCadastro = dataCadastro;
        this.ativo = ativo;
    }

    // Retorna o identificador do usuário.
    public int getIdUsuario() {
        return idUsuario;
    }

    // Retorna o nome do usuário.
    public String getNome() {
        return nome;
    }

    // Retorna o e-mail do usuário.
    public String getEmail() {
        return email;
    }

    // Retorna o hash da senha armazenada.
    public String getSenhaHash() {
        return senhaHash;
    }

    // Retorna a data de cadastro.
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    // Informa se o usuário está ativo.
    public boolean isAtivo() {
        return ativo;
    }

    // Atualiza o identificador do usuário.
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Atualiza o nome do usuário.
    public void setNome(String nome) {
        this.nome = nome;
    }

    // Atualiza o e-mail do usuário.
    public void setEmail(String email) {
        this.email = email;
    }

    // Atualiza o hash da senha.
    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    // Atualiza a data de cadastro.
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    // Altera o estado de ativação do usuário.
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    // Atualiza as informações básicas do perfil.
    public void atualizarPerfil(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    // Verifica a senha e impede a autenticação de usuários inativos.
    public boolean autenticar(String senhaInformada) {

        if (!ativo || senhaInformada == null || senhaHash == null) {
            return false;
        }

        return SenhaUtil.verificarSenha(senhaInformada, senhaHash);
    }

    // Atualiza a senha utilizando um hash previamente gerado.
    public void alterarSenha(String novaSenhaHash) {

        if (novaSenhaHash == null || novaSenhaHash.isBlank()) {
            throw new IllegalArgumentException(
                    "O hash da nova senha não pode ser vazio."
            );
        }

        this.senhaHash = novaSenhaHash;
    }
}
