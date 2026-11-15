package com.petshop.command;

import com.petshop.dao.PetDAO;
import com.petshop.model.Pet;
import java.util.List;

public class ListarPetsComando implements Comando {
    private final PetDAO petDAO;

    public ListarPetsComando(PetDAO petDAO) {
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