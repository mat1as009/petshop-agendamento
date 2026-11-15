package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.EditarPetComando;
import com.petshop.dao.PetDAO;
import java.util.Scanner;

public class EditarPetFactory implements ComandoFactory {
    private final Scanner scanner;
    private final PetDAO petDAO;

    public EditarPetFactory(Scanner scanner, PetDAO petDAO) {
        this.scanner = scanner;
        this.petDAO = petDAO;
    }

    @Override
    public Comando criar() {
        return new EditarPetComando(scanner, petDAO);
    }
}