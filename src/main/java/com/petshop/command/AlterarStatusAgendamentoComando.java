package com.petshop.command;

import com.petshop.dao.AgendamentoDAO;
import com.petshop.model.Agendamento;
import com.petshop.model.StatusAgendamento;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AlterarStatusAgendamentoComando implements Comando {
    private final Scanner scanner;
    private final AgendamentoDAO agendamentoDAO;
    private final StatusAgendamento novoStatus;

    public AlterarStatusAgendamentoComando(Scanner scanner, AgendamentoDAO agendamentoDAO,
                                           StatusAgendamento novoStatus) {
        this.scanner = scanner;
        this.agendamentoDAO = agendamentoDAO;
        this.novoStatus = novoStatus;
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

        agendamentoDAO.atualizarStatus(id, novoStatus);
        System.out.println("Agendamento " + id + " agora está: " + novoStatus);
    }
}