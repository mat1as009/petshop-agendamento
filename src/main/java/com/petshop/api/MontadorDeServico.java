package com.petshop.api;

import com.petshop.model.Servico;
import com.petshop.servico.AdicionalDisponivel;
import com.petshop.servico.ServicoBase;
import com.petshop.servico.ServicoPetshop;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class MontadorDeServico {

    private MontadorDeServico() {
    }

    public static Servico converterServico(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O serviço é obrigatório.");
        }
        try {
            return Servico.valueOf(nome.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Serviço inválido: " + nome);
        }
    }

    // Cada adicional embrulha o serviço anterior (Decorator)
    public static ServicoPetshop montar(Servico servico, List<String> adicionais) {
        ServicoPetshop atual = new ServicoBase(servico);
        if (adicionais == null) {
            return atual;
        }
        Set<AdicionalDisponivel> usados = new HashSet<>();
        for (String nome : adicionais) {
            AdicionalDisponivel adicional = converterAdicional(nome);
            if (usados.add(adicional)) {
                atual = adicional.aplicarEm(atual);
            }
        }
        return atual;
    }

    private static AdicionalDisponivel converterAdicional(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Adicional inválido.");
        }
        try {
            return AdicionalDisponivel.valueOf(nome.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Adicional inválido: " + nome);
        }
    }
}