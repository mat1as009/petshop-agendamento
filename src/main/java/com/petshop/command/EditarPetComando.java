package com.petshop.command;

import com.petshop.builder.PetBuilder;
import com.petshop.dao.PetJdbcRepository;
import com.petshop.model.Pet;
import com.petshop.model.Sexo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class EditarPetComando implements Comando {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final Scanner scanner;
    private final PetJdbcRepository petDAO;

    public EditarPetComando(Scanner scanner, PetJdbcRepository petDAO) {
        this.scanner = scanner;
        this.petDAO = petDAO;
    }

    @Override
    public void executar() {
        List<Pet> pets = petDAO.listar();
        if (pets.isEmpty()) {
            System.out.println("Nenhum pet cadastrado.");
            return;
        }

        pets.forEach(System.out::println);
        System.out.print("ID do pet a editar: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        Optional<Pet> encontrado = petDAO.buscarPorId(id);
        if (encontrado.isEmpty()) {
            System.out.println("Não existe pet com esse ID.");
            return;
        }
        Pet atual = encontrado.get();
        System.out.println("Enter mantém o valor atual. Digite - para apagar um campo opcional.");

        try {
            Pet atualizado = new PetBuilder()
                    .id(id)
                    .nome(novoTexto("Nome", atual.getNome()))
                    .especie(novoTexto("Espécie", atual.getEspecie()))
                    .raca(novoTexto("Raça", atual.getRaca()))
                    .peso(novoPeso(atual.getPeso()))
                    .sexo(novoSexo(atual.getSexo()))
                    .dataNascimento(novaData("Data de nascimento", atual.getDataNascimento()))
                    .cor(novoTexto("Cor", atual.getCor()))
                    .castrado(novoCastrado(atual.isCastrado()))
                    .observacoes(novoTexto("Observações", atual.getObservacoes()))
                    .proximaVacina(novaData("Próxima vacina", atual.getProximaVacina()))
                    .clienteId(atual.getClienteId())
                    .build();
            petDAO.atualizar(atualizado);
            System.out.println("Pet atualizado: " + atualizado);
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida.");
        } catch (NumberFormatException e) {
            System.out.println("Peso inválido.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private String novoTexto(String rotulo, String atual) {
        String texto = perguntar(rotulo + " [" + atual + "]: ");
        if (texto.isEmpty()) {
            return atual;
        }
        return texto.equals("-") ? "" : texto;
    }

    private BigDecimal novoPeso(BigDecimal atual) {
        String exibicao = atual == null ? "sem peso" : atual + " kg";
        String texto = perguntar("Peso em kg [" + exibicao + "]: ");
        if (texto.isEmpty()) {
            return atual;
        }
        return texto.equals("-") ? null : new BigDecimal(texto.replace(',', '.'));
    }

    private Sexo novoSexo(Sexo atual) {
        String texto = perguntar("Sexo, M ou F [" + atual + "]: ");
        if (texto.isEmpty()) {
            return atual;
        }
        if (texto.equalsIgnoreCase("M")) {
            return Sexo.MACHO;
        }
        if (texto.equalsIgnoreCase("F")) {
            return Sexo.FEMEA;
        }
        throw new IllegalArgumentException("Sexo inválido.");
    }

    private boolean novoCastrado(boolean atual) {
        String texto = perguntar("Castrado? s/n [" + (atual ? "sim" : "não") + "]: ");
        if (texto.isEmpty()) {
            return atual;
        }
        return texto.equalsIgnoreCase("s");
    }

    private LocalDate novaData(String rotulo, LocalDate atual) {
        String exibicao = atual == null ? "sem data" : FORMATO.format(atual);
        String texto = perguntar(rotulo + " dd/mm/aaaa [" + exibicao + "]: ");
        if (texto.isEmpty()) {
            return atual;
        }
        return texto.equals("-") ? null : LocalDate.parse(texto, FORMATO);
    }

    private String perguntar(String pergunta) {
        System.out.print(pergunta);
        return scanner.nextLine().trim();
    }
}