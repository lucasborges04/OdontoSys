package br.com.odontosys.repository;

import br.com.odontosys.domain.Consulta;
import br.com.odontosys.domain.Dentista;
import br.com.odontosys.domain.Paciente;
import br.com.odontosys.domain.enums.StatusConsulta;
import br.com.odontosys.util.ConexaoDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaRepository {

    public void agendar(Consulta consulta) {
        String sql = "INSERT INTO consulta (data_consulta, horario, status, paciente_id, dentista_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(consulta.getData()));

            stmt.setTime(2, Time.valueOf(consulta.getHorario()));

            stmt.setString(3, consulta.getStatus().name());

            stmt.setLong(4, consulta.getPaciente().getId());
            stmt.setLong(5, consulta.getDentista().getId());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    consulta.setId(generatedKeys.getLong(1));
                }
            }
            System.out.println("Consulta agendada com sucesso! ID: " + consulta.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao agendar consulta", e);
        }
    }

    public List<Consulta> listarTodas() {
        String sql = """
                SELECT c.*, p.nome_completo AS nome_paciente, d.nome AS nome_dentista 
                FROM consulta c
                INNER JOIN paciente p ON c.paciente_id = p.id
                INNER JOIN dentista d ON c.dentista_id = d.id
                """;

        List<Consulta> lista = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Consulta c = new Consulta();
                c.setId(rs.getLong("id"));
                c.setData(rs.getDate("data_consulta").toLocalDate());
                c.setHorario(rs.getTime("horario").toLocalTime());
                c.setStatus(StatusConsulta.valueOf(rs.getString("status")));

                Paciente p = new Paciente();
                p.setId(rs.getLong("paciente_id"));
                p.setNomeCompleto(rs.getString("nome_paciente"));
                c.setPaciente(p);

                Dentista d = new Dentista();
                d.setId(rs.getLong("dentista_id"));
                d.setNome(rs.getString("nome_dentista"));
                c.setDentista(d);

                lista.add(c);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar consultas", e);
        }
        return lista;
    }

    public void cancelar(Long id) {
        String sql = "UPDATE consulta SET status = ? WHERE id = ?";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, StatusConsulta.CANCELADA.name());
            stmt.setLong(2, id);

            stmt.executeUpdate();
            System.out.println("Consulta " + id + " cancelada com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cancelar consulta", e);
        }
    }
}