package com.petshop.agenda;

import com.petshop.model.Agendamento;
import com.petshop.model.StatusAgendamento;
import com.petshop.repository.AgendamentoRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgendamentoRepositoryFalso implements AgendamentoRepository {
    private final List<Agendamento> agendamentos = new ArrayList<>();

    @Override
    public Agendamento inserir(Agendamento agendamento) {
        Agendamento salvo = agendamento.comId(agendamentos.size() + 1);
        agendamentos.add(salvo);
        return salvo;
    }

    @Override
    public List<Agendamento> listar() {
        return new ArrayList<>(agendamentos);
    }

    @Override
    public Optional<Agendamento> buscarPorId(int id) {
        return agendamentos.stream().filter(a -> a.getId() == id).findFirst();
    }

    @Override
    public void atualizarStatus(int id, StatusAgendamento status) {
        for (int i = 0; i < agendamentos.size(); i++) {
            Agendamento a = agendamentos.get(i);
            if (a.getId() == id) {
                agendamentos.set(i, new Agendamento(a.getId(), a.getPetId(), a.getServico(),
                        a.getDescricao(), a.getData(), a.getHora(), a.getValor(), status,
                        a.getObservacoes(), a.getDataCriacao()));
            }
        }
    }

    @Override
    public List<LocalTime> horariosOcupados(LocalDate data) {
        return agendamentos.stream()
                .filter(a -> a.getData().equals(data))
                .filter(a -> a.getStatus() == StatusAgendamento.AGENDADO)
                .map(Agendamento::getHora)
                .toList();
    }
}