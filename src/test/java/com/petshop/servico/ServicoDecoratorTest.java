package com.petshop.servico;

import static org.junit.Assert.assertEquals;

import com.petshop.model.Servico;
import java.math.BigDecimal;
import org.junit.Test;

public class ServicoDecoratorTest {

    @Test
    public void servicoBaseTemOPrecoDoServico() {
        ServicoPetshop banho = new ServicoBase(Servico.BANHO);

        assertEquals(new BigDecimal("40.00"), banho.preco());
        assertEquals("Banho", banho.descricao());
    }

    @Test
    public void adicionalSomaPrecoEDescricao() {
        ServicoPetshop banho = new Hidratacao(new ServicoBase(Servico.BANHO));

        assertEquals(new BigDecimal("65.00"), banho.preco());
        assertEquals("Banho + Hidratação", banho.descricao());
    }

    @Test
    public void adicionaisPodemSerEmpilhados() {
        ServicoPetshop completo = new Perfume(new Hidratacao(new ServicoBase(Servico.BANHO)));

        assertEquals(new BigDecimal("75.00"), completo.preco());
        assertEquals("Banho + Hidratação + Perfume", completo.descricao());
    }
}