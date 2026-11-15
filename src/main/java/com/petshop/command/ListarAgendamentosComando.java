package com.petshop.command;

import com.petshop.dao.AgendamentoDAO;
import com.petshop.model.Agendamento;
import java.util.List;

public class ListarAgendamentosComando implements Comando {
    private final AgendamentoDAO agendamentoDAO;

    public ListarAgendamentosComando(AgendamentoDAO agendamentoDAO) {
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