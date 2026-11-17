package com.petshop.command;

import com.petshop.dao.ClienteJdbcRepository;
import com.petshop.model.Cliente;
import java.util.List;

public class ListarClientesComando implements Comando {
    private final ClienteJdbcRepository clienteDAO;

    public ListarClientesComando(ClienteJdbcRepository clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public void executar() {
        List<Cliente> clientes = clienteDAO.listar();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            clientes.forEach(System.out::println);
        }
    }
}