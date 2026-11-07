package br.com.nexusacademico.util;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaUtil {

    // Gera um hash seguro para a senha informada.
    public static String gerarHash(String senha) {

        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException(
                    "A senha não pode ser vazia."
            );
        }

        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

    // Verifica se a senha informada corresponde ao hash armazenado.
    public static boolean verificarSenha(
            String senhaInformada,
            String hashArmazenado
    ) {

        if (senhaInformada == null || hashArmazenado == null) {
            return false;
        }

        return BCrypt.checkpw(senhaInformada, hashArmazenado);
    }
}