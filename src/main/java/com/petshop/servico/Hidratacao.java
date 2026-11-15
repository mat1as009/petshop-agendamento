package com.petshop.servico;

import java.math.BigDecimal;

public class Hidratacao extends AdicionalServico {

    public Hidratacao(ServicoPetshop servico) {
        super(servico);
    }

    @Override
    protected String nomeAdicional() {
        return "Hidratação";
    }

    @Override
    protected BigDecimal precoAdicional() {
        return new BigDecimal("25.00");
    }
}