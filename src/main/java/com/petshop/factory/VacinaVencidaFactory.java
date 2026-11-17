package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.VacinaVencidaComando;
import com.petshop.dao.PetJdbcRepository;

public class VacinaVencidaFactory implements ComandoFactory {
    private final PetJdbcRepository petDAO;

    public VacinaVencidaFactory(PetJdbcRepository petDAO) {
        this.petDAO = petDAO;
    }

    @Override
    public Comando criar() {
        return new VacinaVencidaComando(petDAO);
    }
}