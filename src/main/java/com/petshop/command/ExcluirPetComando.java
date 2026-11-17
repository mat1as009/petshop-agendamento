package com.petshop.command;

import com.petshop.dao.PetJdbcRepository;
import com.petshop.model.Pet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ExcluirPetComando implements Comando {
    private final Scanner scanner;
    private final PetJdbcRepository petDAO;

    public ExcluirPetComando(Scanner scanner, PetJdbcRepository petDAO) {
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
        System.out.print("ID do pet a excluir: ");
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

        System.out.print("Excluir " + encontrado.get().getNome() + "? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            petDAO.excluir(id);
            System.out.println("Pet excluído.");
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}