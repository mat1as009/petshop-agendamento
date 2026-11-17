package com.petshop.command;

import com.petshop.builder.PetBuilder;
import com.petshop.dao.ClienteJdbcRepository;
import com.petshop.dao.PetJdbcRepository;
import com.petshop.model.Cliente;
import com.petshop.model.Pet;
import com.petshop.model.Sexo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

public class CadastrarPetComando implements Comando {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final Scanner scanner;
    private final ClienteJdbcRepository clienteDAO;
    private final PetJdbcRepository petDAO;

    public CadastrarPetComando(Scanner scanner, ClienteJdbcRepository clienteDAO, PetJdbcRepository petDAO) {
        this.scanner = scanner;
        this.clienteDAO = clienteDAO;
        this.petDAO = petDAO;
    }

    @Override
    public void executar() {
        List<Cliente> clientes = clienteDAO.listar();
        if (clientes.isEmpty()) {
            System.out.println("Cadastre um cliente primeiro.");
            return;
        }

        clientes.forEach(System.out::println);
        try {
            int clienteId = lerDono();
            Pet pet = new PetBuilder()
                    .nome(perguntar("Nome do pet: "))
                    .especie(perguntar("Espécie: "))
                    .raca(perguntar("Raça: "))
                    .peso(lerPeso())
                    .sexo(lerSexo())
                    .dataNascimento(lerData("Data de nascimento dd/mm/aaaa (Enter para pular): "))
                    .cor(perguntar("Cor: "))
                    .castrado(perguntar("Castrado? (s/n): ").equalsIgnoreCase("s"))
                    .observacoes(perguntar("Observações (opcional): "))
                    .proximaVacina(lerData("Próxima vacina dd/mm/aaaa (Enter para pular): "))
                    .clienteId(clienteId)
                    .build();
            System.out.println("Pet cadastrado: " + petDAO.inserir(pet));
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida.");
        } catch (NumberFormatException e) {
            System.out.println("Número inválido.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private int lerDono() {
        int id = Integer.parseInt(perguntar("ID do dono: "));
        if (clienteDAO.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Não existe cliente com esse ID.");
        }
        return id;
    }

    private BigDecimal lerPeso() {
        String texto = perguntar("Peso em kg (Enter para pular): ");
        return texto.isEmpty() ? null : new BigDecimal(texto.replace(',', '.'));
    }

    private Sexo lerSexo() {
        String texto = perguntar("Sexo (M para macho, F para fêmea): ");
        if (texto.equalsIgnoreCase("M")) {
            return Sexo.MACHO;
        }
        if (texto.equalsIgnoreCase("F")) {
            return Sexo.FEMEA;
        }
        throw new IllegalArgumentException("Sexo inválido.");
    }

    private LocalDate lerData(String pergunta) {
        String texto = perguntar(pergunta);
        return texto.isEmpty() ? null : LocalDate.parse(texto, FORMATO);
    }

    private String perguntar(String pergunta) {
        System.out.print(pergunta);
        return scanner.nextLine().trim();
    }
}