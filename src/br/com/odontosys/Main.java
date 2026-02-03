package br.com.odontosys;

import br.com.odontosys.domain.Consulta;
import br.com.odontosys.domain.Dentista;
import br.com.odontosys.domain.Paciente;
import br.com.odontosys.domain.enums.StatusConsulta;
import br.com.odontosys.repository.DentistaRepository;
import br.com.odontosys.repository.PacienteRepository;
import br.com.odontosys.service.ConsultaService;

import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ODONTOSYS: Teste de Regras de Negócio ===");

        ConsultaService consultaService = new ConsultaService();
        PacienteRepository pacienteRepo = new PacienteRepository();
        DentistaRepository dentistaRepo = new DentistaRepository();

        System.out.println("\n-> Verificando dados iniciais...");


        Paciente p = pacienteRepo.buscarPorId(1L); // Tenta achar o ID 1
        if (p == null) {
            System.out.println("Paciente ID 1 não encontrado. Criando novo...");
            p = new Paciente(null, "João da Silva", LocalDate.of(1990, 1, 15), "11-99999-9999", "joao@email.com");
            pacienteRepo.salvar(p);
        } else {
            System.out.println("Paciente ID 1 encontrado: " + p.getNomeCompleto());
        }

        Dentista d = dentistaRepo.buscarPorId(1L);
        if (d == null) {
            System.out.println("Dentista ID 1 não encontrado. Criando novo...");
            d = new Dentista(null, "Dra. Marcia", "Endodontia", "CRO-RJ-555", "21-8888-8888");
            dentistaRepo.salvar(d);
        } else {
            System.out.println("Dentista ID 1 encontrado: " + d.getNome());
        }

        System.out.println("\n1. Tentando agendar para 10/10/2026 às 10:00...");
        Consulta c1 = new Consulta();
        c1.setData(LocalDate.of(2026, 10, 10));
        c1.setHorario(LocalTime.of(10, 0));
        c1.setStatus(StatusConsulta.AGENDADA);
        c1.setPaciente(p);
        c1.setDentista(d);

        try {
            consultaService.agendarConsulta(c1);
            System.out.println("Sucesso: Primeira consulta marcada!");
        } catch (Exception e) {
            System.out.println("Aviso: " + e.getMessage());
        }

        System.out.println("\n2. Tentando agendar OUTRO agendamento para o MESMO horário...");

        Consulta c2 = new Consulta();
        c2.setData(LocalDate.of(2026, 10, 10));
        c2.setHorario(LocalTime.of(10, 0));
        c2.setStatus(StatusConsulta.AGENDADA);
        c2.setPaciente(p);
        c2.setDentista(d);

        try {
            consultaService.agendarConsulta(c2);
            System.out.println("ERRO: O sistema permitiu duplicidade! Algo está errado.");
        } catch (Exception e) {
            System.err.println("BLOQUEADO COM SUCESSO: " + e.getMessage());
        }
    }
}