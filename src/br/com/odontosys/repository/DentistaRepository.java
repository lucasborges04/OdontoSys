package br.com.odontosys.repository;

import br.com.odontosys.domain.Dentista;
import br.com.odontosys.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DentistaRepository {

    // 1. SALVAR (INSERT)
    public void salvar(Dentista dentista) {
        String sql = "INSERT INTO dentista (nome, especialidade, cro, telefone) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, dentista.getNome());
            stmt.setString(2, dentista.getEspecialidade());
            stmt.setString(3, dentista.getCro());
            stmt.setString(4, dentista.getTelefone());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dentista.setId(generatedKeys.getLong(1));
                }
            }
            System.out.println("Dentista salvo com sucesso! ID: " + dentista.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar dentista", e);
        }
    }

    public Dentista buscarPorId(Long id) {
        String sql = "SELECT * FROM dentista WHERE id = ?";
        Dentista dentista = null;

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dentista = mapearResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar dentista", e);
        }
        return dentista;
    }

    public List<Dentista> listarTodos() {
        String sql = "SELECT * FROM dentista";
        List<Dentista> dentistas = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                dentistas.add(mapearResultSet(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar dentistas", e);
        }
        return dentistas;
    }

    public void atualizar(Dentista dentista) {
        String sql = "UPDATE dentista SET nome=?, especialidade=?, cro=?, telefone=? WHERE id=?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dentista.getNome());
            stmt.setString(2, dentista.getEspecialidade());
            stmt.setString(3, dentista.getCro());
            stmt.setString(4, dentista.getTelefone());
            stmt.setLong(5, dentista.getId());

            stmt.executeUpdate();
            System.out.println("Dentista atualizado com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar dentista", e);
        }
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM dentista WHERE id=?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Dentista removido com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar dentista", e);
        }
    }

    private Dentista mapearResultSet(ResultSet rs) throws SQLException {
        Dentista d = new Dentista();
        d.setId(rs.getLong("id"));
        d.setNome(rs.getString("nome"));
        d.setEspecialidade(rs.getString("especialidade"));
        d.setCro(rs.getString("cro"));
        d.setTelefone(rs.getString("telefone"));
        return d;
    }
}