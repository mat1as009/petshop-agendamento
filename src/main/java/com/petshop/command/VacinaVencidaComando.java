package com.petshop.command;

import com.petshop.dao.PetJdbcRepository;
import com.petshop.model.Pet;
import java.util.List;

public class VacinaVencidaComando implements Comando {
    private final PetJdbcRepository petDAO;

    public VacinaVencidaComando(PetJdbcRepository petDAO) {
        this.petDAO = petDAO;
    }

    @Override
    public void executar() {
        List<Pet> pets = petDAO.listarVacinaVencida();
        if (pets.isEmpty()) {
            System.out.println("Nenhum pet com vacina vencida.");
        } else {
            System.out.println("ATENÇÃO: pets com vacina vencida:");
            pets.forEach(System.out::println);
        }
    }
}