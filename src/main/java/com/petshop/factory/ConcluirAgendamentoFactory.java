package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.ConcluirAgendamentoComando;
import com.petshop.dao.AgendamentoJdbcRepository;
import com.petshop.dao.PetJdbcRepository;
import com.petshop.dao.VacinaJdbcRepository;
import java.util.Scanner;

public class ConcluirAgendamentoFactory implements ComandoFactory {
    private final Scanner scanner;
    private final AgendamentoJdbcRepository agendamentoDAO;
    private final PetJdbcRepository petDAO;
    private final VacinaJdbcRepository vacinaDAO;

    public ConcluirAgendamentoFactory(Scanner scanner, AgendamentoJdbcRepository agendamentoDAO,
                                      PetJdbcRepository petDAO, VacinaJdbcRepository vacinaDAO) {
        this.scanner = scanner;
        this.agendamentoDAO = agendamentoDAO;
        this.petDAO = petDAO;
        this.vacinaDAO = vacinaDAO;
    }

    @Override
    public Comando criar() {
        return new ConcluirAgendamentoComando(scanner, agendamentoDAO, petDAO, vacinaDAO);
    }
}