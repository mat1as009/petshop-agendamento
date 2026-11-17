package com.petshop.agenda;

import com.petshop.model.Pet;
import com.petshop.model.Vacina;
import com.petshop.repository.PetRepository;
import com.petshop.repository.VacinaRepository;
import java.time.LocalDate;
import java.util.Optional;

public class Vacinacao {
    private final VacinaRepository vacinaRepository;
    private final PetRepository petRepository;

    public Vacinacao(VacinaRepository vacinaRepository, PetRepository petRepository) {
        this.vacinaRepository = vacinaRepository;
        this.petRepository = petRepository;
    }

    // Regra: grava a vacina no histórico e renova a próxima vacina do pet em um ano
    public Optional<Pet> registrar(int petId, LocalDate dataAplicacao) {
        vacinaRepository.inserir(new Vacina(petId, dataAplicacao));

        Optional<Pet> encontrado = petRepository.buscarPorId(petId);
        if (encontrado.isEmpty()) {
            return Optional.empty();
        }
        Pet renovado = encontrado.get().comProximaVacina(dataAplicacao.plusYears(1));
        petRepository.atualizar(renovado);
        return Optional.of(renovado);
    }
}