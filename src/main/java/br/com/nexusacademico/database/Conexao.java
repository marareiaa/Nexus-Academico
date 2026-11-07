
package br.com.nexusacademico.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL =
            "jdbc:mysql://localhost:3307/nexus_academico";

    private static final String USUARIO = "nexus_app";

    private static final String SENHA = "NexusSenha2026!";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
