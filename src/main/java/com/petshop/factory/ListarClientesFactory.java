package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.ListarClientesComando;
import com.petshop.dao.ClienteDAO;

public class ListarClientesFactory implements ComandoFactory {
    private final ClienteDAO clienteDAO;

    public ListarClientesFactory(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Comando criar() {
        return new ListarClientesComando(clienteDAO);
    }
}