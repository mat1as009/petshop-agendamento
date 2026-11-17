package com.petshop.factory;

import com.petshop.command.AgendarServicoComando;
import com.petshop.command.Comando;
import com.petshop.dao.AgendamentoJdbcRepository;
import com.petshop.dao.PetJdbcRepository;
import java.util.Scanner;

public class AgendarServicoFactory implements ComandoFactory {
    private final Scanner scanner;
    private final PetJdbcRepository petDAO;
    private final AgendamentoJdbcRepository agendamentoDAO;

    public AgendarServicoFactory(Scanner scanner, PetJdbcRepository petDAO, AgendamentoJdbcRepository agendamentoDAO) {
        this.scanner = scanner;
        this.petDAO = petDAO;
        this.agendamentoDAO = agendamentoDAO;
    }

    @Override
    public Comando criar() {
        return new AgendarServicoComando(scanner, petDAO, agendamentoDAO);
    }
}