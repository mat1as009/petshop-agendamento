package com.petshop.api;

import com.petshop.builder.PetBuilder;
import com.petshop.model.Pet;
import com.petshop.model.Sexo;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PetRequest(String nome, String especie, String raca, BigDecimal peso, String sexo,
                         LocalDate dataNascimento, String cor, boolean castrado,
                         String observacoes, LocalDate proximaVacina, int clienteId) {

    // As validações ficam nas classes do modelo: se algo estiver errado, elas lançam o erro
    public Pet paraPet(int id, int dono) {
        return new PetBuilder()
                .id(id)
                .nome(nome)
                .especie(especie)
                .raca(raca)
                .peso(peso)
                .sexo(converterSexo())
                .dataNascimento(dataNascimento)
                .cor(cor)
                .castrado(castrado)
                .observacoes(observacoes)
                .proximaVacina(proximaVacina)
                .clienteId(dono)
                .build();
    }

    private Sexo converterSexo() {
        if (sexo == null || sexo.isBlank()) {
            return null;
        }
        try {
            return Sexo.valueOf(sexo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Sexo inválido. Use MACHO ou FEMEA.");
        }
    }
}