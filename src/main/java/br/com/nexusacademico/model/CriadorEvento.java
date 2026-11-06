package br.com.nexusacademico.model;

public class CriadorEvento extends Usuario {

    private String vinculo;
    private String cargo;

    public CriadorEvento(String nome, String email, String senhaHash, String vinculo, String cargo) {

        super(nome, email, senhaHash);

        this.vinculo = vinculo;
        this.cargo = cargo;
    }

    public CriadorEvento(int idUsuario, String nome, String email,
                         String senhaHash,
                         java.time.LocalDateTime dataCadastro,
                         String vinculo, String cargo) {

        super(idUsuario, nome, email, senhaHash, dataCadastro);

        this.vinculo = vinculo;
        this.cargo = cargo;
    }

    public String getVinculo() {
        return vinculo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = vinculo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
