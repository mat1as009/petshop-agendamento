package com.petshop.api;

import com.petshop.dao.AgendamentoDAO;
import com.petshop.dao.ClienteDAO;
import com.petshop.dao.PetDAO;
import com.petshop.dao.VacinaDAO;
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
        return new ClienteDAO();
    }

    @Bean
    public PetRepository petRepository() {
        return new PetDAO();
    }

    @Bean
    public AgendamentoRepository agendamentoRepository() {
        return new AgendamentoDAO();
    }

    @Bean
    public VacinaRepository vacinaRepository() {
        return new VacinaDAO();
    }
}