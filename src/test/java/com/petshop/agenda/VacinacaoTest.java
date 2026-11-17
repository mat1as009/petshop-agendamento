package com.petshop.agenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.petshop.builder.PetBuilder;
import com.petshop.model.Pet;
import com.petshop.model.Sexo;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class VacinacaoTest {
    private static final LocalDate DIA_DA_VACINA = LocalDate.of(2030, 3, 10);

    private PetRepositoryFalso petRepository;
    private VacinaRepositoryFalso vacinaRepository;
    private Vacinacao vacinacao;
    private Pet pet;

    @Before
    public void preparar() {
        petRepository = new PetRepositoryFalso();
        vacinaRepository = new VacinaRepositoryFalso();
        vacinacao = new Vacinacao(vacinaRepository, petRepository);
        pet = petRepository.inserir(new PetBuilder()
                .nome("Rex")
                .especie("Cachorro")
                .sexo(Sexo.MACHO)
                .proximaVacina(LocalDate.of(2020, 1, 1))
                .clienteId(1)
                .build());
    }

    @Test
    public void registrarGravaAVacinaNoHistoricoDoPet() {
        vacinacao.registrar(pet.getId(), DIA_DA_VACINA);

        assertEquals(1, vacinaRepository.listarPorPet(pet.getId()).size());
        assertEquals(DIA_DA_VACINA, vacinaRepository.listarPorPet(pet.getId()).get(0).getDataAplicacao());
    }

    @Test
    public void registrarRenovaAProximaVacinaEmUmAno() {
        Optional<Pet> renovado = vacinacao.registrar(pet.getId(), DIA_DA_VACINA);

        assertEquals(DIA_DA_VACINA.plusYears(1), renovado.get().getProximaVacina());
        assertEquals(DIA_DA_VACINA.plusYears(1),
                petRepository.buscarPorId(pet.getId()).get().getProximaVacina());
    }

    @Test
    public void depoisDeVacinarOPetDeixaDeEstarVencido() {
        assertTrue(petRepository.buscarPorId(pet.getId()).get().isVacinaVencida());

        vacinacao.registrar(pet.getId(), DIA_DA_VACINA);

        assertFalse(petRepository.buscarPorId(pet.getId()).get().isVacinaVencida());
    }

    @Test
    public void registrarMantemOsOutrosDadosDoPet() {
        Pet renovado = vacinacao.registrar(pet.getId(), DIA_DA_VACINA).get();

        assertEquals("Rex", renovado.getNome());
        assertEquals(pet.getClienteId(), renovado.getClienteId());
    }

    @Test
    public void historicoComecaPelaVacinaMaisRecente() {
        vacinacao.registrar(pet.getId(), DIA_DA_VACINA);
        vacinacao.registrar(pet.getId(), DIA_DA_VACINA.plusYears(1));

        assertEquals(2, vacinaRepository.listarPorPet(pet.getId()).size());
        assertEquals(DIA_DA_VACINA.plusYears(1),
                vacinaRepository.listarPorPet(pet.getId()).get(0).getDataAplicacao());
    }

    @Test
    public void petInexistenteNaoDevolveNada() {
        Optional<Pet> resultado = vacinacao.registrar(999, DIA_DA_VACINA);

        assertTrue(resultado.isEmpty());
    }
}