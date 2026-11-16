package com.petshop.api;

import com.petshop.model.Pet;
import com.petshop.repository.PetRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
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
}