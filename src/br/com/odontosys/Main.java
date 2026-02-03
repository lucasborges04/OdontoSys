package br.com.odontosys;

import br.com.odontosys.domain.Consulta;
import br.com.odontosys.domain.Dentista;
import br.com.odontosys.domain.Paciente;
import br.com.odontosys.domain.Tratamento;
import br.com.odontosys.domain.enums.StatusConsulta;
import br.com.odontosys.repository.ConsultaRepository;
import br.com.odontosys.repository.DentistaRepository;
import br.com.odontosys.repository.PacienteRepository;
import br.com.odontosys.repository.TratamentoRepository; // Novo import

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ODONTOSYS: Fluxo Clínico Completo ===");

        PacienteRepository pacienteRepo = new PacienteRepository();
        DentistaRepository dentistaRepo = new DentistaRepository();
        ConsultaRepository consultaRepo = new ConsultaRepository();
        TratamentoRepository tratamentoRepo = new TratamentoRepository();

        Paciente p = pacienteRepo.buscarPorId(1L);
        if (p == null) {
            p = new Paciente(null, "João da Silva", LocalDate.of(1990, 1, 15), "11-99999-9999", "joao@email.com");
            pacienteRepo.salvar(p);
        }

        Dentista d = dentistaRepo.buscarPorId(1L);
        if (d == null) {
            d = new Dentista(null, "Dra. Marcia", "Endodontia", "CRO-RJ-555", "21-8888-8888");
            dentistaRepo.salvar(d);
        }

        Tratamento limpeza = tratamentoRepo.buscarPorId(1L);
        if (limpeza == null) {
            limpeza = new Tratamento(null, "Limpeza Completa", new BigDecimal("250.00"));
            tratamentoRepo.salvar(limpeza);
        }

        Tratamento canal = tratamentoRepo.buscarPorId(2L);
        if (canal == null) {
            canal = new Tratamento(null, "Tratamento de Canal", new BigDecimal("800.00"));
            tratamentoRepo.salvar(canal);
        }

        Consulta consulta = new Consulta();
        consulta.setData(LocalDate.now());
        consulta.setHorario(LocalTime.now());
        consulta.setStatus(StatusConsulta.REALIZADA);
        consulta.setPaciente(p);
        consulta.setDentista(d);

        consultaRepo.agendar(consulta);

        System.out.println("\n-> Adicionando procedimentos à consulta " + consulta.getId() + "...");

        tratamentoRepo.adicionarTratamentoNaConsulta(consulta.getId(), limpeza.getId());

        tratamentoRepo.adicionarTratamentoNaConsulta(consulta.getId(), canal.getId());

        System.out.println("\n---------------- RESUMO DA CONSULTA ----------------");
        System.out.println("Paciente: " + p.getNomeCompleto());
        System.out.println("Dentista: " + d.getNome());
        System.out.println("Procedimentos Realizados:");

        List<Tratamento> realizados = tratamentoRepo.listarPorConsulta(consulta.getId());
        BigDecimal total = BigDecimal.ZERO;

        for (Tratamento t : realizados) {
            System.out.println("- " + t.getDescricao() + ": R$ " + t.getValor());
            total = total.add(t.getValor());
        }

        System.out.println("----------------------------------------------------");
        System.out.println("TOTAL A PAGAR: R$ " + total);
        System.out.println("----------------------------------------------------");
    }
}