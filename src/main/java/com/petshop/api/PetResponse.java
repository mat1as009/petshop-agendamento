package com.petshop.api;

import com.petshop.model.Pet;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PetResponse(int id, String nome, String especie, String raca, BigDecimal peso,
                          String sexo, LocalDate dataNascimento, String cor, boolean castrado,
                          String observacoes, LocalDate proximaVacina, boolean vacinaVencida,
                          int clienteId) {

    public static PetResponse de(Pet pet) {
        return new PetResponse(pet.getId(), pet.getNome(), pet.getEspecie(), pet.getRaca(),
                pet.getPeso(), pet.getSexo().name(), pet.getDataNascimento(), pet.getCor(),
                pet.isCastrado(), pet.getObservacoes(), pet.getProximaVacina(),
                pet.isVacinaVencida(), pet.getClienteId());
    }
}