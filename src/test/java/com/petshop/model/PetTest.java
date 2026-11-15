package com.petshop.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.petshop.builder.PetBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.Test;

public class PetTest {

    private PetBuilder petValido() {
        return new PetBuilder()
                .nome("Rex")
                .especie("Cachorro")
                .sexo(Sexo.MACHO)
                .clienteId(1);
    }

    @Test
    public void nomeEmBrancoEhRecusado() {
        assertThrows(IllegalArgumentException.class, () -> petValido().nome(" ").build());
    }

    @Test
    public void petSemSexoEhRecusado() {
        assertThrows(IllegalArgumentException.class, () -> petValido().sexo(null).build());
    }

    @Test
    public void pesoZeroEhRecusado() {
        assertThrows(IllegalArgumentException.class,
                () -> petValido().peso(BigDecimal.ZERO).build());
    }

    @Test
    public void vacinaComDataNoPassadoEstaVencida() {
        Pet pet = petValido().proximaVacina(LocalDate.now().minusDays(1)).build();

        assertTrue(pet.isVacinaVencida());
    }

    @Test
    public void vacinaComDataNoFuturoNaoEstaVencida() {
        Pet pet = petValido().proximaVacina(LocalDate.now().plusDays(30)).build();

        assertFalse(pet.isVacinaVencida());
    }

    @Test
    public void petSemDataDeVacinaNaoEstaVencido() {
        Pet pet = petValido().build();

        assertFalse(pet.isVacinaVencida());
    }

    @Test
    public void petSemDonoLancaErroNoBuilder() {
        assertThrows(IllegalStateException.class, () -> petValido().clienteId(0).build());
    }

    @Test
    public void comProximaVacinaGeraUmaCopiaComADataNova() {
        Pet original = petValido().build();
        LocalDate data = LocalDate.now().plusYears(1);

        Pet copia = original.comProximaVacina(data);

        assertEquals(data, copia.getProximaVacina());
        assertEquals(original.getNome(), copia.getNome());
    }
}