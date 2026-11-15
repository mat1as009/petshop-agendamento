package com.petshop.dao;

import com.petshop.model.Agendamento;
import com.petshop.model.Servico;
import com.petshop.model.StatusAgendamento;
import com.petshop.repository.AgendamentoRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgendamentoDAO implements AgendamentoRepository {

    // Devolve o agendamento já com o id gerado pelo banco
    @Override
    public Agendamento inserir(Agendamento a) {
        String sql = "INSERT INTO agendamentos (pet_id, servico, data, hora, valor, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getPetId());
            ps.setString(2, a.getServico().name());
            ps.setString(3, a.getData().toString());
            ps.setString(4, a.getHora().toString());
            ps.setString(5, a.getValor().toPlainString());
            ps.setString(6, a.getStatus().name());
            ps.executeUpdate();

            int idGerado = 0;
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    idGerado = chaves.getInt(1);
                }
            }
            return new Agendamento(idGerado, a.getPetId(), a.getServico(), a.getData(),
                    a.getHora(), a.getValor(), a.getStatus());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir agendamento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Agendamento> listar() {
        List<Agendamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos ORDER BY data, hora";
        try (Connection c = Conexao.getConexao();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar agendamentos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Optional<Agendamento> buscarPorId(int id) {
        String sql = "SELECT * FROM agendamentos WHERE id = ?";
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar agendamento: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void atualizarStatus(int id, StatusAgendamento status) {
        String sql = "UPDATE agendamentos SET status = ? WHERE id = ?";
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar agendamento: " + e.getMessage(), e);
        }
    }

    // Horários já tomados em um dia (só os que ainda estão "AGENDADO")
    @Override
    public List<LocalTime> horariosOcupados(LocalDate data) {
        List<LocalTime> horarios = new ArrayList<>();
        String sql = "SELECT hora FROM agendamentos WHERE data = ? AND status = 'AGENDADO'";
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, data.toString());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    horarios.add(LocalTime.parse(rs.getString("hora")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar horários: " + e.getMessage(), e);
        }
        return horarios;
    }

    private Agendamento mapear(ResultSet rs) throws SQLException {
        return new Agendamento(
                rs.getInt("id"),
                rs.getInt("pet_id"),
                Servico.valueOf(rs.getString("servico")),
                LocalDate.parse(rs.getString("data")),
                LocalTime.parse(rs.getString("hora")),
                new BigDecimal(rs.getString("valor")),
                StatusAgendamento.valueOf(rs.getString("status")));
    }
}