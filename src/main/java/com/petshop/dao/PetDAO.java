package com.petshop.dao;

import com.petshop.builder.PetBuilder;
import com.petshop.model.Pet;
import com.petshop.model.Sexo;
import com.petshop.repository.PetRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PetDAO implements PetRepository {

    // Devolve o pet já com o id gerado pelo banco
    @Override
    public Pet inserir(Pet pet) {
        String sql = "INSERT INTO pets (nome, especie, raca, peso, sexo, data_nascimento, cor, "
                + "castrado, observacoes, proxima_vacina, cliente_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preencher(ps, pet);
            ps.setInt(11, pet.getClienteId());
            ps.executeUpdate();

            int idGerado = 0;
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    idGerado = chaves.getInt(1);
                }
            }
            return pet.comId(idGerado);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir pet: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pet> listar() {
        return buscar("SELECT * FROM pets", null);
    }

    // Diferencial: pets com a vacina já vencida
    @Override
    public List<Pet> listarVacinaVencida() {
        return buscar("SELECT * FROM pets WHERE proxima_vacina IS NOT NULL AND proxima_vacina < ?",
                LocalDate.now().toString());
    }

    @Override
    public Optional<Pet> buscarPorId(int id) {
        String sql = "SELECT * FROM pets WHERE id = ?";
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pet: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void atualizar(Pet pet) {
        String sql = "UPDATE pets SET nome = ?, especie = ?, raca = ?, peso = ?, sexo = ?, "
                + "data_nascimento = ?, cor = ?, castrado = ?, observacoes = ?, "
                + "proxima_vacina = ? WHERE id = ?";
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            preencher(ps, pet);
            ps.setInt(11, pet.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pet: " + e.getMessage(), e);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM pets WHERE id = ?";
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir pet: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existePetDoCliente(int clienteId) {
        String sql = "SELECT 1 FROM pets WHERE cliente_id = ? LIMIT 1";
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar pets do cliente: " + e.getMessage(), e);
        }
    }

    // Preenche os 10 primeiros campos, iguais no INSERT e no UPDATE
    private void preencher(PreparedStatement ps, Pet pet) throws SQLException {
        ps.setString(1, pet.getNome());
        ps.setString(2, pet.getEspecie());
        ps.setString(3, pet.getRaca());
        ps.setString(4, pet.getPeso() == null ? null : pet.getPeso().toPlainString());
        ps.setString(5, pet.getSexo().name());
        ps.setString(6, texto(pet.getDataNascimento()));
        ps.setString(7, pet.getCor());
        ps.setInt(8, pet.isCastrado() ? 1 : 0);
        ps.setString(9, pet.getObservacoes());
        ps.setString(10, texto(pet.getProximaVacina()));
    }

    private String texto(LocalDate data) {
        return data == null ? null : data.toString();
    }

    private List<Pet> buscar(String sql, String parametro) {
        List<Pet> lista = new ArrayList<>();
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (parametro != null) {
                ps.setString(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pets: " + e.getMessage(), e);
        }
        return lista;
    }

    private Pet mapear(ResultSet rs) throws SQLException {
        String peso = rs.getString("peso");
        String nascimento = rs.getString("data_nascimento");
        String vacina = rs.getString("proxima_vacina");
        return new PetBuilder()
                .id(rs.getInt("id"))
                .nome(rs.getString("nome"))
                .especie(rs.getString("especie"))
                .raca(rs.getString("raca"))
                .peso(peso == null ? null : new BigDecimal(peso))
                .sexo(Sexo.valueOf(rs.getString("sexo")))
                .dataNascimento(nascimento == null ? null : LocalDate.parse(nascimento))
                .cor(rs.getString("cor"))
                .castrado(rs.getInt("castrado") == 1)
                .observacoes(rs.getString("observacoes"))
                .proximaVacina(vacina == null ? null : LocalDate.parse(vacina))
                .clienteId(rs.getInt("cliente_id"))
                .build();
    }
}