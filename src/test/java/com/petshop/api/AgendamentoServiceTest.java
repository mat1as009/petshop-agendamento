package com.petshop.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.petshop.agenda.AgendamentoRepositoryFalso;
import com.petshop.agenda.PetRepositoryFalso;
import com.petshop.agenda.VacinaRepositoryFalso;
import com.petshop.builder.PetBuilder;
import com.petshop.model.Agendamento;
import com.petshop.model.Pet;
import com.petshop.model.Sexo;
import com.petshop.model.StatusAgendamento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AgendamentoServiceTest {
    private static final LocalDate DIA = LocalDate.now().plusDays(10);
    private static final LocalTime NOVE_HORAS = LocalTime.of(9, 0);

    private PetRepositoryFalso petRepository;
    private VacinaRepositoryFalso vacinaRepository;
    private AgendamentoService service;
    private Pet pet;

    @Before
    public void preparar() {
        petRepository = new PetRepositoryFalso();
        vacinaRepository = new VacinaRepositoryFalso();
        service = new AgendamentoService(new AgendamentoRepositoryFalso(), petRepository,
                vacinaRepository);
        pet = petRepository.inserir(new PetBuilder()
                .nome("Rex")
                .especie("Cachorro")
                .sexo(Sexo.MACHO)
                .proximaVacina(LocalDate.of(2020, 1, 1))
                .clienteId(1)
                .build());
    }

    private AgendamentoRequest pedido(String servico, List<String> adicionais, LocalTime hora) {
        return new AgendamentoRequest(pet.getId(), servico, adicionais, DIA, hora, "");
    }

    @Test
    public void agendarGuardaDescricaoEPrecoComOsAdicionais() {
        Agendamento agendado = service.agendar(
                pedido("BANHO", List.of("HIDRATACAO", "PERFUME"), NOVE_HORAS));

        assertEquals("Banho + Hidratação + Perfume", agendado.getDescricao());
        assertEquals(new BigDecimal("75.00"), agendado.getValor());
        assertEquals(StatusAgendamento.AGENDADO, agendado.getStatus());
    }

    @Test
    public void agendarNoMesmoHorarioLancaConflito() {
        service.agendar(pedido("BANHO", List.of(), NOVE_HORAS));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.agendar(pedido("TOSA", List.of(), NOVE_HORAS)));

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
    }

    @Test
    public void agendarParaPetInexistenteEhRecusado() {
        AgendamentoRequest pedido = new AgendamentoRequest(999, "BANHO", List.of(), DIA, NOVE_HORAS, "");

        assertThrows(IllegalArgumentException.class, () -> service.agendar(pedido));
    }

    @Test
    public void agendarForaDosHorariosDaAgendaEhRecusado() {
        assertThrows(IllegalArgumentException.class,
                () -> service.agendar(pedido("BANHO", List.of(), LocalTime.of(9, 30))));
    }

    @Test
    public void agendarNoPassadoEhRecusado() {
        AgendamentoRequest pedido = new AgendamentoRequest(pet.getId(), "BANHO", List.of(),
                LocalDate.now().minusDays(1), NOVE_HORAS, "");

        assertThrows(IllegalArgumentException.class, () -> service.agendar(pedido));
    }

    @Test
    public void adicionalInexistenteEhRecusado() {
        assertThrows(IllegalArgumentException.class,
                () -> service.agendar(pedido("BANHO", List.of("BRILHO"), NOVE_HORAS)));
    }

    @Test
    public void cancelarLiberaOHorario() {
        Agendamento agendado = service.agendar(pedido("BANHO", List.of(), NOVE_HORAS));
        assertFalse(service.horariosLivres(DIA).contains(NOVE_HORAS));

        service.alterarStatus(agendado.getId(), "CANCELADO");

        assertTrue(service.horariosLivres(DIA).contains(NOVE_HORAS));
        service.agendar(pedido("TOSA", List.of(), NOVE_HORAS));
    }

    @Test
    public void concluirUmaVacinaRegistraNoHistoricoERenovaAData() {
        Agendamento agendado = service.agendar(pedido("VACINA", List.of(), NOVE_HORAS));

        Agendamento concluido = service.alterarStatus(agendado.getId(), "CONCLUIDO");

        assertEquals(StatusAgendamento.CONCLUIDO, concluido.getStatus());
        assertEquals(1, vacinaRepository.listarPorPet(pet.getId()).size());
        Pet atualizado = petRepository.buscarPorId(pet.getId()).get();
        assertEquals(LocalDate.now().plusYears(1), atualizado.getProximaVacina());
        assertFalse(atualizado.isVacinaVencida());
    }

    @Test
    public void concluirOutroServicoNaoMexeNaVacina() {
        Agendamento agendado = service.agendar(pedido("BANHO", List.of(), NOVE_HORAS));

        service.alterarStatus(agendado.getId(), "CONCLUIDO");

        assertTrue(vacinaRepository.listarPorPet(pet.getId()).isEmpty());
        assertTrue(petRepository.buscarPorId(pet.getId()).get().isVacinaVencida());
    }

    @Test
    public void alterarUmAgendamentoJaConcluidoLancaConflito() {
        Agendamento agendado = service.agendar(pedido("BANHO", List.of(), NOVE_HORAS));
        service.alterarStatus(agendado.getId(), "CONCLUIDO");

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.alterarStatus(agendado.getId(), "CANCELADO"));

        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
    }

    @Test
    public void statusInvalidoEhRecusado() {
        Agendamento agendado = service.agendar(pedido("BANHO", List.of(), NOVE_HORAS));

        assertThrows(IllegalArgumentException.class,
                () -> service.alterarStatus(agendado.getId(), "X"));
        assertThrows(IllegalArgumentException.class,
                () -> service.alterarStatus(agendado.getId(), "AGENDADO"));
    }

    @Test
    public void buscarAgendamentoInexistenteLancaNaoEncontrado() {
        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.buscar(999));

        assertEquals(HttpStatus.NOT_FOUND, erro.getStatusCode());
    }
}