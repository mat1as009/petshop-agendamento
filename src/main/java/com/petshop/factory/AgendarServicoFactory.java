package com.petshop.factory;

import com.petshop.command.AgendarServicoComando;
import com.petshop.command.Comando;
import com.petshop.dao.AgendamentoDAO;
import com.petshop.dao.PetDAO;
import java.util.Scanner;

public class AgendarServicoFactory implements ComandoFactory {
    private final Scanner scanner;
    private final PetDAO petDAO;
    private final AgendamentoDAO agendamentoDAO;

    public AgendarServicoFactory(Scanner scanner, PetDAO petDAO, AgendamentoDAO agendamentoDAO) {
        this.scanner = scanner;
        this.petDAO = petDAO;
        this.agendamentoDAO = agendamentoDAO;
    }

    @Override
    public Comando criar() {
        return new AgendarServicoComando(scanner, petDAO, agendamentoDAO);
    }
}