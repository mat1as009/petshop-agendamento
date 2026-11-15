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
    private final LocalDate data;
    private final LocalTime hora;
    private final BigDecimal valor;
    private final StatusAgendamento status;

    // Agendamento novo: o valor é o preço do serviço neste momento
    public Agendamento(int petId, Servico servico, LocalDate data, LocalTime hora) {
        this(0, petId, servico, data, hora,
                servico == null ? null : servico.getPreco(), StatusAgendamento.AGENDADO);
    }

    // Agendamento que veio do banco
    public Agendamento(int id, int petId, Servico servico, LocalDate data, LocalTime hora,
                       BigDecimal valor, StatusAgendamento status) {
        if (petId <= 0) {
            throw new IllegalArgumentException("O agendamento precisa de um pet.");
        }
        if (servico == null || data == null || hora == null || valor == null || status == null) {
            throw new IllegalArgumentException("Dados do agendamento incompletos.");
        }
        this.id = id;
        this.petId = petId;
        this.servico = servico;
        this.data = data;
        this.hora = hora;
        this.valor = valor;
        this.status = status;
    }

    public int getId() { return id; }
    public int getPetId() { return petId; }
    public Servico getServico() { return servico; }
    public LocalDate getData() { return data; }
    public LocalTime getHora() { return hora; }
    public BigDecimal getValor() { return valor; }
    public StatusAgendamento getStatus() { return status; }

    @Override
    public String toString() {
        return id + " - Pet " + petId + " | " + servico.getDescricao()
                + " | " + data.format(FORMATO_DATA) + " " + hora
                + " | R$ " + valor + " | " + status;
    }
}