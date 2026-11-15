package com.petshop.factory;

import com.petshop.command.CadastrarClienteComando;
import com.petshop.command.Comando;
import com.petshop.dao.ClienteDAO;
import java.util.Scanner;

public class CadastrarClienteFactory implements ComandoFactory {
    private final Scanner scanner;
    private final ClienteDAO clienteDAO;

    public CadastrarClienteFactory(Scanner scanner, ClienteDAO clienteDAO) {
        this.scanner = scanner;
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Comando criar() {
        return new CadastrarClienteComando(scanner, clienteDAO);
    }
}