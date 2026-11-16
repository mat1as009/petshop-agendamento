package com.petshop.api;

import com.petshop.model.Cliente;
import com.petshop.model.Endereco;
import java.time.LocalDate;

public record ClienteResponse(int id, String nome, String cpf, LocalDate dataNascimento,
                              String telefone, String telefoneSecundario, String email,
                              EnderecoResponse endereco, String observacoes,
                              LocalDate dataCadastro) {

    public record EnderecoResponse(String cep, String logradouro, String numero,
                                   String complemento, String bairro, String cidade,
                                   String estado) {
    }

    public static ClienteResponse de(Cliente cliente) {
        Endereco e = cliente.getEndereco();
        EnderecoResponse endereco = new EnderecoResponse(e.getCepFormatado(), e.getLogradouro(),
                e.getNumero(), e.getComplemento(), e.getBairro(), e.getCidade(), e.getEstado());
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getCpfFormatado(),
                cliente.getDataNascimento(), cliente.getTelefone(), cliente.getTelefoneSecundario(),
                cliente.getEmail(), endereco, cliente.getObservacoes(), cliente.getDataCadastro());
    }
}