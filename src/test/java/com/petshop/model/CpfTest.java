package com.petshop.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class CpfTest {

    @Test
    public void guardaSoOsNumeros() {
        assertEquals("12345678900", new Cpf("123.456.789-00").numeros());
    }

    @Test
    public void formataParaExibir() {
        assertEquals("123.456.789-00", new Cpf("12345678900").formatado());
    }

    @Test
    public void cpfComPoucosNumerosEhRecusado() {
        assertThrows(IllegalArgumentException.class, () -> new Cpf("123"));
    }

    @Test
    public void cpfNuloEhRecusado() {
        assertThrows(IllegalArgumentException.class, () -> new Cpf(null));
    }

    @Test
    public void doisCpfsComOsMesmosNumerosSaoIguais() {
        assertEquals(new Cpf("123.456.789-00"), new Cpf("12345678900"));
    }
}