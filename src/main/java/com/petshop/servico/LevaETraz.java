package com.petshop.servico;

import java.math.BigDecimal;

public class LevaETraz extends AdicionalServico {

    public LevaETraz(ServicoPetshop servico) {
        super(servico);
    }

    @Override
    protected String nomeAdicional() {
        return "Leva e traz";
    }

    @Override
    protected BigDecimal precoAdicional() {
        return new BigDecimal("20.00");
    }
}