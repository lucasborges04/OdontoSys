package br.com.odontosys.service;

import br.com.odontosys.domain.Paciente;
import config.ConfiguracaoJDBC;
import dao.PacienteDaoH2;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PacienteServiceTest {

    @Test
    void naoDevePermitirEmailDuplicado() {
        PacienteDaoH2 dao = new PacienteDaoH2(new ConfiguracaoJDBC());
        PacienteService service = new PacienteService(dao);

        String emailTeste = "teste." + System.currentTimeMillis() + "@email.com";
        LocalDate dataNasc = LocalDate.of(1990, 1, 1);

        Paciente p1 = new Paciente(null, "Paciente Original", dataNasc, "11999999999", emailTeste);
        service.salvar(p1);

        Paciente p2 = new Paciente(null, "Paciente Clone", dataNasc, "11888888888", emailTeste);

        assertThrows(RuntimeException.class, () -> {
            service.salvar(p2);
        });
    }
}