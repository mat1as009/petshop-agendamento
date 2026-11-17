package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.ListarClientesComando;
import com.petshop.dao.ClienteJdbcRepository;

public class ListarClientesFactory implements ComandoFactory {
    private final ClienteJdbcRepository clienteDAO;

    public ListarClientesFactory(ClienteJdbcRepository clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Comando criar() {
        return new ListarClientesComando(clienteDAO);
    }
}