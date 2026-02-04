package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
// IMPORTANTE: Aqui você deve importar sua classe de modelo e configuração
// Se elas estiverem em outro pacote, o IntelliJ vai sugerir o import (Alt+Enter)
import model.Dentista;
import config.ConfiguracaoJDBC;

public class DentistaDaoH2 implements IDao<Dentista> {

    private final ConfiguracaoJDBC configuracaoJDBC;
    private final static Logger logger = Logger.getLogger(DentistaDaoH2.class.getName());

    public DentistaDaoH2(ConfiguracaoJDBC configuracaoJDBC) {
        this.configuracaoJDBC = configuracaoJDBC;
    }

    @Override
    public Dentista salvar(Dentista dentista) {
        logger.info("Registrando dentista: " + dentista.getNome());
        Connection connection = configuracaoJDBC.getConnection();
        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO dentistas (nome, sobrenome, matricula) VALUES (?, ?, ?)";
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, dentista.getNome());
            stmt.setString(2, dentista.getSobrenome());
            stmt.setString(3, dentista.getMatricula());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if(keys.next()) {
                dentista.setId(keys.getInt(1));
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
        PreparedStatement stmt = null;
        List<Dentista> dentistas = new ArrayList<>();

        try {
            String sql = "SELECT * FROM dentistas";
            stmt = connection.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();

            while (result.next()) {
                Integer id = result.getInt("id");
                String nome = result.getString("nome");
                String sobrenome = result.getString("sobrenome");
                String matricula = result.getString("matricula");
                Dentista d = new Dentista(nome, sobrenome, matricula);
                d.setId(id);

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
    public void excluir(Integer id) {
        logger.info("Excluindo dentista ID: " + id);
        Connection connection = configuracaoJDBC.getConnection();
        try {
            String sql = "DELETE FROM dentistas WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dentista buscarPorId(Integer id) {
        logger.info("Buscando dentista ID: " + id);
        Connection connection = configuracaoJDBC.getConnection();
        Dentista dentista = null;
        try {
            String sql = "SELECT * FROM dentistas WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                String nome = result.getString("nome");
                String sobrenome = result.getString("sobrenome");
                String matricula = result.getString("matricula");
                dentista = new Dentista(nome, sobrenome, matricula);
                dentista.setId(id);
            }
            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dentista;
    }
}