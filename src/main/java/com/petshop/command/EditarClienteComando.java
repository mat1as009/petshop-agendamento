package com.petshop.command;

import com.petshop.builder.ClienteBuilder;
import com.petshop.dao.ClienteDAO;
import com.petshop.model.Cliente;
import com.petshop.model.Endereco;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class EditarClienteComando implements Comando {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final Scanner scanner;
    private final ClienteDAO clienteDAO;

    public EditarClienteComando(Scanner scanner, ClienteDAO clienteDAO) {
        this.scanner = scanner;
        this.clienteDAO = clienteDAO;
    }

    @Override
    public void executar() {
        List<Cliente> clientes = clienteDAO.listar();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        clientes.forEach(System.out::println);
        System.out.print("ID do cliente a editar: ");
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
        Cliente atual = encontrado.get();
        System.out.println("Enter mantém o valor atual. Digite - para apagar um campo opcional.");

        try {
            Cliente atualizado = new ClienteBuilder()
                    .id(id)
                    .nome(novoValor("Nome", atual.getNome()))
                    .cpf(novoValor("CPF", atual.getCpfFormatado()))
                    .dataNascimento(novaData("Data de nascimento", atual.getDataNascimento()))
                    .telefone(novoValor("Telefone", atual.getTelefone()))
                    .telefoneSecundario(novoValor("Telefone secundário", atual.getTelefoneSecundario()))
                    .email(novoValor("Email", atual.getEmail()))
                    .observacoes(novoValor("Observações", atual.getObservacoes()))
                    .endereco(editarEndereco(atual.getEndereco()))
                    .dataCadastro(atual.getDataCadastro())
                    .build();
            clienteDAO.atualizar(atualizado);
            System.out.println("Cliente atualizado: " + atualizado);
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private Endereco editarEndereco(Endereco atual) {
        System.out.print("Editar o endereço? (s/n): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("s")) {
            return atual;
        }
        return new Endereco(
                novoValor("  CEP", atual.getCepFormatado()),
                novoValor("  Logradouro", atual.getLogradouro()),
                novoValor("  Número", atual.getNumero()),
                novoValor("  Complemento", atual.getComplemento()),
                novoValor("  Bairro", atual.getBairro()),
                novoValor("  Cidade", atual.getCidade()),
                novoValor("  Estado (UF)", atual.getEstado()));
    }

    private String novoValor(String rotulo, String atual) {
        System.out.print(rotulo + " [" + atual + "]: ");
        String texto = scanner.nextLine().trim();
        if (texto.isEmpty()) {
            return atual;
        }
        return texto.equals("-") ? "" : texto;
    }

    private LocalDate novaData(String rotulo, LocalDate atual) {
        String exibicao = atual == null ? "sem data" : FORMATO.format(atual);
        System.out.print(rotulo + " dd/mm/aaaa [" + exibicao + "]: ");
        String texto = scanner.nextLine().trim();
        if (texto.isEmpty()) {
            return atual;
        }
        return texto.equals("-") ? null : LocalDate.parse(texto, FORMATO);
    }
}