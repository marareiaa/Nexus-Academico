package br.com.nexusacademico;

import br.com.nexusacademico.database.Conexao;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try (Connection conexao = Conexao.conectar()) {

            System.out.println("Conexão estabelecida com sucesso!");
            System.out.println("Banco: " + conexao.getCatalog());

        } catch (SQLException erro) {

            System.out.println("Não foi possível conectar ao banco.");
            System.out.println("Erro: " + erro.getMessage());

        }
    }
}
