package com.petshop.agenda;

import com.petshop.model.Pet;
import com.petshop.repository.PetRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PetRepositoryFalso implements PetRepository {
    private final List<Pet> pets = new ArrayList<>();

    @Override
    public Pet inserir(Pet pet) {
        Pet salvo = pet.comId(pets.size() + 1);
        pets.add(salvo);
        return salvo;
    }

    @Override
    public List<Pet> listar() {
        return new ArrayList<>(pets);
    }

    @Override
    public List<Pet> listarVacinaVencida() {
        return pets.stream().filter(Pet::isVacinaVencida).toList();
    }

    @Override
    public Optional<Pet> buscarPorId(int id) {
        return pets.stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public void atualizar(Pet pet) {
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getId() == pet.getId()) {
                pets.set(i, pet);
            }
        }
    }

    @Override
    public void excluir(int id) {
        pets.removeIf(p -> p.getId() == id);
    }

    @Override
    public boolean existePetDoCliente(int clienteId) {
        return pets.stream().anyMatch(p -> p.getClienteId() == clienteId);
    }
}