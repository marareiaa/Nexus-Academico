package br.com.nexusacademico.model;

public class TipoEvento {

    private int idTipoEvento;
    private String nome;
    private String descricao;

    public TipoEvento(String nome, String descricao) {
        this.nome = validarNome(nome);
        this.descricao = validarDescricao(descricao);
    }

    public TipoEvento(int idTipoEvento, String nome, String descricao) {
        this(nome, descricao);
        this.idTipoEvento = idTipoEvento;
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank() || nome.length() > 100) {
            throw new IllegalArgumentException(
                    "O nome do tipo de evento é obrigatório (até 100 caracteres)."
            );
        }
        return nome;
    }

    private static String validarDescricao(String descricao) {
        if (descricao != null && descricao.length() > 200) {
            throw new IllegalArgumentException(
                    "A descrição deve ter no máximo 200 caracteres."
            );
        }
        return descricao;
    }

    public int getIdTipoEvento() {
        return idTipoEvento;
    }

    public void setIdTipoEvento(int idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void atualizar(String nome, String descricao) {
        this.nome = validarNome(nome);
        this.descricao = validarDescricao(descricao);
    }
}