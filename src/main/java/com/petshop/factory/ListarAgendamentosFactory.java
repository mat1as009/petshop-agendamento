package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.ListarAgendamentosComando;
import com.petshop.dao.AgendamentoJdbcRepository;

public class ListarAgendamentosFactory implements ComandoFactory {
    private final AgendamentoJdbcRepository agendamentoDAO;

    public ListarAgendamentosFactory(AgendamentoJdbcRepository agendamentoDAO) {
        this.agendamentoDAO = agendamentoDAO;
    }

    @Override
    public Comando criar() {
        return new ListarAgendamentosComando(agendamentoDAO);
    }
}