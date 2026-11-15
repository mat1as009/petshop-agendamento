package com.petshop.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Vacina {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final int id;
    private final int petId;
    private final LocalDate dataAplicacao;

    // Vacina nova: o id ainda não existe, o banco é quem gera
    public Vacina(int petId, LocalDate dataAplicacao) {
        this(0, petId, dataAplicacao);
    }

    // Vacina que veio do banco
    public Vacina(int id, int petId, LocalDate dataAplicacao) {
        if (petId <= 0 || dataAplicacao == null) {
            throw new IllegalArgumentException("Dados da vacina incompletos.");
        }
        this.id = id;
        this.petId = petId;
        this.dataAplicacao = dataAplicacao;
    }

    public int getId() { return id; }
    public int getPetId() { return petId; }
    public LocalDate getDataAplicacao() { return dataAplicacao; }

    @Override
    public String toString() {
        return dataAplicacao.format(FORMATO_DATA);
    }
}