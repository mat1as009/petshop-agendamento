package com.petshop.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Test;

public class AgendamentoTest {
    private static final LocalDate DATA = LocalDate.now().plusDays(1);
    private static final LocalTime HORA = LocalTime.of(9, 0);

    @Test
    public void agendamentoSimplesUsaDescricaoEPrecoDoServico() {
        Agendamento agendamento = new Agendamento(1, Servico.BANHO, DATA, HORA);

        assertEquals("Banho", agendamento.getDescricao());
        assertEquals(new BigDecimal("40.00"), agendamento.getValor());
        assertEquals(StatusAgendamento.AGENDADO, agendamento.getStatus());
    }

    @Test
    public void agendamentoGuardaDescricaoEValorComAdicionais() {
        Agendamento agendamento = new Agendamento(1, Servico.BANHO, "Banho + Perfume",
                new BigDecimal("50.00"), DATA, HORA, "");

        assertEquals("Banho + Perfume", agendamento.getDescricao());
        assertEquals(new BigDecimal("50.00"), agendamento.getValor());
    }

    @Test
    public void agendamentoSemPetEhRecusado() {
        assertThrows(IllegalArgumentException.class,
                () -> new Agendamento(0, Servico.BANHO, DATA, HORA));
    }

    @Test
    public void comIdMantemOsDemaisDados() {
        Agendamento original = new Agendamento(1, Servico.TOSA, DATA, HORA);

        Agendamento comId = original.comId(7);

        assertEquals(7, comId.getId());
        assertEquals(original.getDescricao(), comId.getDescricao());
    }
}