package com.petshop.agenda;

import com.petshop.repository.AgendamentoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private static final int PRIMEIRA_HORA = 8;
    private static final int ULTIMA_HORA = 17;

    private final AgendamentoRepository agendamentoRepository;

    public Agenda(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    // Regra: um horário só pode ter um agendamento ainda "AGENDADO"
    public List<LocalTime> horariosLivres(LocalDate data, LocalDateTime agora) {
        List<LocalTime> ocupados = agendamentoRepository.horariosOcupados(data);
        List<LocalTime> livres = new ArrayList<>();
        for (int hora = PRIMEIRA_HORA; hora <= ULTIMA_HORA; hora++) {
            LocalTime horario = LocalTime.of(hora, 0);
            if (estaLivre(data, horario, ocupados, agora)) {
                livres.add(horario);
            }
        }
        return livres;
    }

    private boolean estaLivre(LocalDate data, LocalTime horario,
                              List<LocalTime> ocupados, LocalDateTime agora) {
        boolean jaPassou = data.equals(agora.toLocalDate()) && !horario.isAfter(agora.toLocalTime());
        return !ocupados.contains(horario) && !jaPassou;
    }
}