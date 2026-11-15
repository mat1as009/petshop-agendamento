package com.petshop.model;

import java.time.LocalDate;

public class Cliente {
    private final int id;
    private final String nome;
    private final String cpf;          // guardado só com números
    private final LocalDate dataNascimento;
    private final String telefone;
    private final String telefoneSecundario;
    private final String email;
    private final Endereco endereco;   // relacionamento 1:1
    private final String observacoes;
    private final LocalDate dataCadastro;

    // Para criar um Cliente, use o ClienteBuilder
    public Cliente(int id, String nome, String cpf, LocalDate dataNascimento, String telefone,
                   String telefoneSecundario, String email, Endereco endereco,
                   String observacoes, LocalDate dataCadastro) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        String cpfNumeros = cpf == null ? "" : cpf.replaceAll("\\D", "");
        if (cpfNumeros.length() != 11) {
            throw new IllegalArgumentException("O CPF deve ter 11 números.");
        }
        if (email != null && !email.isBlank()
                && !email.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException(
                    "Email inválido. Use o formato nome@dominio.com ou deixe em branco.");
        }
        if (endereco == null) {
            throw new IllegalArgumentException("O cliente precisa de um endereço.");
        }
        if (dataNascimento != null && dataNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento não pode ser no futuro.");
        }
        if (dataCadastro == null) {
            throw new IllegalArgumentException("A data de cadastro é obrigatória.");
        }
        this.id = id;
        this.nome = nome.trim();
        this.cpf = cpfNumeros;
        this.dataNascimento = dataNascimento;
        this.telefone = limpar(telefone);
        this.telefoneSecundario = limpar(telefoneSecundario);
        this.email = limpar(email);
        this.endereco = endereco;
        this.observacoes = limpar(observacoes);
        this.dataCadastro = dataCadastro;
    }

    private static String limpar(String texto) {
        return texto == null ? "" : texto.trim();
    }

    // Como os campos não mudam, "alterar" é criar uma cópia com o valor novo
    public Cliente comId(int novoId) {
        return new Cliente(novoId, nome, cpf, dataNascimento, telefone, telefoneSecundario,
                email, endereco, observacoes, dataCadastro);
    }

    public String getCpfFormatado() {
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getTelefone() { return telefone; }
    public String getTelefoneSecundario() { return telefoneSecundario; }
    public String getEmail() { return email; }
    public Endereco getEndereco() { return endereco; }
    public String getObservacoes() { return observacoes; }
    public LocalDate getDataCadastro() { return dataCadastro; }

    @Override
    public String toString() {
        return id + " - " + nome + " | CPF " + getCpfFormatado() + " | " + telefone;
    }
}