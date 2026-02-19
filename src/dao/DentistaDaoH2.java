package dao;

import br.com.odontosys.domain.Dentista;
import config.ConfiguracaoJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DentistaDaoH2 implements IDao<Dentista> {

    private final ConfiguracaoJDBC configuracaoJDBC;
    private final static Logger logger = Logger.getLogger(DentistaDaoH2.class.getName());

    public DentistaDaoH2(ConfiguracaoJDBC configuracaoJDBC) {
        this.configuracaoJDBC = configuracaoJDBC;
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS dentistas (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "nome VARCHAR(100) NOT NULL, " +
                "especialidade VARCHAR(100) NOT NULL, " +
                "telefone VARCHAR(20), " +
                "cro VARCHAR(20) NOT NULL UNIQUE)";

        try (Connection connection = configuracaoJDBC.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao se conectar no banco de dados", e);
        }
    }

    @Override
    public Dentista salvar(Dentista dentista) {
        logger.info("Registrando dentista: " + dentista.getNome());
        Connection connection = configuracaoJDBC.getConnection();

        String sql = "INSERT INTO dentistas (nome, especialidade, cro, telefone) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, dentista.getNome());
            stmt.setString(2, dentista.getEspecialidade());
            stmt.setString(3, dentista.getCro());
            stmt.setString(4, dentista.getTelefone());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if(keys.next()) {
                dentista.setId(keys.getLong(1));
            }

            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dentista;
    }

    @Override
    public List<Dentista> buscarTodos() {
        logger.info("Buscando todos os dentistas...");
        Connection connection = configuracaoJDBC.getConnection();
        List<Dentista> dentistas = new ArrayList<>();

        String sql = "SELECT * FROM dentistas";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Long id = result.getLong("id");
                String nome = result.getString("nome");
                String especialidade = result.getString("especialidade");
                String cro = result.getString("cro");
                String telefone = result.getString("telefone");

                Dentista d = new Dentista(id, nome, especialidade, cro, telefone);
                dentistas.add(d);
            }
            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dentistas;
    }

    @Override
    public void excluir(Long id) {
        logger.info("Excluindo dentista ID: " + id);
        Connection connection = configuracaoJDBC.getConnection();
        try {
            String sql = "DELETE FROM dentistas WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.execute();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dentista buscarPorId(Long id) {
        logger.info("Buscando dentista ID: " + id);
        Connection connection = configuracaoJDBC.getConnection();
        Dentista dentista = null;
        try {
            String sql = "SELECT * FROM dentistas WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, id);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                String nome = result.getString("nome");
                String especialidade = result.getString("especialidade");
                String cro = result.getString("cro");
                String telefone = result.getString("telefone");

                dentista = new Dentista(id, nome, especialidade, cro, telefone);
            }
            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dentista;
    }
}