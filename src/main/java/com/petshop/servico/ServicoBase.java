package com.petshop.servico;

import com.petshop.model.Servico;
import java.math.BigDecimal;

public class ServicoBase implements ServicoPetshop {
    private final Servico servico;

    public ServicoBase(Servico servico) {
        if (servico == null) {
            throw new IllegalArgumentException("O serviço é obrigatório.");
        }
        this.servico = servico;
    }

    @Override
    public String descricao() {
        return servico.getDescricao();
    }

    @Override
    public BigDecimal preco() {
        return servico.getPreco();
    }
}