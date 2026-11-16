package com.petshop.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AgendamentoRequest(int petId, String servico, List<String> adicionais,
                                 LocalDate data, LocalTime hora, String observacoes) {
}