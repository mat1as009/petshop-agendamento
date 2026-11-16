package com.petshop.api;

import com.petshop.model.Servico;
import com.petshop.servico.AdicionalDisponivel;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    @GetMapping
    public CatalogoResponse catalogo() {
        List<ServicoResponse> servicos = Arrays.stream(Servico.values())
                .map(s -> new ServicoResponse(s.name(), s.getDescricao(), s.getPreco()))
                .toList();
        List<AdicionalResponse> adicionais = Arrays.stream(AdicionalDisponivel.values())
                .map(a -> new AdicionalResponse(a.name(), a.toString()))
                .toList();
        return new CatalogoResponse(servicos, adicionais);
    }

    public record ServicoResponse(String id, String descricao, BigDecimal preco) {
    }

    public record AdicionalResponse(String id, String descricao) {
    }

    public record CatalogoResponse(List<ServicoResponse> servicos,
                                   List<AdicionalResponse> adicionais) {
    }
}