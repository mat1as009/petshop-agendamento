package com.petshop.command;

import com.petshop.agenda.Agenda;
import com.petshop.model.Agendamento;
import com.petshop.model.Pet;
import com.petshop.model.Servico;
import com.petshop.repository.AgendamentoRepository;
import com.petshop.repository.PetRepository;
import com.petshop.servico.AdicionalDisponivel;
import com.petshop.servico.ServicoBase;
import com.petshop.servico.ServicoPetshop;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AgendarServicoComando implements Comando {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final Scanner scanner;
    private final PetRepository petRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final Agenda agenda;

    public AgendarServicoComando(Scanner scanner, PetRepository petRepository,
                                 AgendamentoRepository agendamentoRepository) {
        this.scanner = scanner;
        this.petRepository = petRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.agenda = new Agenda(agendamentoRepository);
    }

    @Override
    public void executar() {
        List<Pet> pets = petRepository.listar();
        if (pets.isEmpty()) {
            System.out.println("Cadastre um pet primeiro.");
            return;
        }

        pets.forEach(System.out::println);
        try {
            int petId = lerPet();
            Servico servico = escolherServico();
            ServicoPetshop completo = escolherAdicionais(servico);
            System.out.println("Serviço: " + completo.descricao() + " (R$ " + completo.preco() + ")");
            LocalDate data = lerData();
            LocalTime hora = escolherHorario(data);
            String observacoes = perguntar("Observações (opcional): ");

            Agendamento novo = new Agendamento(petId, servico, completo.descricao(),
                    completo.preco(), data, hora, observacoes);
            System.out.println("Agendado: " + agendamentoRepository.inserir(novo));
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida.");
        } catch (NumberFormatException e) {
            System.out.println("Número inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private int lerPet() {
        int id = Integer.parseInt(perguntar("ID do pet: "));
        if (petRepository.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Não existe pet com esse ID.");
        }
        return id;
    }

    private Servico escolherServico() {
        Servico[] servicos = Servico.values();
        System.out.println("Serviços:");
        for (int i = 0; i < servicos.length; i++) {
            System.out.println((i + 1) + " - " + servicos[i]);
        }
        int opcao = Integer.parseInt(perguntar("Escolha o serviço: "));
        if (opcao < 1 || opcao > servicos.length) {
            throw new IllegalArgumentException("Serviço inválido.");
        }
        return servicos[opcao - 1];
    }

    // Cada adicional escolhido embrulha o serviço anterior (Decorator)
    private ServicoPetshop escolherAdicionais(Servico servico) {
        ServicoPetshop atual = new ServicoBase(servico);
        AdicionalDisponivel[] adicionais = AdicionalDisponivel.values();
        System.out.println("Adicionais:");
        for (int i = 0; i < adicionais.length; i++) {
            System.out.println((i + 1) + " - " + adicionais[i]);
        }
        String texto = perguntar("Escolha (Enter para nenhum, ou vários separados por vírgula): ");
        if (texto.isEmpty()) {
            return atual;
        }

        Set<Integer> usados = new HashSet<>();
        for (String parte : texto.split(",")) {
            int opcao = Integer.parseInt(parte.trim());
            if (opcao < 1 || opcao > adicionais.length) {
                throw new IllegalArgumentException("Adicional inválido.");
            }
            if (usados.add(opcao)) {
                atual = adicionais[opcao - 1].aplicarEm(atual);
            }
        }
        return atual;
    }

    private LocalDate lerData() {
        LocalDate data = LocalDate.parse(perguntar("Data (dd/mm/aaaa): "), FORMATO);
        if (data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Não é possível agendar no passado.");
        }
        return data;
    }

    private LocalTime escolherHorario(LocalDate data) {
        List<LocalTime> livres = agenda.horariosLivres(data, LocalDateTime.now());
        if (livres.isEmpty()) {
            throw new IllegalArgumentException("Não há horários livres nesse dia.");
        }

        System.out.println("Horários livres:");
        for (int i = 0; i < livres.size(); i++) {
            System.out.println((i + 1) + " - " + livres.get(i));
        }
        int opcao = Integer.parseInt(perguntar("Escolha o horário: "));
        if (opcao < 1 || opcao > livres.size()) {
            throw new IllegalArgumentException("Horário inválido.");
        }
        return livres.get(opcao - 1);
    }

    private String perguntar(String pergunta) {
        System.out.print(pergunta);
        return scanner.nextLine().trim();
    }
}