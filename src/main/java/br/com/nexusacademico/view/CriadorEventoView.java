package br.com.nexusacademico.view;

import br.com.nexusacademico.controller.CertificadoController;
import br.com.nexusacademico.controller.EncontroController;
import br.com.nexusacademico.controller.AlunoController;
import br.com.nexusacademico.controller.InscricaoController;
import br.com.nexusacademico.controller.PresencaController;
import br.com.nexusacademico.controller.TipoEventoController;
import br.com.nexusacademico.model.Aluno;
import br.com.nexusacademico.model.CriadorEvento;
import br.com.nexusacademico.model.EncontroEvento;
import br.com.nexusacademico.model.Evento;
import br.com.nexusacademico.model.InscricaoEvento;
import br.com.nexusacademico.model.TipoEvento;
import br.com.nexusacademico.util.EntradaConsole;

import java.time.LocalDate;
import java.util.List;

public class CriadorEventoView extends BaseView {

    private final CriadorEvento criador;
    private final TipoEventoController tipoController = new TipoEventoController();
    private final EncontroController encontroController = new EncontroController();
    private final InscricaoController inscricaoController = new InscricaoController();
    private final PresencaController presencaController = new PresencaController();
    private final CertificadoController certificadoController = new CertificadoController();
    private final AlunoController alunoController = new AlunoController();

    public CriadorEventoView(EntradaConsole entrada, CriadorEvento criador) {
        super(entrada);
        this.criador = criador;
    }

