package br.com.odontosys.repository;

import br.com.odontosys.domain.Paciente;
import br.com.odontosys.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepository {

    public void salvar(Paciente paciente) {
        String sql = "INSERT INTO paciente (nome_completo, data_nascimento, telefone, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, paciente.getNomeCompleto());
            stmt.setDate(2, Date.valueOf(paciente.getDataNascimento()));
            stmt.setString(3, paciente.getTelefone());
            stmt.setString(4, paciente.getEmail());

            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paciente.setId(generatedKeys.getLong(1));
                }
            }
            System.out.println("Paciente salvo com ID: " + paciente.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar paciente", e);
        }
    }

    public Paciente buscarPorId(Long id) {
        String sql = "SELECT * FROM paciente WHERE id = ?";
        Paciente paciente = null;

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    paciente = mapearResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar paciente", e);
        }
        return paciente;
    }

    public List<Paciente> listarTodos() {
        String sql = "SELECT * FROM paciente";
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapearResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pacientes", e);
        }
        return pacientes;
    }

    public void atualizar(Paciente paciente) {
        String sql = "UPDATE paciente SET nome_completo=?, data_nascimento=?, telefone=?, email=? WHERE id=?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, paciente.getNomeCompleto());
            stmt.setDate(2, Date.valueOf(paciente.getDataNascimento()));
            stmt.setString(3, paciente.getTelefone());
            stmt.setString(4, paciente.getEmail());
            stmt.setLong(5, paciente.getId());

            stmt.executeUpdate();
            System.out.println("Paciente atualizado com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar paciente", e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM paciente WHERE id=?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Paciente removido com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar paciente", e);
        }
    }

    private Paciente mapearResultSet(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getLong("id"));
        p.setNomeCompleto(rs.getString("nome_completo")); // Atenção ao nome da coluna no Banco (snake_case)

        // CONVERSÃO INVERSA: java.sql.Date (Banco) -> LocalDate (Java)
        Date dataSql = rs.getDate("data_nascimento");
        if (dataSql != null) {
            p.setDataNascimento(dataSql.toLocalDate());
        }

        p.setTelefone(rs.getString("telefone"));
        p.setEmail(rs.getString("email"));

        return p;
    }
}