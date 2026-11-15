package com.petshop.factory;

import com.petshop.command.Comando;
import com.petshop.command.VacinaVencidaComando;
import com.petshop.dao.PetDAO;

public class VacinaVencidaFactory implements ComandoFactory {
    private final PetDAO petDAO;

    public VacinaVencidaFactory(PetDAO petDAO) {
        this.petDAO = petDAO;
    }

    @Override
    public Comando criar() {
        return new VacinaVencidaComando(petDAO);
    }
}