package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.ExcluirClienteComando;
import com.petshop.dao.ClienteDAO;
import com.petshop.dao.PetDAO;
import java.util.Scanner;

public class ExcluirClienteFactory implements ComandoFactory {
    private final Scanner scanner;
    private final ClienteDAO clienteDAO;
    private final PetDAO petDAO;

    public ExcluirClienteFactory(Scanner scanner, ClienteDAO clienteDAO, PetDAO petDAO) {
        this.scanner = scanner;
        this.clienteDAO = clienteDAO;
        this.petDAO = petDAO;
    }

    @Override
    public Comando criar() {
        return new ExcluirClienteComando(scanner, clienteDAO, petDAO);
    }
}