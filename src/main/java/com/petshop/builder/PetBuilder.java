package com.petshop.builder;

import com.petshop.model.Pet;
import com.petshop.model.Sexo;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PetBuilder {
    private int id;
    private String nome;
    private String especie;
    private String raca;
    private BigDecimal peso;
    private Sexo sexo;
    private LocalDate dataNascimento;
    private String cor;
    private boolean castrado;
    private String observacoes;
    private LocalDate proximaVacina;
    private int clienteId;

    public PetBuilder id(int id) {
        this.id = id;
        return this;
    }

    public PetBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public PetBuilder especie(String especie) {
        this.especie = especie;
        return this;
    }

    public PetBuilder raca(String raca) {
        this.raca = raca;
        return this;
    }

    public PetBuilder peso(BigDecimal peso) {
        this.peso = peso;
        return this;
    }

    public PetBuilder sexo(Sexo sexo) {
        this.sexo = sexo;
        return this;
    }

    public PetBuilder dataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        return this;
    }

    public PetBuilder cor(String cor) {
        this.cor = cor;
        return this;
    }

    public PetBuilder castrado(boolean castrado) {
        this.castrado = castrado;
        return this;
    }

    public PetBuilder observacoes(String observacoes) {
        this.observacoes = observacoes;
        return this;
    }

    public PetBuilder proximaVacina(LocalDate proximaVacina) {
        this.proximaVacina = proximaVacina;
        return this;
    }

    public PetBuilder clienteId(int clienteId) {
        this.clienteId = clienteId;
        return this;
    }

    public Pet build() {
        if (clienteId <= 0) {
            throw new IllegalStateException("O pet precisa ter um dono (cliente).");
        }
        return new Pet(id, nome, especie, raca, peso, sexo, dataNascimento, cor,
                castrado, observacoes, proximaVacina, clienteId);
    }
}