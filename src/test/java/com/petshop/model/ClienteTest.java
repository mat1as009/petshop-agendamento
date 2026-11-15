package com.petshop.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.petshop.builder.ClienteBuilder;
import java.time.LocalDate;
import org.junit.Test;

public class ClienteTest {

    private Endereco endereco() {
        return new Endereco("01310-100", "Avenida Paulista", "1000", "",
                "Bela Vista", "São Paulo", "SP");
    }

    private ClienteBuilder clienteValido() {
        return new ClienteBuilder()
                .nome("Maria Silva")
                .cpf("123.456.789-00")
                .telefone("11999999999")
                .endereco(endereco());
    }

    @Test
    public void cpfEhGuardadoSoComNumerosEFormatadoParaExibir() {
        Cliente cliente = clienteValido().build();

        assertEquals("12345678900", cliente.getCpf());
        assertEquals("123.456.789-00", cliente.getCpfFormatado());
    }

    @Test
    public void cpfComMenosDeOnzeNumerosEhRecusado() {
        assertThrows(IllegalArgumentException.class, () -> clienteValido().cpf("123").build());
    }

    @Test
    public void nomeEmBrancoEhRecusado() {
        assertThrows(IllegalArgumentException.class, () -> clienteValido().nome("  ").build());
    }

    @Test
    public void emailSemFormatoEhRecusado() {
        assertThrows(IllegalArgumentException.class, () -> clienteValido().email("abc@").build());
    }

    @Test
    public void emailEmBrancoEhAceito() {
        Cliente cliente = clienteValido().email("").build();

        assertEquals("", cliente.getEmail());
    }

    @Test
    public void clienteSemEnderecoEhRecusado() {
        assertThrows(IllegalArgumentException.class,
                () -> new ClienteBuilder().nome("Maria").cpf("12345678900").build());
    }

    @Test
    public void nascimentoNoFuturoEhRecusado() {
        LocalDate amanha = LocalDate.now().plusDays(1);

        assertThrows(IllegalArgumentException.class,
                () -> clienteValido().dataNascimento(amanha).build());
    }
}