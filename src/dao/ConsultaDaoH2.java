package dao;

import br.com.odontosys.domain.Consulta;
import br.com.odontosys.domain.Dentista;
import br.com.odontosys.domain.Paciente;
import br.com.odontosys.domain.enums.StatusConsulta;
import config.ConfiguracaoJDBC;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultaDaoH2 implements IDao<Consulta> {

    private final ConfiguracaoJDBC configuracaoJDBC;
    private final static Logger logger = Logger.getLogger(ConsultaDaoH2.class.getName());

    public ConsultaDaoH2(ConfiguracaoJDBC configuracaoJDBC) {
        this.configuracaoJDBC = configuracaoJDBC;
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS consultas (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "data DATE NOT NULL, " +
                "horario TIME NOT NULL, " +
                "status VARCHAR(50), NOT NULL COMMENT 'Enum: AGENDADA, REALIZADA, CANCELADA, NAO_COMPARECEU', " +
                "paciente_id BIGINT, " +
                "dentista_id BIGINT, " +
                "FOREIGN KEY (dentista_id) REFERENCES dentistas(id), " +
                "FOREIGN KEY (paciente_id) REFERENCES pacientes(id))";

        try (Connection connection = configuracaoJDBC.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Falha ao criar a tabela de consultas no banco de dados", e);
        }
    }

    @Override
    public Consulta salvar(Consulta consulta) {
        logger.info("Agendando consulta...");
        Connection connection = configuracaoJDBC.getConnection();

        String sql = "INSERT INTO consultas (data, horario, status, paciente_id, dentista_id) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setDate(1, Date.valueOf(consulta.getData()));

            stmt.setTime(2, Time.valueOf(consulta.getHorario()));

            stmt.setString(3, consulta.getStatus().toString());

            stmt.setLong(4, consulta.getDentista().getId());
            stmt.setLong(5, consulta.getPaciente().getId());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                consulta.setId(keys.getLong(1));
            }

            stmt.close();
            connection.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Falha ao agendar (salvar) a consulta no banco de dados", e);
        }
        return consulta;
    }

    @Override
    public List<Consulta> buscarTodos() {
        logger.info("Buscando todas as consultas...");
        Connection connection = configuracaoJDBC.getConnection();
        List<Consulta> consultas = new ArrayList<>();

        String sql = "SELECT c.id, c.data, c.horario, c.status, " +
                "p.id AS p_id, p.nome_completo AS p_nome, p.data_nascimento AS p_dataNascimento, p.telefone, p.email" +
                "d.id AS d_id, d.nome AS d_nome, d.especialidade AS d_especialidade, d.cro, d.telefone" +
                "FROM consultas c " +
                "INNER JOIN pacientes p ON c.paciente_id = p.id" +
                "INNER JOIN dentistas d ON c.dentista_id = d.id";

        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(sql);

            while (result.next()) {
                Dentista dentista = new Dentista();
                dentista.setId(result.getLong("d_id"));
                dentista.setNome(result.getString("d_nome"));
                dentista.setEspecialidade(result.getString("d_especialidade"));
                dentista.setCro(result.getString("cro"));
                dentista.setTelefone(result.getString("telefone"));

                Paciente paciente = new Paciente();
                paciente.setId(result.getLong("p_id"));
                paciente.setNomeCompleto(result.getString("p_nome"));
                paciente.setDataNascimento(LocalDate.parse(result.getString("p_dataNascimento")));
                paciente.setTelefone(result.getString("telefone"));
                paciente.setEmail(result.getString("email"));

                Consulta c = new Consulta(
                        result.getLong("id"),
                        result.getDate("data").toLocalDate(),
                        result.getTime("horario").toLocalTime(),
                        StatusConsulta.valueOf(result.getString("status")),
                        paciente,
                        dentista
                );

                consultas.add(c);
            }
            stmt.close();
            connection.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Falha ao buscar a lista de consultas e seus relacionamentos", e);
        }
        return consultas;
    }

    @Override
    public void excluir(Long id) {
        Connection connection = configuracaoJDBC.getConnection();
        String sql = "DELETE FROM consultas WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.execute();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Falha ao excluir a consulta de ID: " + id, e);
        }
    }

    @Override
    public Consulta buscarPorId(Long id) {
        return null;
    }
}