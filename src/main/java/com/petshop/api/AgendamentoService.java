package com.petshop.api;

import com.petshop.agenda.Agenda;
import com.petshop.model.Agendamento;
import com.petshop.model.Servico;
import com.petshop.repository.AgendamentoRepository;
import com.petshop.repository.PetRepository;
import com.petshop.servico.ServicoPetshop;
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
    private final PetRepository petRepository;
    private final Agenda agenda;

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              PetRepository petRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.petRepository = petRepository;
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

    public Agendamento agendar(AgendamentoRequest dados) {
        exigirPet(dados.petId());
        Servico servico = MontadorDeServico.converterServico(dados.servico());
        ServicoPetshop completo = MontadorDeServico.montar(servico, dados.adicionais());
        exigirHorarioLivre(dados.data(), dados.hora());

        Agendamento novo = new Agendamento(dados.petId(), servico, completo.descricao(),
                completo.preco(), dados.data(), dados.hora(), dados.observacoes());
        return agendamentoRepository.inserir(novo);
    }

    private void exigirPet(int petId) {
        if (petId <= 0 || petRepository.buscarPorId(petId).isEmpty()) {
            throw new IllegalArgumentException("Não existe pet com esse ID.");
        }
    }

    private void exigirHorarioLivre(LocalDate data, LocalTime hora) {
        if (data == null || hora == null) {
            throw new IllegalArgumentException("Data e hora são obrigatórias.");
        }
        if (horariosLivres(data).contains(hora)) {
            return;
        }
        if (agendamentoRepository.horariosOcupados(data).contains(hora)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esse horário já está ocupado.");
        }
        throw new IllegalArgumentException("Horário indisponível. Escolha um horário livre do dia.");
    }
}