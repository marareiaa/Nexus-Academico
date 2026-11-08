package br.com.nexusacademico.util;

import java.util.regex.Pattern;

public class ValidacaoUtil {

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static boolean textoVazio(String texto) {
        return texto == null || texto.isBlank();
    }

    public static boolean emailValido(String email) {
        return email != null && EMAIL.matcher(email).matches();
    }

    public static boolean senhaValida(String senha) {
        return senha != null && senha.length() >= 8;
    }
}