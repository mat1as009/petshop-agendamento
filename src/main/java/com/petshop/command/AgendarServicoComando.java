package com.petshop.command;

import com.petshop.dao.AgendamentoDAO;
import com.petshop.dao.PetDAO;
import com.petshop.model.Agendamento;
import com.petshop.model.Pet;
import com.petshop.model.Servico;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AgendarServicoComando implements Comando {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final int PRIMEIRA_HORA = 8;
    private static final int ULTIMA_HORA = 17;

    private final Scanner scanner;
    private final PetDAO petDAO;
    private final AgendamentoDAO agendamentoDAO;

    public AgendarServicoComando(Scanner scanner, PetDAO petDAO, AgendamentoDAO agendamentoDAO) {
        this.scanner = scanner;
        this.petDAO = petDAO;
        this.agendamentoDAO = agendamentoDAO;
    }

    @Override
    public void executar() {
        List<Pet> pets = petDAO.listar();
        if (pets.isEmpty()) {
            System.out.println("Cadastre um pet primeiro.");
            return;
        }

        pets.forEach(System.out::println);
        System.out.print("ID do pet: ");
        int petId = lerInteiro();
        if (petDAO.buscarPorId(petId).isEmpty()) {
            System.out.println("Não existe pet com esse ID.");
            return;
        }

        Servico[] servicos = Servico.values();
        System.out.println("Serviços:");
        for (int i = 0; i < servicos.length; i++) {
            System.out.println((i + 1) + " - " + servicos[i]);
        }
        System.out.print("Escolha o serviço: ");
        int opcaoServico = lerInteiro();
        if (opcaoServico < 1 || opcaoServico > servicos.length) {
            System.out.println("Serviço inválido.");
            return;
        }
        Servico servico = servicos[opcaoServico - 1];

        System.out.print("Data (dd/mm/aaaa): ");
        LocalDate data;
        try {
            data = LocalDate.parse(scanner.nextLine().trim(), FORMATO);
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida.");
            return;
        }
        if (data.isBefore(LocalDate.now())) {
            System.out.println("Não é possível agendar no passado.");
            return;
        }

        // Só aparecem os horários que ainda estão livres
        List<LocalTime> ocupados = agendamentoDAO.horariosOcupados(data);
        List<LocalTime> livres = new ArrayList<>();
        for (int hora = PRIMEIRA_HORA; hora <= ULTIMA_HORA; hora++) {
            LocalTime horario = LocalTime.of(hora, 0);
            boolean jaPassou = data.equals(LocalDate.now()) && !horario.isAfter(LocalTime.now());
            if (!ocupados.contains(horario) && !jaPassou) {
                livres.add(horario);
            }
        }
        if (livres.isEmpty()) {
            System.out.println("Não há horários livres nesse dia.");
            return;
        }

        System.out.println("Horários livres:");
        for (int i = 0; i < livres.size(); i++) {
            System.out.println((i + 1) + " - " + livres.get(i));
        }
        System.out.print("Escolha o horário: ");
        int opcaoHorario = lerInteiro();
        if (opcaoHorario < 1 || opcaoHorario > livres.size()) {
            System.out.println("Horário inválido.");
            return;
        }

        Agendamento novo = new Agendamento(petId, servico, data, livres.get(opcaoHorario - 1));
        System.out.println("Agendado: " + agendamentoDAO.inserir(novo));
    }

    private int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}