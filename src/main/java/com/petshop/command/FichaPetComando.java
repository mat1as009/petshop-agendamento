package com.petshop.command;

import com.petshop.dao.ClienteDAO;
import com.petshop.dao.PetDAO;
import com.petshop.dao.VacinaDAO;
import com.petshop.model.Cliente;
import com.petshop.model.Pet;
import com.petshop.model.Vacina;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class FichaPetComando implements Comando {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int MAXIMO_VACINAS = 5;

    private final Scanner scanner;
    private final PetDAO petDAO;
    private final ClienteDAO clienteDAO;
    private final VacinaDAO vacinaDAO;

    public FichaPetComando(Scanner scanner, PetDAO petDAO, ClienteDAO clienteDAO, VacinaDAO vacinaDAO) {
        this.scanner = scanner;
        this.petDAO = petDAO;
        this.clienteDAO = clienteDAO;
        this.vacinaDAO = vacinaDAO;
    }

    @Override
    public void executar() {
        List<Pet> pets = petDAO.listar();
        if (pets.isEmpty()) {
            System.out.println("Nenhum pet cadastrado.");
            return;
        }

        pets.forEach(System.out::println);
        System.out.print("ID do pet: ");
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
        mostrarFicha(encontrado.get());
    }

    private void mostrarFicha(Pet pet) {
        System.out.println();
        System.out.println("===== FICHA DO PET =====");
        System.out.println("Nome: " + pet.getNome());
        System.out.println("Espécie: " + pet.getEspecie());
        System.out.println("Raça: " + pet.getRaca());
        System.out.println("Sexo: " + pet.getSexo());
        System.out.println("Cor: " + pet.getCor());
        System.out.println("Castrado: " + (pet.isCastrado() ? "sim" : "não"));
        System.out.println("Peso: " + (pet.getPeso() == null ? "não informado" : pet.getPeso() + " kg"));
        System.out.println("Nascimento: " + formatar(pet.getDataNascimento()));
        System.out.println("Observações: "
                + (pet.getObservacoes().isEmpty() ? "nenhuma" : pet.getObservacoes()));
        mostrarDono(pet);
        mostrarVacinas(pet);
    }

    private void mostrarDono(Pet pet) {
        Optional<Cliente> dono = clienteDAO.buscarPorId(pet.getClienteId());
        String texto = dono.isPresent()
                ? dono.get().getNome() + " (" + dono.get().getTelefone() + ")"
                : "não encontrado";
        System.out.println("Dono: " + texto);
    }

    private void mostrarVacinas(Pet pet) {
        String situacao = pet.isVacinaVencida() ? "  (VENCIDA)" : "";
        System.out.println("Próxima vacina: " + formatar(pet.getProximaVacina()) + situacao);

        System.out.println("Últimas vacinas:");
        List<Vacina> vacinas = vacinaDAO.listarPorPet(pet.getId());
        if (vacinas.isEmpty()) {
            System.out.println("  nenhuma registrada");
            return;
        }
        vacinas.stream().limit(MAXIMO_VACINAS).forEach(v -> System.out.println("  - " + v));
    }

    private String formatar(LocalDate data) {
        return data == null ? "não informada" : data.format(FORMATO);
    }
}