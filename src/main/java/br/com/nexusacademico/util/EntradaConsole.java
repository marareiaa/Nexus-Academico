package br.com.nexusacademico.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// Leitura de dados do console com validação de formato.
public class EntradaConsole {

    public static final DateTimeFormatter DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT);
    public static final DateTimeFormatter HORA =
            DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Scanner scanner;

    public EntradaConsole(Scanner scanner) {
        this.scanner = scanner;
    }

    public String lerTexto(String rotulo) {
        System.out.print(rotulo + ": ");
        return scanner.nextLine().trim();
    }

    public int lerInt(String rotulo) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(rotulo));
            } catch (NumberFormatException erro) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    public int lerOpcao() {
        System.out.print("Escolha uma opção: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException erro) {
            return -1;
        }
    }

    public LocalDate lerData(String rotulo) {
        while (true) {
            try {
                return LocalDate.parse(lerTexto(rotulo + " (dd/mm/aaaa)"), DATA);
            } catch (DateTimeParseException erro) {
                System.out.println("Data inválida. Use o formato dd/mm/aaaa.");
            }
        }
    }

    public LocalTime lerHora(String rotulo) {
        while (true) {
            try {
                return LocalTime.parse(lerTexto(rotulo + " (hh:mm)"), HORA);
            } catch (DateTimeParseException erro) {
                System.out.println("Hora inválida. Use o formato hh:mm.");
            }
        }
    }

    public boolean confirmar(String pergunta) {
        return lerTexto(pergunta + " (s/n)").equalsIgnoreCase("s");
    }
}