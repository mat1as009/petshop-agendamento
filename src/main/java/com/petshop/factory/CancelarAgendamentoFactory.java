package com.petshop.factory;

import com.petshop.command.AlterarStatusAgendamentoComando;
import com.petshop.command.Comando;
import com.petshop.dao.AgendamentoJdbcRepository;
import com.petshop.model.StatusAgendamento;
import java.util.Scanner;

public class CancelarAgendamentoFactory implements ComandoFactory {
    private final Scanner scanner;
    private final AgendamentoJdbcRepository agendamentoDAO;

    public CancelarAgendamentoFactory(Scanner scanner, AgendamentoJdbcRepository agendamentoDAO) {
        this.scanner = scanner;
        this.agendamentoDAO = agendamentoDAO;
    }

    @Override
    public Comando criar() {
        return new AlterarStatusAgendamentoComando(scanner, agendamentoDAO, StatusAgendamento.CANCELADO);
    }
}