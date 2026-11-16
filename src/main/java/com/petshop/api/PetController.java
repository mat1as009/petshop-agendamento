package com.petshop.api;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PostMapping
    public ResponseEntity<PetResponse> cadastrar(@RequestBody PetRequest dados) {
        PetResponse salvo = PetResponse.de(petService.cadastrar(dados));
        return ResponseEntity.created(URI.create("/api/pets/" + salvo.id())).body(salvo);
    }

    @PutMapping("/{id}")
    public PetResponse atualizar(@PathVariable("id") int id, @RequestBody PetRequest dados) {
        return PetResponse.de(petService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable("id") int id) {
        petService.excluir(id);
    }
}