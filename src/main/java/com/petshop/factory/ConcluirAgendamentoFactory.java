package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.ConcluirAgendamentoComando;
import com.petshop.dao.AgendamentoDAO;
import com.petshop.dao.PetDAO;
import com.petshop.dao.VacinaDAO;
import java.util.Scanner;

public class ConcluirAgendamentoFactory implements ComandoFactory {
    private final Scanner scanner;
    private final AgendamentoDAO agendamentoDAO;
    private final PetDAO petDAO;
    private final VacinaDAO vacinaDAO;

    public ConcluirAgendamentoFactory(Scanner scanner, AgendamentoDAO agendamentoDAO,
                                      PetDAO petDAO, VacinaDAO vacinaDAO) {
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