package com.petshop.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Agendamento {
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final int id;
    private final int petId;
    private final Servico servico;
    private final String descricao;
    private final LocalDate data;
    private final LocalTime hora;
    private final BigDecimal valor;
    private final StatusAgendamento status;
    private final String observacoes;
    private final LocalDate dataCriacao;

    // Serviço simples, sem adicionais
    public Agendamento(int petId, Servico servico, LocalDate data, LocalTime hora) {
        this(petId, servico, servico == null ? null : servico.getDescricao(),
                servico == null ? null : servico.getPreco(), data, hora, "");
    }

    // Agendamento novo: descrição e valor já vêm calculados (com os adicionais)
    public Agendamento(int petId, Servico servico, String descricao, BigDecimal valor,
                       LocalDate data, LocalTime hora, String observacoes) {
        this(0, petId, servico, descricao, data, hora, valor,
                StatusAgendamento.AGENDADO, observacoes, LocalDate.now());
    }

    // Agendamento que veio do banco
    public Agendamento(int id, int petId, Servico servico, String descricao, LocalDate data,
                       LocalTime hora, BigDecimal valor, StatusAgendamento status,
                       String observacoes, LocalDate dataCriacao) {
        if (petId <= 0) {
            throw new IllegalArgumentException("O agendamento precisa de um pet.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição do serviço é obrigatória.");
        }
        if (servico == null || data == null || hora == null || valor == null
                || status == null || dataCriacao == null) {
            throw new IllegalArgumentException("Dados do agendamento incompletos.");
        }
        this.id = id;
        this.petId = petId;
        this.servico = servico;
        this.descricao = descricao.trim();
        this.data = data;
        this.hora = hora;
        this.valor = valor;
        this.status = status;
        this.observacoes = observacoes == null ? "" : observacoes.trim();
        this.dataCriacao = dataCriacao;
    }

    public Agendamento comId(int novoId) {
        return new Agendamento(novoId, petId, servico, descricao, data, hora, valor,
                status, observacoes, dataCriacao);
    }

    public int getId() { return id; }
    public int getPetId() { return petId; }
    public Servico getServico() { return servico; }
    public String getDescricao() { return descricao; }
    public LocalDate getData() { return data; }
    public LocalTime getHora() { return hora; }
    public BigDecimal getValor() { return valor; }
    public StatusAgendamento getStatus() { return status; }
    public String getObservacoes() { return observacoes; }
    public LocalDate getDataCriacao() { return dataCriacao; }

    @Override
    public String toString() {
        return id + " - Pet " + petId + " | " + descricao
                + " | " + data.format(FORMATO_DATA) + " " + hora
                + " | R$ " + valor + " | " + status;
    }
}