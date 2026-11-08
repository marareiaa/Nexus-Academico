package br.com.nexusacademico.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    // Os valores podem ser sobrescritos por variáveis de ambiente
    // (NEXUS_DB_URL, NEXUS_DB_USUARIO, NEXUS_DB_SENHA).
    private static final String URL = valor(
            "NEXUS_DB_URL",
            "jdbc:mysql://localhost:3307/nexus_academico"
    );

    private static final String USUARIO = valor("NEXUS_DB_USUARIO", "nexus_app");

    private static final String SENHA = valor("NEXUS_DB_SENHA", "NexusSenha2026!");

    private static String valor(String chave, String padrao) {
        String valor = System.getenv(chave);
        return (valor == null || valor.isBlank()) ? padrao : valor;
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}