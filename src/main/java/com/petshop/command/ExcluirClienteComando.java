package com.petshop.command;

import com.petshop.dao.ClienteDAO;
import com.petshop.dao.PetDAO;
import com.petshop.model.Cliente;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ExcluirClienteComando implements Comando {
    private final Scanner scanner;
    private final ClienteDAO clienteDAO;
    private final PetDAO petDAO;

    public ExcluirClienteComando(Scanner scanner, ClienteDAO clienteDAO, PetDAO petDAO) {
        this.scanner = scanner;
        this.clienteDAO = clienteDAO;
        this.petDAO = petDAO;
    }

    @Override
    public void executar() {
        List<Cliente> clientes = clienteDAO.listar();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        clientes.forEach(System.out::println);
        System.out.print("ID do cliente a excluir: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        Optional<Cliente> encontrado = clienteDAO.buscarPorId(id);
        if (encontrado.isEmpty()) {
            System.out.println("Não existe cliente com esse ID.");
            return;
        }

        if (petDAO.existePetDoCliente(id)) {
            System.out.println("Este cliente ainda tem pets cadastrados. Exclua os pets antes.");
            return;
        }

        System.out.print("Excluir " + encontrado.get().getNome() + "? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            clienteDAO.excluir(id);
            System.out.println("Cliente excluído.");
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}