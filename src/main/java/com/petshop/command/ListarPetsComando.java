package com.petshop.command;

import com.petshop.dao.PetJdbcRepository;
import com.petshop.model.Pet;
import java.util.List;

public class ListarPetsComando implements Comando {
    private final PetJdbcRepository petDAO;

    public ListarPetsComando(PetJdbcRepository petDAO) {
        this.petDAO = petDAO;
    }

    @Override
    public void executar() {
        List<Pet> pets = petDAO.listar();
        if (pets.isEmpty()) {
            System.out.println("Nenhum pet cadastrado.");
        } else {
            pets.forEach(System.out::println);
        }
    }
}