package com.petshop.api;

import com.petshop.dao.Conexao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetshopApiApplication {

    public static void main(String[] args) {
        Conexao.criarTabelas();
        SpringApplication.run(PetshopApiApplication.class, args);
    }
}