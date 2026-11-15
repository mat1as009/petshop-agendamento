package com.petshop.model;

public class Endereco {
    private final String cep;          // guardado só com números
    private final String logradouro;
    private final String numero;
    private final String complemento;
    private final String bairro;
    private final String cidade;
    private final String estado;

    public Endereco(String cep, String logradouro, String numero, String complemento,
                    String bairro, String cidade, String estado) {
        String cepNumeros = cep == null ? "" : cep.replaceAll("\\D", "");
        if (cepNumeros.length() != 8) {
            throw new IllegalArgumentException("O CEP deve ter 8 números.");
        }
        if (estaVazio(logradouro) || estaVazio(numero) || estaVazio(cidade)) {
            throw new IllegalArgumentException("Logradouro, número e cidade são obrigatórios.");
        }
        if (estado == null || !estado.trim().matches("[A-Za-z]{2}")) {
            throw new IllegalArgumentException("O estado deve ter 2 letras (exemplo: SP).");
        }
        this.cep = cepNumeros;
        this.logradouro = logradouro.trim();
        this.numero = numero.trim();
        this.complemento = limpar(complemento);
        this.bairro = limpar(bairro);
        this.cidade = cidade.trim();
        this.estado = estado.trim().toUpperCase();
    }

    private static boolean estaVazio(String texto) {
        return texto == null || texto.isBlank();
    }

    private static String limpar(String texto) {
        return texto == null ? "" : texto.trim();
    }

    public String getCepFormatado() {
        return cep.substring(0, 5) + "-" + cep.substring(5);
    }

    public String getCep() { return cep; }
    public String getLogradouro() { return logradouro; }
    public String getNumero() { return numero; }
    public String getComplemento() { return complemento; }
    public String getBairro() { return bairro; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        return logradouro + ", " + numero
                + (complemento.isEmpty() ? "" : " (" + complemento + ")")
                + (bairro.isEmpty() ? "" : " - " + bairro)
                + " - " + cidade + "/" + estado + " - CEP " + getCepFormatado();
    }
}