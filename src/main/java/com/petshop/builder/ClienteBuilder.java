package com.petshop.builder;

import com.petshop.model.Cliente;
import com.petshop.model.Endereco;
import java.time.LocalDate;

public class ClienteBuilder {
    private int id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String telefoneSecundario;
    private String email;
    private Endereco endereco;
    private String observacoes;
    private LocalDate dataCadastro;

    public ClienteBuilder id(int id) {
        this.id = id;
        return this;
    }

    public ClienteBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public ClienteBuilder cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public ClienteBuilder dataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        return this;
    }

    public ClienteBuilder telefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public ClienteBuilder telefoneSecundario(String telefoneSecundario) {
        this.telefoneSecundario = telefoneSecundario;
        return this;
    }

    public ClienteBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ClienteBuilder endereco(Endereco endereco) {
        this.endereco = endereco;
        return this;
    }

    public ClienteBuilder observacoes(String observacoes) {
        this.observacoes = observacoes;
        return this;
    }

    public ClienteBuilder dataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
        return this;
    }

    public Cliente build() {
        LocalDate cadastro = dataCadastro == null ? LocalDate.now() : dataCadastro;
        return new Cliente(id, nome, cpf, dataNascimento, telefone, telefoneSecundario,
                email, endereco, observacoes, cadastro);
    }
}