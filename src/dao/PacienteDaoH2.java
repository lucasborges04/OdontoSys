package dao;

import br.com.odontosys.domain.Paciente;
import config.ConfiguracaoJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacienteDaoH2 implements IDao<Paciente> {

    private final ConfiguracaoJDBC configuracaoJDBC;
    private final static Logger logger = Logger.getLogger(PacienteDaoH2.class.getName());

    public PacienteDaoH2(ConfiguracaoJDBC configuracaoJDBC) {
        this.configuracaoJDBC = configuracaoJDBC;
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS pacientes (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nome_completo VARCHAR(100) NOT NULL, " +
                "data_nascimento VARCHAR(50) NOT NULL, " +
                "telefone VARCHAR(20), " +
                "email VARCHAR(100) UNIQUE)";

        try (Connection connection = configuracaoJDBC.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao se conectar no banco de dados", e);
        }
    }

    @Override
    public Paciente salvar(Paciente paciente) {
        logger.info("Registrando paciente: " + paciente.getNomeCompleto());
        Connection connection = configuracaoJDBC.getConnection();
        String sql = "INSERT INTO pacientes (nome_completo, data_nascimento, telefone, email) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, paciente.getNomeCompleto());
            stmt.setString(2, String.valueOf(java.sql.Date.valueOf(paciente.getDataNascimento())));
            stmt.setString(3, paciente.getTelefone());
            stmt.setString(4, paciente.getEmail());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if(keys.next()) paciente.setId(keys.getLong(1));

            stmt.close();
            connection.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao salvar o paciente no banco de dados", e);
        }
        return paciente;
    }

    @Override
    public List<Paciente> buscarTodos() {
        logger.info("Buscando todos os pacientes...");
        Connection connection = configuracaoJDBC.getConnection();
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";

        try {
            Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(sql);

            while (result.next()) {
                Paciente p = new Paciente(
                        result.getLong("id"),
                        result.getString("nome_completo"),
                        LocalDate.parse(result.getString("data_nascimento")),
                        result.getString("telefone"),
                        result.getString("email")
                );
                pacientes.add(p);
            }
            stmt.close();
            connection.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar pacientes no banco de dados", e);
        }
        return pacientes;
    }

    @Override public void excluir(Long id) {}
    @Override public Paciente buscarPorId(Long id) { return null; }
}