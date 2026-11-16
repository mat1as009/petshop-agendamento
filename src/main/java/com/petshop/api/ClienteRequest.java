package com.petshop.api;

import com.petshop.builder.ClienteBuilder;
import com.petshop.model.Cliente;
import com.petshop.model.Endereco;
import java.time.LocalDate;

public record ClienteRequest(String nome, String cpf, LocalDate dataNascimento,
                             String telefone, String telefoneSecundario, String email,
                             EnderecoRequest endereco, String observacoes) {

    public record EnderecoRequest(String cep, String logradouro, String numero,
                                  String complemento, String bairro, String cidade,
                                  String estado) {
    }

    // As validações ficam nas classes do modelo: se algo estiver errado, elas lançam o erro
    public Cliente paraCliente(int id, LocalDate dataCadastro) {
        if (endereco == null) {
            throw new IllegalArgumentException("O endereço é obrigatório.");
        }
        Endereco novoEndereco = new Endereco(endereco.cep(), endereco.logradouro(),
                endereco.numero(), endereco.complemento(), endereco.bairro(),
                endereco.cidade(), endereco.estado());
        return new ClienteBuilder()
                .id(id)
                .nome(nome)
                .cpf(cpf)
                .dataNascimento(dataNascimento)
                .telefone(telefone)
                .telefoneSecundario(telefoneSecundario)
                .email(email)
                .endereco(novoEndereco)
                .observacoes(observacoes)
                .dataCadastro(dataCadastro)
                .build();
    }
}