package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.FichaPetComando;
import com.petshop.dao.ClienteDAO;
import com.petshop.dao.PetDAO;
import com.petshop.dao.VacinaDAO;
import java.util.Scanner;

public class FichaPetFactory implements ComandoFactory {
    private final Scanner scanner;
    private final PetDAO petDAO;
    private final ClienteDAO clienteDAO;
    private final VacinaDAO vacinaDAO;

    public FichaPetFactory(Scanner scanner, PetDAO petDAO, ClienteDAO clienteDAO, VacinaDAO vacinaDAO) {
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