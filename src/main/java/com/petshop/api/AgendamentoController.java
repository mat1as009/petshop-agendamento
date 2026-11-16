package com.petshop.api;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {
    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public List<AgendamentoResponse> listar() {
        return agendamentoService.listar().stream().map(AgendamentoResponse::de).toList();
    }

    @GetMapping("/horarios-livres")
    public List<String> horariosLivres(@RequestParam("data") LocalDate data) {
        return agendamentoService.horariosLivres(data).stream().map(Object::toString).toList();
    }

    @GetMapping("/{id}")
    public AgendamentoResponse buscar(@PathVariable("id") int id) {
        return AgendamentoResponse.de(agendamentoService.buscar(id));
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponse> agendar(@RequestBody AgendamentoRequest dados) {
        AgendamentoResponse salvo = AgendamentoResponse.de(agendamentoService.agendar(dados));
        return ResponseEntity.created(URI.create("/api/agendamentos/" + salvo.id())).body(salvo);
    }
}