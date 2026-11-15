package com.petshop.dao;

import com.petshop.model.Vacina;
import com.petshop.repository.VacinaRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VacinaDAO implements VacinaRepository {

    // Devolve a vacina já com o id gerado pelo banco
    @Override
    public Vacina inserir(Vacina vacina) {
        String sql = "INSERT INTO vacinas (pet_id, data_aplicacao) VALUES (?, ?)";
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, vacina.getPetId());
            ps.setString(2, vacina.getDataAplicacao().toString());
            ps.executeUpdate();

            int idGerado = 0;
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    idGerado = chaves.getInt(1);
                }
            }
            return new Vacina(idGerado, vacina.getPetId(), vacina.getDataAplicacao());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir vacina: " + e.getMessage(), e);
        }
    }

    // Da mais recente para a mais antiga
    @Override
    public List<Vacina> listarPorPet(int petId) {
        List<Vacina> lista = new ArrayList<>();
        String sql = "SELECT id, pet_id, data_aplicacao FROM vacinas "
                + "WHERE pet_id = ? ORDER BY data_aplicacao DESC";
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, petId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Vacina(
                            rs.getInt("id"),
                            rs.getInt("pet_id"),
                            LocalDate.parse(rs.getString("data_aplicacao"))));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vacinas: " + e.getMessage(), e);
        }
        return lista;
    }
}