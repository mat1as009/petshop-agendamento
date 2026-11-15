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

    @Test
    public void levaETrazSomaOPreco() {
        ServicoPetshop tosa = new LevaETraz(new ServicoBase(Servico.TOSA));

        assertEquals(new BigDecimal("80.00"), tosa.preco());
        assertEquals("Tosa + Leva e traz", tosa.descricao());
    }

    @Test
    public void listaDeAdicionaisAplicaOEscolhido() {
        ServicoPetshop base = new ServicoBase(Servico.BANHO);

        ServicoPetshop comPerfume = AdicionalDisponivel.PERFUME.aplicarEm(base);

        assertEquals(new BigDecimal("50.00"), comPerfume.preco());
        assertEquals("Banho + Perfume", comPerfume.descricao());
    }
}