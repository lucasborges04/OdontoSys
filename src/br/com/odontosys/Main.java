package br.com.odontosys;

import br.com.odontosys.domain.Consulta;
import br.com.odontosys.domain.Dentista;
import br.com.odontosys.domain.Paciente;
import br.com.odontosys.domain.enums.StatusConsulta;
import br.com.odontosys.repository.ConsultaRepository;
import br.com.odontosys.repository.DentistaRepository;
import br.com.odontosys.repository.PacienteRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA ODONTOSYS: Agendamento ===");

        PacienteRepository pacienteRepo = new PacienteRepository();
        DentistaRepository dentistaRepo = new DentistaRepository();
        ConsultaRepository consultaRepo = new ConsultaRepository();

        System.out.println("\n-> Cadastrando Paciente e Dentista...");

        Paciente paciente = new Paciente(null, "João da Silva", LocalDate.of(1990, 1, 15), "11-99999-9999", "joao@email.com");
        pacienteRepo.salvar(paciente);

        Dentista dentista = new Dentista(null, "Dra. Marcia", "Endodontia", "CRO-RJ-555", "21-8888-8888");
        dentistaRepo.salvar(dentista);

        System.out.println("\n-> Agendando a Consulta...");
        Consulta novaConsulta = new Consulta();
        novaConsulta.setData(LocalDate.of(2025, 12, 25));
        novaConsulta.setHorario(LocalTime.of(14, 30));
        novaConsulta.setStatus(StatusConsulta.AGENDADA);

        novaConsulta.setPaciente(paciente);
        novaConsulta.setDentista(dentista);

        consultaRepo.agendar(novaConsulta);

        System.out.println("\n-> Relatório de Consultas:");
        List<Consulta> agenda = consultaRepo.listarTodas();

        for (Consulta c : agenda) {
            System.out.println("ID: " + c.getId() +
                    " | Data: " + c.getData() + " às " + c.getHorario() +
                    " | Paciente: " + c.getPaciente().getNomeCompleto() +
                    " | Dentista: " + c.getDentista().getNome() +
                    " | Status: " + c.getStatus());
        }

//         consultaRepo.cancelar(novaConsulta.getId());
    }
}