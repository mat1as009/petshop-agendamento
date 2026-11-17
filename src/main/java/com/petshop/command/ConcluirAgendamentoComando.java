package com.petshop.command;

import com.petshop.agenda.Vacinacao;
import com.petshop.model.Agendamento;
import com.petshop.model.Pet;
import com.petshop.model.Servico;
import com.petshop.model.StatusAgendamento;
import com.petshop.repository.AgendamentoRepository;
import com.petshop.repository.PetRepository;
import com.petshop.repository.VacinaRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConcluirAgendamentoComando implements Comando {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Scanner scanner;
    private final AgendamentoRepository agendamentoRepository;
    private final Vacinacao vacinacao;

    public ConcluirAgendamentoComando(Scanner scanner, AgendamentoRepository agendamentoRepository,
                                      PetRepository petRepository, VacinaRepository vacinaRepository) {
        this.scanner = scanner;
        this.agendamentoRepository = agendamentoRepository;
        this.vacinacao = new Vacinacao(vacinaRepository, petRepository);
    }

    @Override
    public void executar() {
        List<Agendamento> agendados = agendamentoRepository.listar().stream()
                .filter(a -> a.getStatus() == StatusAgendamento.AGENDADO)
                .toList();
        if (agendados.isEmpty()) {
            System.out.println("Nenhum agendamento em aberto.");
            return;
        }

        agendados.forEach(System.out::println);
        System.out.print("ID do agendamento: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        Optional<Agendamento> encontrado = agendamentoRepository.buscarPorId(id);
        if (encontrado.isEmpty() || encontrado.get().getStatus() != StatusAgendamento.AGENDADO) {
            System.out.println("Não existe agendamento em aberto com esse ID.");
            return;
        }
        Agendamento agendamento = encontrado.get();

        agendamentoRepository.atualizarStatus(id, StatusAgendamento.CONCLUIDO);
        System.out.println("Agendamento " + id + " concluído.");

        if (agendamento.getServico() == Servico.VACINA) {
            registrarVacina(agendamento.getPetId());
        }
    }

    private void registrarVacina(int petId) {
        Optional<Pet> pet = vacinacao.registrar(petId, LocalDate.now());
        if (pet.isPresent()) {
            System.out.println("Vacina registrada. Próxima vacina de " + pet.get().getNome()
                    + ": " + pet.get().getProximaVacina().format(FORMATO));
        }
    }
}