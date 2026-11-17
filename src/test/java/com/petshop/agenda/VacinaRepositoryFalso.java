package com.petshop.agenda;

import com.petshop.model.Vacina;
import com.petshop.repository.VacinaRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VacinaRepositoryFalso implements VacinaRepository {
    private final List<Vacina> vacinas = new ArrayList<>();

    @Override
    public Vacina inserir(Vacina vacina) {
        Vacina salva = new Vacina(vacinas.size() + 1, vacina.getPetId(), vacina.getDataAplicacao());
        vacinas.add(salva);
        return salva;
    }

    @Override
    public List<Vacina> listarPorPet(int petId) {
        return vacinas.stream()
                .filter(v -> v.getPetId() == petId)
                .sorted(Comparator.comparing(Vacina::getDataAplicacao).reversed())
                .toList();
    }
}