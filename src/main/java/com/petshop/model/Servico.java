package com.petshop.model;

import java.math.BigDecimal;

public enum Servico {
    BANHO("Banho", "40.00"),
    TOSA("Tosa", "60.00"),
    VACINA("Vacina", "80.00"),
    CONSULTA("Consulta", "120.00");

    private final String descricao;
    private final BigDecimal preco;

    Servico(String descricao, String preco) {
        this.descricao = descricao;
        this.preco = new BigDecimal(preco);
    }

    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }

    @Override
    public String toString() {
        return descricao + " (R$ " + preco + ")";
    }
}