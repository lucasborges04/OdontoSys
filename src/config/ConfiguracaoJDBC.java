package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfiguracaoJDBC {

    private final String jdbcDriver;
    private final String dbUrl;
    private final String nomeUsuario;
    private final String senha;

    private static final Logger logger = Logger.getLogger(ConfiguracaoJDBC.class.getName());

    public ConfiguracaoJDBC() {
        this.jdbcDriver = "org.h2.Driver";
        this.dbUrl = "jdbc:mysql://localhost:3306/odontosys?useTimezone=true&serverTimezone=UTC";
        this.nomeUsuario = "root";
        this.senha = "admin1234";
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(dbUrl, nomeUsuario, senha);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao conectar no banco de dados", e);
        }
        return connection;
    }
}