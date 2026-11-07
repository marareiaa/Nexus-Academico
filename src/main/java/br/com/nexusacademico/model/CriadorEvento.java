package br.com.nexusacademico.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CriadorEvento extends Usuario {

    // Valores aceitos pela coluna organizador.vinculo (ENUM no banco).
    private static final List<String> VINCULOS_VALIDOS =
            List.of("professor", "coordenador", "membro_liga");

    // Corresponde a organizador.vinculo.
    private String vinculo;

    // Corresponde a organizador.cargo.
    private String cargo;

    // Construtor para cadastrar um novo criador de eventos.
    public CriadorEvento(
            String nome,
            String email,
            String senhaHash,
            String vinculo,
            String cargo
    ) {
        super(nome, email, senhaHash);

        this.vinculo = validarVinculo(vinculo);
        this.cargo = cargo;
    }

    // Construtor para recuperar um criador do banco de dados.
    public CriadorEvento(
            int idUsuario,
            String nome,
            String email,
            String senhaHash,
            LocalDateTime dataCadastro,
            String vinculo,
            String cargo
    ) {
        super(idUsuario, nome, email, senhaHash, dataCadastro);

        this.vinculo = validarVinculo(vinculo);
        this.cargo = cargo;
    }

    private static String validarVinculo(String vinculo) {
        if (vinculo == null || !VINCULOS_VALIDOS.contains(vinculo)) {
            throw new IllegalArgumentException(
                    "Vínculo inválido. Use: professor, coordenador ou membro_liga."
            );
        }
        return vinculo;
    }

    public String getVinculo() {
        return vinculo;
    }

    public void setVinculo(String vinculo) {
        this.vinculo = validarVinculo(vinculo);
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    // Implementa a autenticação herdada de Usuario.
    @Override
    public boolean autenticar(String senhaInformada) {
        return super.autenticar(senhaInformada);
    }

    // Cria um evento vinculado a este criador (nasce com status "pendente").
    public Evento criarEvento(
            int idTipoEvento,
            String titulo,
            String descricao,
            String local,
            int vagas,
            LocalDate dataInicio,
            LocalDate dataFim,
            int cargaHoraria,
            String modalidade
    ) {
        return new Evento(
                getIdUsuario(), idTipoEvento, titulo, descricao, local,
                vagas, dataInicio, dataFim, cargaHoraria, modalidade
        );
    }

    // Edita um evento que pertença a este criador.
    public void editarEvento(
            Evento evento,
            String titulo,
            String descricao,
            String local,
            int vagas,
            LocalDate dataInicio,
            LocalDate dataFim,
            int cargaHoraria,
            String modalidade
    ) {
        verificarPropriedade(evento);

        evento.atualizar(
                titulo, descricao, local, vagas,
                dataInicio, dataFim, cargaHoraria, modalidade
        );
    }

    // Cancela um evento que pertença a este criador.
    public void cancelarEvento(Evento evento) {
        verificarPropriedade(evento);
        evento.cancelar();
    }

    // Registra a presença de um aluno em um encontro do evento.
    public Presenca registrarPresenca(InscricaoEvento inscricao, int idEncontro) {
        return inscricao.marcarPresenca(idEncontro);
    }

    private void verificarPropriedade(Evento evento) {
        if (evento == null || evento.getIdOrganizador() != getIdUsuario()) {
            throw new IllegalStateException(
                    "Este evento não pertence ao criador informado."
            );
        }
    }
}