package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.ListarPetsComando;
import com.petshop.dao.PetJdbcRepository;

public class ListarPetsFactory implements ComandoFactory {
    private final PetJdbcRepository petDAO;

    public ListarPetsFactory(PetJdbcRepository petDAO) {
        this.petDAO = petDAO;
    }

    @Override
    public Comando criar() {
        return new ListarPetsComando(petDAO);
    }
}