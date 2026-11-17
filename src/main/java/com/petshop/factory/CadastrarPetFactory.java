package com.petshop.factory;

import com.petshop.command.CadastrarPetComando;
import com.petshop.command.Comando;
import com.petshop.dao.ClienteJdbcRepository;
import com.petshop.dao.PetJdbcRepository;
import java.util.Scanner;

public class CadastrarPetFactory implements ComandoFactory {
    private final Scanner scanner;
    private final ClienteJdbcRepository clienteDAO;
    private final PetJdbcRepository petDAO;

    public CadastrarPetFactory(Scanner scanner, ClienteJdbcRepository clienteDAO, PetJdbcRepository petDAO) {
        this.scanner = scanner;
        this.clienteDAO = clienteDAO;
        this.petDAO = petDAO;
    }

    @Override
    public Comando criar() {
        return new CadastrarPetComando(scanner, clienteDAO, petDAO);
    }
}