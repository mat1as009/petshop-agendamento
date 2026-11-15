package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.ExcluirPetComando;
import com.petshop.dao.PetDAO;
import java.util.Scanner;

public class ExcluirPetFactory implements ComandoFactory {
    private final Scanner scanner;
    private final PetDAO petDAO;

    public ExcluirPetFactory(Scanner scanner, PetDAO petDAO) {
        this.scanner = scanner;
        this.petDAO = petDAO;
    }

    @Override
    public Comando criar() {
        return new ExcluirPetComando(scanner, petDAO);
    }
}