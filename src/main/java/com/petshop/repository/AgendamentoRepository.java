package com.petshop.repository;

import com.petshop.model.Agendamento;
import com.petshop.model.StatusAgendamento;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository {
    Agendamento inserir(Agendamento agendamento);

    List<Agendamento> listar();

    Optional<Agendamento> buscarPorId(int id);

    void atualizarStatus(int id, StatusAgendamento status);

    List<LocalTime> horariosOcupados(LocalDate data);
}