    public void exibirMenu() {
        int opcao;

        do {
            titulo("CRIADOR DE EVENTOS - " + criador.getNome());
            System.out.println("1  - Criar evento");
            System.out.println("2  - Meus eventos");
            System.out.println("3  - Editar evento");
            System.out.println("4  - Cancelar evento");
            System.out.println("5  - Reenviar evento rejeitado para avaliação");
            System.out.println("6  - Adicionar encontro a um evento");
            System.out.println("7  - Ver / remover encontros");
            System.out.println("8  - Ver inscritos de um evento");
            System.out.println("9  - Registrar presença em um encontro");
            System.out.println("10 - Encerrar evento e emitir certificados");
            System.out.println("11 - Listar tipos de evento");
            System.out.println("12 - Cadastrar tipo de evento");
            System.out.println("13 - Atualizar perfil");
            System.out.println("14 - Alterar senha");
            System.out.println("0  - Sair (logout)");

            opcao = entrada.lerOpcao();

            switch (opcao) {
                case 1 -> criarEvento();
                case 2 -> meusEventos();
                case 3 -> editarEvento();
                case 4 -> cancelarEvento();
                case 5 -> reenviarEvento();
                case 6 -> adicionarEncontro();
                case 7 -> verOuRemoverEncontros();
                case 8 -> verInscritos();
                case 9 -> registrarPresenca();
                case 10 -> encerrarEvento();
                case 11 -> listarTipos();
                case 12 -> cadastrarTipo();
                case 13 -> atualizarPerfil(criador);
                case 14 -> alterarSenha(criador);
                case 0 -> System.out.println("Sessão encerrada.");
                default -> System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    private void criarEvento() {
        titulo("CRIAR EVENTO");

        if (!listarTipos()) {
            System.out.println("Cadastre um tipo de evento primeiro (opção 12).");
            return;
        }

        int idTipo = entrada.lerInt("\nID do tipo de evento");
        String titulo = entrada.lerTexto("Título");
        String descricao = entrada.lerTexto("Descrição");
        String local = entrada.lerTexto("Local");
        int vagas = entrada.lerInt("Vagas");
        LocalDate inicio = entrada.lerData("Data de início");
        LocalDate fim = entrada.lerData("Data de fim");
        int carga = entrada.lerInt("Carga horária (horas)");
        String modalidade = entrada.lerTexto("Modalidade (presencial/online/hibrido)");

        Evento evento = eventoController.criarEvento(
                criador, idTipo, titulo, descricao, local, vagas,
                inicio, fim, carga, modalidade
        );

        if (evento != null) {
            System.out.println("\nEvento criado (ID " + evento.getIdEvento()
                    + ") e enviado para avaliação de um moderador.");
        } else {
            System.out.println("\nNão foi possível criar o evento.");
        }
    }

    private boolean meusEventos() {
        titulo("MEUS EVENTOS");

        List<Evento> eventos = eventoController.listarMeusEventos(criador);

        if (eventos.isEmpty()) {
            System.out.println("Você ainda não criou eventos.");
            return false;
        }

        eventos.forEach(this::imprimirEvento);
        return true;
    }

    private void editarEvento() {
        if (!meusEventos()) {
            return;
        }

        int id = entrada.lerInt("\nID do evento a editar");
        Evento atual = eventoController.buscarPorId(id);

        if (atual == null) {
            System.out.println("Evento não encontrado.");
            return;
        }

        System.out.println("(Enter mantém o valor atual)");

        String titulo = entrada.lerTexto("Título [" + atual.getTitulo() + "]");
        String descricao = entrada.lerTexto("Descrição");
        String local = entrada.lerTexto("Local [" + atual.getLocal() + "]");
        String vagasTxt = entrada.lerTexto("Vagas [" + atual.getVagas() + "]");
        String modalidade = entrada.lerTexto("Modalidade [" + atual.getModalidade() + "]");
        String cargaTxt = entrada.lerTexto("Carga horária [" + atual.getCargaHoraria() + "]");
        String inicioTxt = entrada.lerTexto("Data de início [" + atual.getDataInicio().format(EntradaConsole.DATA) + "]");
        String fimTxt = entrada.lerTexto("Data de fim [" + atual.getDataFim().format(EntradaConsole.DATA) + "]");

        try {
            boolean ok = eventoController.editarEvento(
                    criador, id,
                    titulo.isBlank() ? atual.getTitulo() : titulo,
                    descricao.isBlank() ? atual.getDescricao() : descricao,
                    local.isBlank() ? atual.getLocal() : local,
                    vagasTxt.isBlank() ? atual.getVagas() : Integer.parseInt(vagasTxt),
                    inicioTxt.isBlank() ? atual.getDataInicio()
                            : LocalDate.parse(inicioTxt, EntradaConsole.DATA),
                    fimTxt.isBlank() ? atual.getDataFim()
                            : LocalDate.parse(fimTxt, EntradaConsole.DATA),
                    cargaTxt.isBlank() ? atual.getCargaHoraria() : Integer.parseInt(cargaTxt),
                    modalidade.isBlank() ? atual.getModalidade() : modalidade
            );

            resultado(ok, "Evento atualizado com sucesso!",
                    "Não foi possível atualizar o evento.");

        } catch (NumberFormatException | java.time.format.DateTimeParseException erro) {
            System.out.println("\nValor inválido informado. Nada foi alterado.");
        }
    }

    private void cancelarEvento() {
        if (!meusEventos()) {
            return;
        }

        int id = entrada.lerInt("\nID do evento a cancelar");

        if (!entrada.confirmar("Isso cancela também as inscrições. Confirmar?")) {
            System.out.println("Operação abortada.");
            return;
        }

        resultado(eventoController.cancelarEvento(criador, id),
                "Evento cancelado.", "Não foi possível cancelar o evento.");
    }

    private void reenviarEvento() {
        if (!meusEventos()) {
            return;
        }

        int id = entrada.lerInt("\nID do evento rejeitado");

        resultado(eventoController.reenviarParaAvaliacao(criador, id),
                "Evento reenviado para avaliação.",
                "Não foi possível reenviar o evento.");
    }

    private void adicionarEncontro() {
        if (!meusEventos()) {
            return;
        }

        int id = entrada.lerInt("\nID do evento");
        LocalDate data = entrada.lerData("Data do encontro");
        var inicio = entrada.lerHora("Hora de início");
        var fim = entrada.lerHora("Hora de fim");

        EncontroEvento encontro =
                encontroController.adicionarEncontro(criador, id, data, inicio, fim);

        if (encontro != null) {
            System.out.println("\nEncontro adicionado (ID " + encontro.getIdEncontro() + ").");
        } else {
            System.out.println("\nNão foi possível adicionar o encontro.");
        }
    }

    private void verOuRemoverEncontros() {
        if (!meusEventos()) {
            return;
        }

        int id = entrada.lerInt("\nID do evento");

        if (!listarEncontros(id)) {
            return;
        }

        if (entrada.confirmar("Deseja remover algum encontro?")) {
            int idEncontro = entrada.lerInt("ID do encontro");

            resultado(encontroController.removerEncontro(criador, idEncontro),
                    "Encontro removido.", "Não foi possível remover o encontro.");
        }
    }

    private boolean listarEncontros(int idEvento) {
        List<EncontroEvento> encontros = encontroController.listarPorEvento(idEvento);

        if (encontros.isEmpty()) {
            System.out.println("Este evento ainda não possui encontros.");
            return false;
        }

        for (EncontroEvento e : encontros) {
            System.out.println("Encontro " + e.getIdEncontro() + " | "
                    + e.getDataEncontro().format(EntradaConsole.DATA) + " | "
                    + e.getHoraInicio().format(EntradaConsole.HORA) + " - "
                    + e.getHoraFim().format(EntradaConsole.HORA));
        }

        return true;
    }

    private void verInscritos() {
        if (!meusEventos()) {
            return;
        }

        int id = entrada.lerInt("\nID do evento");
        List<InscricaoEvento> inscritos = inscricaoController.listarInscritos(criador, id);

        if (inscritos == null) {
            return;
        }

        titulo("INSCRITOS");

        if (inscritos.isEmpty()) {
            System.out.println("Nenhum inscrito neste evento.");
            return;
        }

        for (InscricaoEvento i : inscritos) {
            imprimirInscrito(i);
        }
    }

    private void imprimirInscrito(InscricaoEvento i) {
        Aluno a = alunoController.buscarPorId(i.getIdAluno());
        String nome = a == null ? "(aluno removido)" : a.getNome() + " (RA " + a.getRa() + ")";

        System.out.println("Inscrição " + i.getIdInscricao() + " | " + nome
                + " | " + i.getStatus());
    }

    // Percorre os inscritos confirmados e pergunta a presença de cada um.
    private void registrarPresenca() {
        if (!meusEventos()) {
            return;
        }

        int idEvento = entrada.lerInt("\nID do evento");

        if (!listarEncontros(idEvento)) {
            return;
        }

        int idEncontro = entrada.lerInt("ID do encontro");

        List<InscricaoEvento> inscritos = inscricaoController.listarInscritos(criador, idEvento);

        if (inscritos == null) {
            return;
        }

        boolean algum = false;

        for (InscricaoEvento i : inscritos) {
            if (!"confirmada".equals(i.getStatus())) {
                continue;
            }

            algum = true;
            imprimirInscrito(i);

            String resp = entrada.lerTexto("  Presente? (s = presente, n = ausente, Enter = pular)");

            if (resp.isBlank()) {
                continue;
            }

            boolean ok = presencaController.registrarPresenca(
                    criador, i.getIdInscricao(), idEncontro, resp.equalsIgnoreCase("s")
            );

            System.out.println(ok ? "  registrado." : "  não foi possível registrar.");
        }

        if (!algum) {
            System.out.println("Não há inscrições confirmadas neste evento.");
        }
    }

    private void encerrarEvento() {
        if (!meusEventos()) {
            return;
        }

        int id = entrada.lerInt("\nID do evento a encerrar");

        if (!entrada.confirmar("Alunos presentes em todos os encontros recebem certificado; os demais ficam ausentes. Confirmar?")) {
            System.out.println("Operação abortada.");
            return;
        }

        int[] resumo = certificadoController.encerrarEventoEEmitirCertificados(criador, id);

        if (resumo != null) {
            System.out.println("\nEvento encerrado. Certificados emitidos: "
                    + resumo[0] + " | Ausentes: " + resumo[1]);
        } else {
            System.out.println("\nNão foi possível encerrar o evento.");
        }
    }

    private boolean listarTipos() {
        titulo("TIPOS DE EVENTO");

        List<TipoEvento> tipos = tipoController.listar();

        if (tipos.isEmpty()) {
            System.out.println("Nenhum tipo de evento cadastrado.");
            return false;
        }

        for (TipoEvento t : tipos) {
            System.out.println(t.getIdTipoEvento() + " - " + t.getNome()
                    + (t.getDescricao() == null || t.getDescricao().isBlank()
                    ? "" : " (" + t.getDescricao() + ")"));
        }

        return true;
    }

    private void cadastrarTipo() {
        titulo("CADASTRAR TIPO DE EVENTO");

        String nome = entrada.lerTexto("Nome");
        String descricao = entrada.lerTexto("Descrição");

        resultado(tipoController.cadastrar(nome, descricao),
                "Tipo de evento cadastrado!",
                "Não foi possível cadastrar o tipo de evento.");
    }
}