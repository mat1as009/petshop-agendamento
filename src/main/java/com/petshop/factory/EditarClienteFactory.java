package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.EditarClienteComando;
import com.petshop.dao.ClienteJdbcRepository;
import java.util.Scanner;

public class EditarClienteFactory implements ComandoFactory {
    private final Scanner scanner;
    private final ClienteJdbcRepository clienteDAO;

    public EditarClienteFactory(Scanner scanner, ClienteJdbcRepository clienteDAO) {
        this.scanner = scanner;
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Comando criar() {
        return new EditarClienteComando(scanner, clienteDAO);
    }
}