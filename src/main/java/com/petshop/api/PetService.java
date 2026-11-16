package com.petshop.api;

import com.petshop.model.Pet;
import com.petshop.repository.AgendamentoRepository;
import com.petshop.repository.ClienteRepository;
import com.petshop.repository.PetRepository;
import com.petshop.repository.VacinaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final ClienteRepository clienteRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final VacinaRepository vacinaRepository;

    public PetService(PetRepository petRepository, ClienteRepository clienteRepository,
                      AgendamentoRepository agendamentoRepository,
                      VacinaRepository vacinaRepository) {
        this.petRepository = petRepository;
        this.clienteRepository = clienteRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.vacinaRepository = vacinaRepository;
    }

    public List<Pet> listar() {
        return petRepository.listar();
    }

    public List<Pet> listarVacinaVencida() {
        return petRepository.listarVacinaVencida();
    }

    public Pet buscar(int id) {
        return petRepository.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Pet não encontrado."));
    }

    public Pet cadastrar(PetRequest dados) {
        exigirDono(dados.clienteId());
        return petRepository.inserir(dados.paraPet(0, dados.clienteId()));
    }

    // O dono não muda: usamos o que já está gravado
    public Pet atualizar(int id, PetRequest dados) {
        Pet atual = buscar(id);
        Pet atualizado = dados.paraPet(id, atual.getClienteId());
        petRepository.atualizar(atualizado);
        return atualizado;
    }

    public void excluir(int id) {
        buscar(id);
        boolean temAgendamentos = agendamentoRepository.listar().stream()
                .anyMatch(a -> a.getPetId() == id);
        boolean temVacinas = !vacinaRepository.listarPorPet(id).isEmpty();
        if (temAgendamentos || temVacinas) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "O pet tem agendamentos ou vacinas registradas.");
        }
        petRepository.excluir(id);
    }

    private void exigirDono(int clienteId) {
        if (clienteId <= 0) {
            throw new IllegalArgumentException("O cliente (dono) é obrigatório.");
        }
        if (clienteRepository.buscarPorId(clienteId).isEmpty()) {
            throw new IllegalArgumentException("Não existe cliente com esse ID.");
        }
    }
}