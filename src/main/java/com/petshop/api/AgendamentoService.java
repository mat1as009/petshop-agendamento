package com.petshop.api;

import com.petshop.agenda.Agenda;
import com.petshop.model.Agendamento;
import com.petshop.repository.AgendamentoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AgendamentoService {
    private final AgendamentoRepository agendamentoRepository;
    private final Agenda agenda;

    public AgendamentoService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.agenda = new Agenda(agendamentoRepository);
    }

    public List<Agendamento> listar() {
        return agendamentoRepository.listar();
    }

    public Agendamento buscar(int id) {
        return agendamentoRepository.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Agendamento não encontrado."));
    }

    public List<LocalTime> horariosLivres(LocalDate data) {
        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Não é possível agendar no passado.");
        }
        return agenda.horariosLivres(data, LocalDateTime.now());
    }
}