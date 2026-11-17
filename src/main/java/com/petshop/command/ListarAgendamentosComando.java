package com.petshop.command;

import com.petshop.dao.AgendamentoJdbcRepository;
import com.petshop.model.Agendamento;
import java.util.List;

public class ListarAgendamentosComando implements Comando {
    private final AgendamentoJdbcRepository agendamentoDAO;

    public ListarAgendamentosComando(AgendamentoJdbcRepository agendamentoDAO) {
        this.agendamentoDAO = agendamentoDAO;
    }

    @Override
    public void executar() {
        List<Agendamento> agendamentos = agendamentoDAO.listar();
        if (agendamentos.isEmpty()) {
            System.out.println("Nenhum agendamento.");
        } else {
            agendamentos.forEach(System.out::println);
        }
    }
}