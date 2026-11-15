package com.petshop.servico;

import java.math.BigDecimal;

public abstract class AdicionalServico implements ServicoPetshop {
    private final ServicoPetshop servico;

    protected AdicionalServico(ServicoPetshop servico) {
        if (servico == null) {
            throw new IllegalArgumentException("O serviço a decorar é obrigatório.");
        }
        this.servico = servico;
    }

    @Override
    public String descricao() {
        return servico.descricao() + " + " + nomeAdicional();
    }

    @Override
    public BigDecimal preco() {
        return servico.preco().add(precoAdicional());
    }

    protected abstract String nomeAdicional();

    protected abstract BigDecimal precoAdicional();
}