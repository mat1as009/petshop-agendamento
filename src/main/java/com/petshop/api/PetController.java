package com.petshop.api;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<PetResponse> listar() {
        return petService.listar().stream().map(PetResponse::de).toList();
    }

    @GetMapping("/vacina-vencida")
    public List<PetResponse> listarVacinaVencida() {
        return petService.listarVacinaVencida().stream().map(PetResponse::de).toList();
    }

    @GetMapping("/{id}")
    public PetResponse buscar(@PathVariable("id") int id) {
        return PetResponse.de(petService.buscar(id));
    }
}