package com.petshop.agenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.petshop.model.Agendamento;
import com.petshop.model.Servico;
import com.petshop.model.StatusAgendamento;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class AgendaTest {
    private static final LocalDate DIA = LocalDate.of(2030, 1, 15);
    private static final LocalDateTime ANTES_DO_DIA = LocalDateTime.of(2030, 1, 1, 12, 0);
    private static final LocalTime NOVE_HORAS = LocalTime.of(9, 0);

    private AgendamentoRepositoryFalso repositorio;
    private Agenda agenda;

    @Before
    public void preparar() {
        repositorio = new AgendamentoRepositoryFalso();
        agenda = new Agenda(repositorio);
    }

    @Test
    public void diaSemAgendamentosTemTodosOsHorarios() {
        List<LocalTime> livres = agenda.horariosLivres(DIA, ANTES_DO_DIA);

        assertEquals(10, livres.size());
    }

    @Test
    public void horarioOcupadoNaoApareceComoLivre() {
        repositorio.inserir(new Agendamento(1, Servico.BANHO, DIA, NOVE_HORAS));

        List<LocalTime> livres = agenda.horariosLivres(DIA, ANTES_DO_DIA);

        assertFalse(livres.contains(NOVE_HORAS));
        assertEquals(9, livres.size());
    }

    @Test
    public void agendamentoCanceladoLiberaOHorario() {
        Agendamento salvo = repositorio.inserir(new Agendamento(1, Servico.BANHO, DIA, NOVE_HORAS));
        repositorio.atualizarStatus(salvo.getId(), StatusAgendamento.CANCELADO);

        List<LocalTime> livres = agenda.horariosLivres(DIA, ANTES_DO_DIA);

        assertTrue(livres.contains(NOVE_HORAS));
    }

    @Test
    public void agendamentoDeOutroDiaNaoOcupaOHorario() {
        LocalDate outroDia = DIA.plusDays(1);
        repositorio.inserir(new Agendamento(1, Servico.BANHO, outroDia, NOVE_HORAS));

        List<LocalTime> livres = agenda.horariosLivres(DIA, ANTES_DO_DIA);

        assertTrue(livres.contains(NOVE_HORAS));
    }

    @Test
    public void horariosQueJaPassaramHojeNaoSaoOferecidos() {
        LocalDateTime meioDiaEMeia = LocalDateTime.of(2030, 1, 15, 12, 30);

        List<LocalTime> livres = agenda.horariosLivres(DIA, meioDiaEMeia);

        assertFalse(livres.contains(LocalTime.of(12, 0)));
        assertTrue(livres.contains(LocalTime.of(13, 0)));
        assertEquals(5, livres.size());
    }
}