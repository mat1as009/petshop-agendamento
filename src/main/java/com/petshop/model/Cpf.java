package com.petshop.model;

public final class Cpf {
    private final String numeros;

    public Cpf(String texto) {
        String soNumeros = texto == null ? "" : texto.replaceAll("\\D", "");
        if (soNumeros.length() != 11) {
            throw new IllegalArgumentException("O CPF deve ter 11 números.");
        }
        this.numeros = soNumeros;
    }

    public String numeros() {
        return numeros;
    }

    public String formatado() {
        return numeros.substring(0, 3) + "." + numeros.substring(3, 6) + "."
                + numeros.substring(6, 9) + "-" + numeros.substring(9);
    }

    @Override
    public boolean equals(Object outro) {
        return outro instanceof Cpf cpf && numeros.equals(cpf.numeros);
    }

    @Override
    public int hashCode() {
        return numeros.hashCode();
    }

    @Override
    public String toString() {
        return formatado();
    }
}