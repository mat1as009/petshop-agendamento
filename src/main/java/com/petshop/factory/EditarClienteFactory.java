package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.EditarClienteComando;
import com.petshop.dao.ClienteDAO;
import java.util.Scanner;

public class EditarClienteFactory implements ComandoFactory {
    private final Scanner scanner;
    private final ClienteDAO clienteDAO;

    public EditarClienteFactory(Scanner scanner, ClienteDAO clienteDAO) {
        this.scanner = scanner;
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Comando criar() {
        return new EditarClienteComando(scanner, clienteDAO);
    }
}