package com.petshop.servico;

import java.util.function.UnaryOperator;

public enum AdicionalDisponivel {
    HIDRATACAO("Hidratação", Hidratacao::new),
    PERFUME("Perfume", Perfume::new),
    LEVA_E_TRAZ("Leva e traz", LevaETraz::new);

    private final String descricao;
    private final UnaryOperator<ServicoPetshop> embrulho;

    AdicionalDisponivel(String descricao, UnaryOperator<ServicoPetshop> embrulho) {
        this.descricao = descricao;
        this.embrulho = embrulho;
    }

    public ServicoPetshop aplicarEm(ServicoPetshop servico) {
        return embrulho.apply(servico);
    }

    @Override
    public String toString() {
        return descricao;
    }
}