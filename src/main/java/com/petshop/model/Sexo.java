package com.petshop.model;

public enum Sexo {
    MACHO("Macho"),
    FEMEA("Fêmea");

    private final String descricao;

    Sexo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }

    @Override
    public String toString() {
        return descricao;
    }
}