package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.ListarPetsComando;
import com.petshop.dao.PetDAO;

public class ListarPetsFactory implements ComandoFactory {
    private final PetDAO petDAO;

    public ListarPetsFactory(PetDAO petDAO) {
        this.petDAO = petDAO;
    }

    @Override
    public Comando criar() {
        return new ListarPetsComando(petDAO);
    }
}