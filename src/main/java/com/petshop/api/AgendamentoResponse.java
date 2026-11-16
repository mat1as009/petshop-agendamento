package com.petshop.api;

import com.petshop.model.Agendamento;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AgendamentoResponse(int id, int petId, String servico, String descricao,
                                  LocalDate data, String hora, BigDecimal valor, String status,
                                  String observacoes, LocalDate dataCriacao) {

    public static AgendamentoResponse de(Agendamento a) {
        return new AgendamentoResponse(a.getId(), a.getPetId(), a.getServico().name(),
                a.getDescricao(), a.getData(), a.getHora().toString(), a.getValor(),
                a.getStatus().name(), a.getObservacoes(), a.getDataCriacao());
    }
}