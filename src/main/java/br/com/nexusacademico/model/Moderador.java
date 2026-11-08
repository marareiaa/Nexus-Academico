package br.com.nexusacademico.model;

import java.time.LocalDateTime;

public class Moderador extends Usuario {

    // Valores aceitos pela coluna moderador.nivel_acesso (ENUM no banco).
    private String nivelAcesso;

    public Moderador(String nome, String email, String senhaHash,
                     String nivelAcesso) {

        super(nome, email, senhaHash);

        this.nivelAcesso = validarNivel(nivelAcesso);
    }

    public Moderador(int idUsuario, String nome, String email,
                     String senhaHash, LocalDateTime dataCadastro,
                     String nivelAcesso) {

        super(idUsuario, nome, email, senhaHash, dataCadastro);

        this.nivelAcesso = validarNivel(nivelAcesso);
    }

    private static String validarNivel(String nivelAcesso) {
        if (!"basico".equals(nivelAcesso) && !"adm".equals(nivelAcesso)) {
            throw new IllegalArgumentException(
                    "Nível de acesso inválido. Use: basico ou adm."
            );
        }
        return nivelAcesso;
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = validarNivel(nivelAcesso);
    }

    // Moderadores administradores também gerenciam moderadores e tipos de evento.
    public boolean isAdm() {
        return "adm".equals(nivelAcesso);
    }

    @Override
    public boolean autenticar(String senhaInformada) {
        return super.autenticar(senhaInformada);
    }

    // Operação de listagem dos eventos pendentes de avaliação.
    public void visualizarPendentes() {
        // A consulta e a apresentação são feitas por ModeradorController e ModeradorView.
    }

    // Aprova o evento e devolve o registro da avaliação.
    public AvaliacaoEvento aprovarEvento(Evento evento) {
        evento.aprovar();
        return new AvaliacaoEvento(getIdUsuario(), evento.getIdEvento(), "aprovado");
    }

    // Rejeita o evento e devolve o registro da avaliação.
    public AvaliacaoEvento rejeitarEvento(Evento evento) {
        evento.rejeitar();
        return new AvaliacaoEvento(getIdUsuario(), evento.getIdEvento(), "rejeitado");
    }
}