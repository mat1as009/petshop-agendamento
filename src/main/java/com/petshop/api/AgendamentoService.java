package com.petshop.api;

import com.petshop.agenda.Agenda;
import com.petshop.agenda.Vacinacao;
import com.petshop.model.Agendamento;
import com.petshop.model.Servico;
import com.petshop.model.StatusAgendamento;
import com.petshop.repository.AgendamentoRepository;
import com.petshop.repository.PetRepository;
import com.petshop.repository.VacinaRepository;
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
    private static final String STATUS_ACEITOS = "Status inválido. Use CANCELADO ou CONCLUIDO.";

    private final AgendamentoRepository agendamentoRepository;
    private final PetRepository petRepository;
    private final Agenda agenda;
    private final Vacinacao vacinacao;

    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              PetRepository petRepository, VacinaRepository vacinaRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.petRepository = petRepository;
        this.agenda = new Agenda(agendamentoRepository);
        this.vacinacao = new Vacinacao(vacinaRepository, petRepository);
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

    // Cancela ou conclui. Concluir uma vacina registra no histórico e renova a data do pet.
    public Agendamento alterarStatus(int id, String novoStatus) {
        Agendamento atual = buscar(id);
        StatusAgendamento status = converterStatus(novoStatus);
        if (atual.getStatus() != StatusAgendamento.AGENDADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só agendamentos em aberto podem ser alterados.");
        }

        agendamentoRepository.atualizarStatus(id, status);
        if (status == StatusAgendamento.CONCLUIDO && atual.getServico() == Servico.VACINA) {
            vacinacao.registrar(atual.getPetId(), LocalDate.now());
        }
        return buscar(id);
    }

    private StatusAgendamento converterStatus(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("O status é obrigatório.");
        }
        StatusAgendamento status;
        try {
            status = StatusAgendamento.valueOf(texto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(STATUS_ACEITOS);
        }
        if (status == StatusAgendamento.AGENDADO) {
            throw new IllegalArgumentException(STATUS_ACEITOS);
        }
        return status;
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