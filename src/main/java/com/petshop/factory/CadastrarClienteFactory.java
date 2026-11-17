package com.petshop.factory;

import com.petshop.command.CadastrarClienteComando;
import com.petshop.command.Comando;
import com.petshop.dao.ClienteJdbcRepository;
import java.util.Scanner;

public class CadastrarClienteFactory implements ComandoFactory {
    private final Scanner scanner;
    private final ClienteJdbcRepository clienteDAO;

    public CadastrarClienteFactory(Scanner scanner, ClienteJdbcRepository clienteDAO) {
        this.scanner = scanner;
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Comando criar() {
        return new CadastrarClienteComando(scanner, clienteDAO);
    }
}