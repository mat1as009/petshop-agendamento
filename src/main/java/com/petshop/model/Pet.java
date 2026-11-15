package com.petshop.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Pet {
    private final int id;
    private final String nome;
    private final String especie;
    private final String raca;
    private final BigDecimal peso;
    private final Sexo sexo;
    private final LocalDate dataNascimento;
    private final String cor;
    private final boolean castrado;
    private final String observacoes;
    private final LocalDate proximaVacina;
    private final int clienteId;   // relacionamento 1:n (um cliente tem vários pets)

    // Para criar um Pet, use o PetBuilder
    public Pet(int id, String nome, String especie, String raca, BigDecimal peso, Sexo sexo,
               LocalDate dataNascimento, String cor, boolean castrado, String observacoes,
               LocalDate proximaVacina, int clienteId) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do pet é obrigatório.");
        }
        if (sexo == null) {
            throw new IllegalArgumentException("O sexo do pet é obrigatório.");
        }
        if (peso != null && peso.signum() <= 0) {
            throw new IllegalArgumentException("O peso deve ser maior que zero.");
        }
        if (dataNascimento != null && dataNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento não pode ser no futuro.");
        }
        this.id = id;
        this.nome = nome.trim();
        this.especie = limpar(especie);
        this.raca = limpar(raca);
        this.peso = peso;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.cor = limpar(cor);
        this.castrado = castrado;
        this.observacoes = limpar(observacoes);
        this.proximaVacina = proximaVacina;
        this.clienteId = clienteId;
    }

    private static String limpar(String texto) {
        return texto == null ? "" : texto.trim();
    }

    // Como os campos não mudam, "alterar" é criar uma cópia com o valor novo
    public Pet comId(int novoId) {
        return new Pet(novoId, nome, especie, raca, peso, sexo, dataNascimento, cor,
                castrado, observacoes, proximaVacina, clienteId);
    }

    public Pet comProximaVacina(LocalDate novaData) {
        return new Pet(id, nome, especie, raca, peso, sexo, dataNascimento, cor,
                castrado, observacoes, novaData, clienteId);
    }

    // A regra fica dentro da própria classe
    public boolean isVacinaVencida() {
        return proximaVacina != null && proximaVacina.isBefore(LocalDate.now());
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEspecie() { return especie; }
    public String getRaca() { return raca; }
    public BigDecimal getPeso() { return peso; }
    public Sexo getSexo() { return sexo; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getCor() { return cor; }
    public boolean isCastrado() { return castrado; }
    public String getObservacoes() { return observacoes; }
    public LocalDate getProximaVacina() { return proximaVacina; }
    public int getClienteId() { return clienteId; }

    @Override
    public String toString() {
        return id + " - " + nome + " (" + especie + ", " + raca + ", " + sexo
                + (peso == null ? "" : ", " + peso + " kg")
                + (castrado ? ", castrado" : "")
                + "), vacina: " + proximaVacina;
    }
}