package com.petshop.view;

import java.util.Scanner;

public class MenuView {
    private final Scanner scanner;

    public MenuView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibirMenu() {
        System.out.println();
        System.out.println("=== PETSHOP ===");
        System.out.println("1  - Cadastrar cliente");
        System.out.println("2  - Cadastrar pet");
        System.out.println("3  - Listar clientes");
        System.out.println("4  - Listar pets");
        System.out.println("5  - Pets com vacina vencida");
        System.out.println("6  - Editar cliente");
        System.out.println("7  - Excluir cliente");
        System.out.println("8  - Editar pet");
        System.out.println("9  - Excluir pet");
        System.out.println("10 - Agendar serviço");
        System.out.println("11 - Listar agendamentos");
        System.out.println("12 - Cancelar agendamento");
        System.out.println("13 - Concluir agendamento");
        System.out.println("14 - Ficha do pet");
        System.out.println("0  - Sair");
    }

    public String lerOpcao() {
        System.out.print("Escolha: ");
        return scanner.nextLine().trim();
    }

    public void mostrarMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}