package com.petshop.repository;

import com.petshop.model.Vacina;
import java.util.List;

public interface VacinaRepository {
    Vacina inserir(Vacina vacina);

    List<Vacina> listarPorPet(int petId);
}