package com.petshop.dao;

import com.petshop.builder.ClienteBuilder;
import com.petshop.model.Cliente;
import com.petshop.model.Endereco;
import com.petshop.repository.ClienteRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteJdbcRepository implements ClienteRepository {
    private static final String SELECT_COMPLETO =
            "SELECT * FROM clientes c JOIN enderecos e ON e.cliente_id = c.id";

    // Grava o cliente e o endereço juntos (se um falhar, nenhum é gravado)
    @Override
    public Cliente inserir(Cliente cliente) {
        try (Connection c = Conexao.getConexao()) {
            c.setAutoCommit(false);
            try {
                int id = inserirCliente(c, cliente);
                inserirEndereco(c, id, cliente.getEndereco());
                c.commit();
                return cliente.comId(id);
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        try (Connection c = Conexao.getConexao();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(SELECT_COMPLETO + " ORDER BY c.id")) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Optional<Cliente> buscarPorId(int id) {
        try (Connection c = Conexao.getConexao();
             PreparedStatement ps = c.prepareStatement(SELECT_COMPLETO + " WHERE c.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public void atualizar(Cliente cliente) {
        try (Connection c = Conexao.getConexao()) {
            c.setAutoCommit(false);
            try {
                atualizarCliente(c, cliente);
                atualizarEndereco(c, cliente.getId(), cliente.getEndereco());
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    // Apaga o endereço junto com o cliente
    @Override
    public void excluir(int id) {
        try (Connection c = Conexao.getConexao()) {
            c.setAutoCommit(false);
            try {
                executarExclusao(c, "DELETE FROM enderecos WHERE cliente_id = ?", id);
                executarExclusao(c, "DELETE FROM clientes WHERE id = ?", id);
                c.commit();
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente: " + e.getMessage(), e);
        }
    }

    private int inserirCliente(Connection c, Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nome, cpf, data_nascimento, telefone, "
                + "telefone_secundario, email, observacoes, data_cadastro) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, texto(cliente.getDataNascimento()));
            ps.setString(4, cliente.getTelefone());
            ps.setString(5, cliente.getTelefoneSecundario());
            ps.setString(6, cliente.getEmail());
            ps.setString(7, cliente.getObservacoes());
            ps.setString(8, cliente.getDataCadastro().toString());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    return chaves.getInt(1);
                }
            }
        }
        throw new SQLException("O banco não devolveu o id do cliente.");
    }

    private void inserirEndereco(Connection c, int clienteId, Endereco e) throws SQLException {
        String sql = "INSERT INTO enderecos (cep, logradouro, numero, complemento, bairro, "
                + "cidade, estado, cliente_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            preencherEndereco(ps, e);
            ps.setInt(8, clienteId);
            ps.executeUpdate();
        }
    }

    private void atualizarCliente(Connection c, Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, cpf = ?, data_nascimento = ?, telefone = ?, "
                + "telefone_secundario = ?, email = ?, observacoes = ? WHERE id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCpf());
            ps.setString(3, texto(cliente.getDataNascimento()));
            ps.setString(4, cliente.getTelefone());
            ps.setString(5, cliente.getTelefoneSecundario());
            ps.setString(6, cliente.getEmail());
            ps.setString(7, cliente.getObservacoes());
            ps.setInt(8, cliente.getId());
            ps.executeUpdate();
        }
    }

    private void atualizarEndereco(Connection c, int clienteId, Endereco e) throws SQLException {
        String sql = "UPDATE enderecos SET cep = ?, logradouro = ?, numero = ?, complemento = ?, "
                + "bairro = ?, cidade = ?, estado = ? WHERE cliente_id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            preencherEndereco(ps, e);
            ps.setInt(8, clienteId);
            ps.executeUpdate();
        }
    }

    private void executarExclusao(Connection c, String sql, int id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Preenche os 7 primeiros campos, iguais no INSERT e no UPDATE
    private void preencherEndereco(PreparedStatement ps, Endereco e) throws SQLException {
        ps.setString(1, e.getCep());
        ps.setString(2, e.getLogradouro());
        ps.setString(3, e.getNumero());
        ps.setString(4, e.getComplemento());
        ps.setString(5, e.getBairro());
        ps.setString(6, e.getCidade());
        ps.setString(7, e.getEstado());
    }

    private String texto(LocalDate data) {
        return data == null ? null : data.toString();
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco(
                rs.getString("cep"),
                rs.getString("logradouro"),
                rs.getString("numero"),
                rs.getString("complemento"),
                rs.getString("bairro"),
                rs.getString("cidade"),
                rs.getString("estado"));
        String nascimento = rs.getString("data_nascimento");
        return new ClienteBuilder()
                .id(rs.getInt("id"))
                .nome(rs.getString("nome"))
                .cpf(rs.getString("cpf"))
                .dataNascimento(nascimento == null ? null : LocalDate.parse(nascimento))
                .telefone(rs.getString("telefone"))
                .telefoneSecundario(rs.getString("telefone_secundario"))
                .email(rs.getString("email"))
                .endereco(endereco)
                .observacoes(rs.getString("observacoes"))
                .dataCadastro(LocalDate.parse(rs.getString("data_cadastro")))
                .build();
    }
}