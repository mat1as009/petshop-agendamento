package com.petshop.command;

import com.petshop.dao.AgendamentoDAO;
import com.petshop.dao.PetDAO;
import com.petshop.dao.VacinaDAO;
import com.petshop.model.Agendamento;
import com.petshop.model.Pet;
import com.petshop.model.Servico;
import com.petshop.model.StatusAgendamento;
import com.petshop.model.Vacina;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConcluirAgendamentoComando implements Comando {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Scanner scanner;
    private final AgendamentoDAO agendamentoDAO;
    private final PetDAO petDAO;
    private final VacinaDAO vacinaDAO;

    public ConcluirAgendamentoComando(Scanner scanner, AgendamentoDAO agendamentoDAO,
                                      PetDAO petDAO, VacinaDAO vacinaDAO) {
        this.scanner = scanner;
        this.agendamentoDAO = agendamentoDAO;
        this.petDAO = petDAO;
        this.vacinaDAO = vacinaDAO;
    }

    @Override
    public void executar() {
        List<Agendamento> agendados = agendamentoDAO.listar().stream()
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

        Optional<Agendamento> encontrado = agendamentoDAO.buscarPorId(id);
        if (encontrado.isEmpty() || encontrado.get().getStatus() != StatusAgendamento.AGENDADO) {
            System.out.println("Não existe agendamento em aberto com esse ID.");
            return;
        }
        Agendamento agendamento = encontrado.get();

        agendamentoDAO.atualizarStatus(id, StatusAgendamento.CONCLUIDO);
        System.out.println("Agendamento " + id + " concluído.");

        // Regra: concluir uma vacina grava no histórico e renova a próxima vacina do pet
        if (agendamento.getServico() == Servico.VACINA) {
            registrarVacina(agendamento.getPetId());
        }
    }

    private void registrarVacina(int petId) {
        LocalDate hoje = LocalDate.now();
        vacinaDAO.inserir(new Vacina(petId, hoje));

        Optional<Pet> encontrado = petDAO.buscarPorId(petId);
        if (encontrado.isPresent()) {
            Pet atual = encontrado.get();
            LocalDate proxima = hoje.plusYears(1);
            petDAO.atualizar(atual.comProximaVacina(proxima));
            System.out.println("Vacina registrada. Próxima vacina de " + atual.getNome()
                    + ": " + proxima.format(FORMATO));
        }
    }
}