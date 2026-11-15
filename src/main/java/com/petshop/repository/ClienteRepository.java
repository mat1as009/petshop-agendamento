package com.petshop.repository;

import com.petshop.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente inserir(Cliente cliente);

    List<Cliente> listar();

    Optional<Cliente> buscarPorId(int id);

    void atualizar(Cliente cliente);

    void excluir(int id);
}