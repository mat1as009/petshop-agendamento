package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.ListarAgendamentosComando;
import com.petshop.dao.AgendamentoDAO;

public class ListarAgendamentosFactory implements ComandoFactory {
    private final AgendamentoDAO agendamentoDAO;

    public ListarAgendamentosFactory(AgendamentoDAO agendamentoDAO) {
        this.agendamentoDAO = agendamentoDAO;
    }

    @Override
    public Comando criar() {
        return new ListarAgendamentosComando(agendamentoDAO);
    }
}