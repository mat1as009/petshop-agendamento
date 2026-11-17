package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.FichaPetComando;
import com.petshop.dao.ClienteJdbcRepository;
import com.petshop.dao.PetJdbcRepository;
import com.petshop.dao.VacinaJdbcRepository;
import java.util.Scanner;

public class FichaPetFactory implements ComandoFactory {
    private final Scanner scanner;
    private final PetJdbcRepository petDAO;
    private final ClienteJdbcRepository clienteDAO;
    private final VacinaJdbcRepository vacinaDAO;

    public FichaPetFactory(Scanner scanner, PetJdbcRepository petDAO, ClienteJdbcRepository clienteDAO, VacinaJdbcRepository vacinaDAO) {
        this.scanner = scanner;
        this.petDAO = petDAO;
        this.clienteDAO = clienteDAO;
        this.vacinaDAO = vacinaDAO;
    }

    @Override
    public Comando criar() {
        return new FichaPetComando(scanner, petDAO, clienteDAO, vacinaDAO);
    }
}