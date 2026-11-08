package br.com.nexusacademico.view;

import br.com.nexusacademico.controller.EventoController;
import br.com.nexusacademico.controller.UsuarioController;
import br.com.nexusacademico.model.Evento;
import br.com.nexusacademico.model.Usuario;
import br.com.nexusacademico.util.EntradaConsole;

// Recursos comuns aos menus de cada perfil.
abstract class BaseView {

    protected final EntradaConsole entrada;
    protected final UsuarioController usuarioController = new UsuarioController();
    protected final EventoController eventoController = new EventoController();

    protected BaseView(EntradaConsole entrada) {
        this.entrada = entrada;
    }

    protected void titulo(String texto) {
        System.out.println("\n================================");
        System.out.println(" " + texto);
        System.out.println("================================");
    }

    protected void resultado(boolean sucesso, String ok, String erro) {
        System.out.println(sucesso ? "\n" + ok : "\n" + erro);
    }

    protected void imprimirEvento(Evento e) {
        System.out.println("----------------------------------------");
        System.out.println("ID: " + e.getIdEvento() + " | " + e.getTitulo());
        System.out.println("Status: " + e.getStatus()
                + " | Modalidade: " + e.getModalidade());
        System.out.println("Local: " + e.getLocal());
        System.out.println("Período: "
                + e.getDataInicio().format(EntradaConsole.DATA) + " a "
                + e.getDataFim().format(EntradaConsole.DATA)
                + " | Carga horária: " + e.getCargaHoraria() + "h");
        System.out.println("Vagas disponíveis: "
                + eventoController.vagasRestantes(e) + " de " + e.getVagas());

        if (e.getDescricao() != null && !e.getDescricao().isBlank()) {
            System.out.println("Descrição: " + e.getDescricao());
        }
    }

    protected void atualizarPerfil(Usuario usuario) {
        titulo("ATUALIZAR PERFIL");

        String nome = entrada.lerTexto("Nome [" + usuario.getNome() + "]");
        String email = entrada.lerTexto("E-mail [" + usuario.getEmail() + "]");

        if (nome.isBlank()) {
            nome = usuario.getNome();
        }

        if (email.isBlank()) {
            email = usuario.getEmail();
        }

        resultado(
                usuarioController.atualizarPerfil(usuario, nome, email),
                "Perfil atualizado com sucesso!",
                "Não foi possível atualizar o perfil."
        );
    }

    protected void alterarSenha(Usuario usuario) {
        titulo("ALTERAR SENHA");

        String atual = entrada.lerTexto("Senha atual");
        String nova = entrada.lerTexto("Nova senha");

        resultado(
                usuarioController.alterarSenha(usuario, atual, nova),
                "Senha alterada com sucesso!",
                "Não foi possível alterar a senha."
        );
    }
}