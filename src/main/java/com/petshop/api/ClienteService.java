package com.petshop.api;

import com.petshop.model.Cliente;
import com.petshop.repository.ClienteRepository;
import com.petshop.repository.PetRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final PetRepository petRepository;

    public ClienteService(ClienteRepository clienteRepository, PetRepository petRepository) {
        this.clienteRepository = clienteRepository;
        this.petRepository = petRepository;
    }

    public List<Cliente> listar() {
        return clienteRepository.listar();
    }

    public Cliente buscar(int id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente não encontrado."));
    }

    public Cliente cadastrar(ClienteRequest dados) {
        return clienteRepository.inserir(dados.paraCliente(0, null));
    }

    public Cliente atualizar(int id, ClienteRequest dados) {
        Cliente atual = buscar(id);
        Cliente atualizado = dados.paraCliente(id, atual.getDataCadastro());
        clienteRepository.atualizar(atualizado);
        return atualizado;
    }

    public void excluir(int id) {
        buscar(id);
        if (petRepository.existePetDoCliente(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "O cliente ainda tem pets cadastrados.");
        }
        clienteRepository.excluir(id);
    }
}