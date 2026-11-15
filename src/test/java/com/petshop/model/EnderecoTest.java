package com.petshop.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class EnderecoTest {

    private Endereco endereco(String cep, String logradouro, String estado) {
        return new Endereco(cep, logradouro, "1000", "", "Bela Vista", "São Paulo", estado);
    }

    @Test
    public void cepEhFormatadoParaExibir() {
        Endereco endereco = endereco("01310100", "Avenida Paulista", "SP");

        assertEquals("01310-100", endereco.getCepFormatado());
    }

    @Test
    public void cepComPoucosNumerosEhRecusado() {
        assertThrows(IllegalArgumentException.class,
                () -> endereco("123", "Avenida Paulista", "SP"));
    }

    @Test
    public void estadoEhGuardadoEmMaiusculo() {
        Endereco endereco = endereco("01310100", "Avenida Paulista", "sp");

        assertEquals("SP", endereco.getEstado());
    }

    @Test
    public void estadoComTresLetrasEhRecusado() {
        assertThrows(IllegalArgumentException.class,
                () -> endereco("01310100", "Avenida Paulista", "SPA"));
    }

    @Test
    public void logradouroEmBrancoEhRecusado() {
        assertThrows(IllegalArgumentException.class,
                () -> endereco("01310100", " ", "SP"));
    }
}