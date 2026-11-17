package com.petshop.command;

import com.petshop.builder.ClienteBuilder;
import com.petshop.dao.ClienteJdbcRepository;
import com.petshop.model.Cliente;
import com.petshop.model.Endereco;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

public class CadastrarClienteComando implements Comando {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final Scanner scanner;
    private final ClienteJdbcRepository clienteDAO;

    public CadastrarClienteComando(Scanner scanner, ClienteJdbcRepository clienteDAO) {
        this.scanner = scanner;
        this.clienteDAO = clienteDAO;
    }

    @Override
    public void executar() {
        try {
            Cliente cliente = new ClienteBuilder()
                    .nome(perguntar("Nome: "))
                    .cpf(perguntar("CPF (11 números): "))
                    .dataNascimento(lerData("Data de nascimento dd/mm/aaaa (Enter para pular): "))
                    .telefone(perguntar("Telefone: "))
                    .telefoneSecundario(perguntar("Telefone secundário (opcional): "))
                    .email(perguntar("Email (opcional): "))
                    .observacoes(perguntar("Observações (opcional): "))
                    .endereco(lerEndereco())
                    .build();
            System.out.println("Cliente cadastrado: " + clienteDAO.inserir(cliente));
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private Endereco lerEndereco() {
        System.out.println("Endereço:");
        return new Endereco(
                perguntar("  CEP (8 números): "),
                perguntar("  Logradouro: "),
                perguntar("  Número: "),
                perguntar("  Complemento (opcional): "),
                perguntar("  Bairro: "),
                perguntar("  Cidade: "),
                perguntar("  Estado (UF): "));
    }

    private String perguntar(String pergunta) {
        System.out.print(pergunta);
        return scanner.nextLine().trim();
    }

    private LocalDate lerData(String pergunta) {
        String texto = perguntar(pergunta);
        return texto.isEmpty() ? null : LocalDate.parse(texto, FORMATO);
    }
}