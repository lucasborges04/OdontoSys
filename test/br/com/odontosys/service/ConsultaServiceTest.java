package br.com.odontosys.service;

import br.com.odontosys.domain.Consulta;
import br.com.odontosys.domain.Dentista;
import br.com.odontosys.domain.Paciente;
import br.com.odontosys.domain.enums.StatusConsulta;
import config.ConfiguracaoJDBC;
import dao.ConsultaDaoH2;
import dao.DentistaDaoH2;
import dao.IDao;
import dao.PacienteDaoH2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ConsultaServiceTest {

    private ConsultaService consultaService;
    private DentistaService dentistaService;
    private PacienteService pacienteService;

    @BeforeEach
    void setUp() {
        ConfiguracaoJDBC config = new ConfiguracaoJDBC();

        try (Connection connection = new ConfiguracaoJDBC().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS consultas");
            stmt.execute("DROP TABLE IF EXISTS pacientes");
            stmt.execute("DROP TABLE IF EXISTS dentistas");
        } catch (Exception e) {
            e.printStackTrace();
        }

        pacienteService = new PacienteService(new PacienteDaoH2(config));
        dentistaService = new DentistaService(new DentistaDaoH2(config));
        consultaService = new ConsultaService(new ConsultaDaoH2(config));
    }

    @Test
    void deveAgendarConsultaComSucesso() {
        Dentista dentista = criarDentistaExemplo("Dr. Teste Sucesso","CRO-12345");
        dentista = dentistaService.registrarDentista(dentista);

        Paciente paciente = criarPacienteExemplo("Paciente Sucesso", "email.sucesso" + System.currentTimeMillis() + "@test.com");
        paciente = pacienteService.salvar(paciente);

        Consulta consulta = new Consulta(null, LocalDate.now().plusDays(1), LocalTime.of(14, 0), StatusConsulta.AGENDADA, paciente, dentista);

        Consulta consultaSalva = consultaService.agendar(consulta);

        assertNotNull(consultaSalva.getId());
        System.out.println("Consulta agendada com ID: " + consultaSalva.getId());
    }

    @Test
    void naoDevePermitirHorarioDuplicadoParaMesmoDentista() {
        Dentista dentista = criarDentistaExemplo("Dr. Conflito", "CRO-11111");
        dentista = dentistaService.registrarDentista(dentista);

        Paciente p1 = criarPacienteExemplo("Paciente Um", "p1." + System.currentTimeMillis() + "@test.com");
        pacienteService.salvar(p1);

        Paciente p2 = criarPacienteExemplo("Paciente Dois", "p2." + System.currentTimeMillis() + "@test.com");
        pacienteService.salvar(p2);

        LocalDate data = LocalDate.now().plusDays(5);
        LocalTime hora = LocalTime.of(10, 0);

        Consulta c1 = new Consulta(null, data, hora, StatusConsulta.AGENDADA, p1, dentista);
        consultaService.agendar(c1);

        Consulta c2 = new Consulta(null, data, hora, StatusConsulta.AGENDADA, p2, dentista);

        assertThrows(RuntimeException.class, () -> {
            consultaService.agendar(c2);
        });
        System.out.println("Teste de conflito passou: O sistema bloqueou o segundo agendamento!");
    }


    private Dentista criarDentistaExemplo(String nome, String cro) {
        return new Dentista(null, nome, "Geral", cro, "0000");
    }

    private Paciente criarPacienteExemplo(String nome, String email) {
        return new Paciente(null, nome, LocalDate.of(1990, 1, 1), "11999999999", email);
    }
}