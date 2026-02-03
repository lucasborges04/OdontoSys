package br.com.odontosys.repository;

import br.com.odontosys.domain.Tratamento;
import br.com.odontosys.util.ConexaoDB;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TratamentoRepository {

    private static final Logger logger = Logger.getLogger(TratamentoRepository.class.getName());

    public void salvar(Tratamento tratamento) {
        String sql = "INSERT INTO tratamento (descricao, valor) VALUES (?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, tratamento.getDescricao());
            stmt.setBigDecimal(2, tratamento.getValor());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tratamento.setId(generatedKeys.getLong(1));
                }
            }
            System.out.println("Tratamento criado: " + tratamento.getDescricao());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar tratamento", e);
        }
    }

    public Tratamento buscarPorId(Long id) {
        String sql = "SELECT * FROM tratamento WHERE id = ?";
        Tratamento t = null;

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    t = new Tratamento();
                    t.setId(rs.getLong("id"));
                    t.setDescricao(rs.getString("descricao"));
                    t.setValor(rs.getBigDecimal("valor"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar tratamento", e);
        }
        return t;
    }


    public void adicionarTratamentoNaConsulta(Long idConsulta, Long idTratamento) {
        String sql = "INSERT INTO consulta_tratamento (consulta_id, tratamento_id) VALUES (?, ?)";

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idConsulta);
            stmt.setLong(2, idTratamento);

            stmt.executeUpdate();
            System.out.println("Vinculo criado: Consulta " + idConsulta + " recebeu Tratamento " + idTratamento);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao vincular tratamento na consulta", e);
        }
    }


    public List<Tratamento> listarPorConsulta(Long idConsulta) {
        String sql = """
                SELECT t.* FROM tratamento t
                INNER JOIN consulta_tratamento ct ON t.id = ct.tratamento_id
                WHERE ct.consulta_id = ?
                """;

        List<Tratamento> lista = new ArrayList<>();

        try (Connection conn = ConexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idConsulta);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tratamento t = new Tratamento();
                    t.setId(rs.getLong("id"));
                    t.setDescricao(rs.getString("descricao"));
                    t.setValor(rs.getBigDecimal("valor"));
                    lista.add(t);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tratamentos da consulta", e);
        }
        return lista;
    }

    public void vincularTratamentosEmLote(Long idConsulta, List<Long> idsTratamentos) {
        String sql = "INSERT INTO consulta_tratamento (consulta_id, tratamento_id) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConexaoDB.getConnection();

            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(sql);

            for (Long idTratamento : idsTratamentos) {
                if (idTratamento == 999) {
                    throw new RuntimeException("Erro Simulado: Tratamento 999 é inválido!");
                }

                stmt.setLong(1, idConsulta);
                stmt.setLong(2, idTratamento);
                stmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Transação concluída! Todos os " + idsTratamentos.size() + " tratamentos foram salvos.");

        } catch (Exception e) {

            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("ERRO DETECTADO! Realizando Rollback (desfazendo alterações)...");
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Ocorreu um erro no banco de dados", ex);
            }
            throw new RuntimeException("Falha na transação em lote", e);

        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Ocorreu um erro no banco de dados", e);
            }
        }
    }
}