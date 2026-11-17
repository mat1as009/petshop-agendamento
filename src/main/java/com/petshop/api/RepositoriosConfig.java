package com.petshop.api;

import com.petshop.dao.AgendamentoJdbcRepository;
import com.petshop.dao.ClienteJdbcRepository;
import com.petshop.dao.PetJdbcRepository;
import com.petshop.dao.VacinaJdbcRepository;
import com.petshop.repository.AgendamentoRepository;
import com.petshop.repository.ClienteRepository;
import com.petshop.repository.PetRepository;
import com.petshop.repository.VacinaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoriosConfig {

    @Bean
    public ClienteRepository clienteRepository() {
        return new ClienteJdbcRepository();
    }

    @Bean
    public PetRepository petRepository() {
        return new PetJdbcRepository();
    }

    @Bean
    public AgendamentoRepository agendamentoRepository() {
        return new AgendamentoJdbcRepository();
    }

    @Bean
    public VacinaRepository vacinaRepository() {
        return new VacinaJdbcRepository();
    }
}