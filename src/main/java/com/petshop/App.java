package com.petshop;

import com.petshop.controller.MenuController;
import com.petshop.dao.AgendamentoDAO;
import com.petshop.dao.ClienteDAO;
import com.petshop.dao.Conexao;
import com.petshop.dao.PetDAO;
import com.petshop.dao.VacinaDAO;
import com.petshop.factory.AgendarServicoFactory;
import com.petshop.factory.CadastrarClienteFactory;
import com.petshop.factory.CadastrarPetFactory;
import com.petshop.factory.CancelarAgendamentoFactory;
import com.petshop.factory.ComandoFactory;
import com.petshop.factory.ConcluirAgendamentoFactory;
import com.petshop.factory.EditarClienteFactory;
import com.petshop.factory.EditarPetFactory;
import com.petshop.factory.ExcluirClienteFactory;
import com.petshop.factory.ExcluirPetFactory;
import com.petshop.factory.FichaPetFactory;
import com.petshop.factory.ListarAgendamentosFactory;
import com.petshop.factory.ListarClientesFactory;
import com.petshop.factory.ListarPetsFactory;
import com.petshop.factory.VacinaVencidaFactory;
import com.petshop.view.MenuView;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Conexao.criarTabelas();

        Scanner scanner = new Scanner(System.in);
        ClienteDAO clienteDAO = new ClienteDAO();
        PetDAO petDAO = new PetDAO();
        AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
        VacinaDAO vacinaDAO = new VacinaDAO();

        Map<String, ComandoFactory> fabricas = new HashMap<>();
        fabricas.put("1", new CadastrarClienteFactory(scanner, clienteDAO));
        fabricas.put("2", new CadastrarPetFactory(scanner, clienteDAO, petDAO));
        fabricas.put("3", new ListarClientesFactory(clienteDAO));
        fabricas.put("4", new ListarPetsFactory(petDAO));
        fabricas.put("5", new VacinaVencidaFactory(petDAO));
        fabricas.put("6", new EditarClienteFactory(scanner, clienteDAO));
        fabricas.put("7", new ExcluirClienteFactory(scanner, clienteDAO, petDAO));
        fabricas.put("8", new EditarPetFactory(scanner, petDAO));
        fabricas.put("9", new ExcluirPetFactory(scanner, petDAO));
        fabricas.put("10", new AgendarServicoFactory(scanner, petDAO, agendamentoDAO));
        fabricas.put("11", new ListarAgendamentosFactory(agendamentoDAO));
        fabricas.put("12", new CancelarAgendamentoFactory(scanner, agendamentoDAO));
        fabricas.put("13", new ConcluirAgendamentoFactory(scanner, agendamentoDAO, petDAO, vacinaDAO));
        fabricas.put("14", new FichaPetFactory(scanner, petDAO, clienteDAO, vacinaDAO));

        MenuView view = new MenuView(scanner);
        MenuController controller = new MenuController(view, fabricas);
        controller.iniciar();

        scanner.close();
    }
}