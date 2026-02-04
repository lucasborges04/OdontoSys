package br.com.odontosys;

import config.ConfiguracaoJDBC;
import java.sql.Connection;
import java.sql.Statement;

public class SetupBanco {
    public static void main(String[] args) {
        ConfiguracaoJDBC config = new ConfiguracaoJDBC();

        try (Connection conn = config.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS dentistas (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "nome VARCHAR(255), " +
                    "especialidade VARCHAR(255), " +
                    "cro VARCHAR(50), " +
                    "telefone VARCHAR(50))";

            stmt.execute(sql);
            System.out.println("Tabela 'dentistas' criada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}