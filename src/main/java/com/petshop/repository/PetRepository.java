package com.petshop.repository;

import com.petshop.model.Pet;
import java.util.List;
import java.util.Optional;

public interface PetRepository {
    Pet inserir(Pet pet);

    List<Pet> listar();

    List<Pet> listarVacinaVencida();

    Optional<Pet> buscarPorId(int id);

    void atualizar(Pet pet);

    void excluir(int id);

    boolean existePetDoCliente(int clienteId);
}