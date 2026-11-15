package com.petshop.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
    private static final String URL = "jdbc:sqlite:petshop.db";

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void criarTabelas() {
        try (Connection c = getConexao(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS clientes ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nome TEXT NOT NULL, "
                    + "cpf TEXT NOT NULL, "
                    + "data_nascimento TEXT, "
                    + "telefone TEXT, "
                    + "telefone_secundario TEXT, "
                    + "email TEXT, "
                    + "observacoes TEXT, "
                    + "data_cadastro TEXT NOT NULL)");
            // 1:1 com clientes: o id do cliente é a chave primária do endereço
            s.execute("CREATE TABLE IF NOT EXISTS enderecos ("
                    + "cliente_id INTEGER PRIMARY KEY, "
                    + "cep TEXT NOT NULL, "
                    + "logradouro TEXT NOT NULL, "
                    + "numero TEXT NOT NULL, "
                    + "complemento TEXT, "
                    + "bairro TEXT, "
                    + "cidade TEXT NOT NULL, "
                    + "estado TEXT NOT NULL, "
                    + "FOREIGN KEY (cliente_id) REFERENCES clientes(id))");
            s.execute("CREATE TABLE IF NOT EXISTS pets ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "nome TEXT NOT NULL, "
                    + "especie TEXT, "
                    + "raca TEXT, "
                    + "peso TEXT, "
                    + "sexo TEXT NOT NULL, "
                    + "data_nascimento TEXT, "
                    + "cor TEXT, "
                    + "castrado INTEGER NOT NULL DEFAULT 0, "
                    + "observacoes TEXT, "
                    + "proxima_vacina TEXT, "
                    + "cliente_id INTEGER NOT NULL, "
                    + "FOREIGN KEY (cliente_id) REFERENCES clientes(id))");
            s.execute("CREATE TABLE IF NOT EXISTS agendamentos ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "pet_id INTEGER NOT NULL, "
                    + "servico TEXT NOT NULL, "
                    + "data TEXT NOT NULL, "
                    + "hora TEXT NOT NULL, "
                    + "valor TEXT NOT NULL, "
                    + "status TEXT NOT NULL, "
                    + "FOREIGN KEY (pet_id) REFERENCES pets(id))");
            // Só um agendamento "AGENDADO" por data e hora
            s.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_horario_unico "
                    + "ON agendamentos(data, hora) WHERE status = 'AGENDADO'");
            s.execute("CREATE TABLE IF NOT EXISTS vacinas ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "pet_id INTEGER NOT NULL, "
                    + "data_aplicacao TEXT NOT NULL, "
                    + "FOREIGN KEY (pet_id) REFERENCES pets(id))");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabelas: " + e.getMessage(), e);
        }
    }
}