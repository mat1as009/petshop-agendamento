package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.ExcluirPetComando;
import com.petshop.dao.PetJdbcRepository;
import java.util.Scanner;

public class ExcluirPetFactory implements ComandoFactory {
    private final Scanner scanner;
    private final PetJdbcRepository petDAO;

    public ExcluirPetFactory(Scanner scanner, PetJdbcRepository petDAO) {
        this.scanner = scanner;
        this.petDAO = petDAO;
    }

    @Override
    public Comando criar() {
        return new ExcluirPetComando(scanner, petDAO);
    }
}