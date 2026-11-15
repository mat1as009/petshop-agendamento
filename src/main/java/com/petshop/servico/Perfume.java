package com.petshop.servico;

import java.math.BigDecimal;

public class Perfume extends AdicionalServico {

    public Perfume(ServicoPetshop servico) {
        super(servico);
    }

    @Override
    protected String nomeAdicional() {
        return "Perfume";
    }

    @Override
    protected BigDecimal precoAdicional() {
        return new BigDecimal("10.00");
    }
}