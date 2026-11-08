package br.com.nexusacademico.controller;

import br.com.nexusacademico.dao.TipoEventoDAO;
import br.com.nexusacademico.model.TipoEvento;

import java.util.List;

public class TipoEventoController {

    private final TipoEventoDAO tipoDAO = new TipoEventoDAO();

    public boolean cadastrar(String nome, String descricao) {

        try {
            TipoEvento tipo = new TipoEvento(nome, descricao);

            if (tipoDAO.nomeExiste(tipo.getNome())) {
                System.out.println("Já existe um tipo de evento com este nome.");
                return false;
            }

            return tipoDAO.cadastrar(tipo);

        } catch (IllegalArgumentException erro) {
            System.out.println(erro.getMessage());
            return false;
        }
    }

    public List<TipoEvento> listar() {
        return tipoDAO.listarTodos();
    }
}