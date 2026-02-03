package br.com.odontosys;

import br.com.odontosys.domain.Consulta;
import br.com.odontosys.domain.Dentista;
import br.com.odontosys.domain.Paciente;
import br.com.odontosys.domain.Tratamento;
import br.com.odontosys.domain.enums.StatusConsulta;
import br.com.odontosys.repository.ConsultaRepository;
import br.com.odontosys.repository.DentistaRepository;
import br.com.odontosys.repository.PacienteRepository;
import br.com.odontosys.repository.TratamentoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== ODONTOSYS: Teste de Transações (Commit/Rollback) ===");

        PacienteRepository pacienteRepo = new PacienteRepository();
        DentistaRepository dentistaRepo = new DentistaRepository();
        ConsultaRepository consultaRepo = new ConsultaRepository();
        TratamentoRepository tratamentoRepo = new TratamentoRepository();

        Paciente p = pacienteRepo.buscarPorId(1L);
        Dentista d = dentistaRepo.buscarPorId(1L);

        Tratamento t1 = tratamentoRepo.buscarPorId(1L);
        Tratamento t2 = tratamentoRepo.buscarPorId(2L);

        if (p == null || d == null || t1 == null || t2 == null) {
            System.out.println("Erro: Rode os exemplos anteriores para popular o banco primeiro!");
            return;
        }

        Consulta consultaTeste = new Consulta();
        consultaTeste.setData(LocalDate.now().plusDays(1));
        consultaTeste.setHorario(LocalTime.of(9, 0));
        consultaTeste.setStatus(StatusConsulta.AGENDADA);
        consultaTeste.setPaciente(p);
        consultaTeste.setDentista(d);
        consultaRepo.agendar(consultaTeste);

        System.out.println("Nova Consulta Criada ID: " + consultaTeste.getId());

        System.out.println("\nTentando adicionar 3 tratamentos (2 válidos e 1 inválido)...");
        System.out.println("IDs: " + t1.getId() + ", " + t2.getId() + ", 999");

        try {

            List<Long> listaTratamentos = Arrays.asList(t1.getId(), t2.getId(), 999L);

            tratamentoRepo.vincularTratamentosEmLote(consultaTeste.getId(), listaTratamentos);

            System.out.println("SUCESSO! (Isso não deve aparecer)");

        } catch (Exception e) {
            System.out.println("\n>>> Ocorreu um erro esperado: " + e.getMessage());
            System.out.println(">>> O Rollback deve ter entrado em ação.");
        }

        System.out.println("\n--- Verificando o Banco de Dados ---");
        List<Tratamento> salvos = tratamentoRepo.listarPorConsulta(consultaTeste.getId());

        if (salvos.isEmpty()) {
            System.out.println("RESULTADO: SUCESSO! A lista está vazia.");
            System.out.println("Explicação: O erro no item 3 cancelou a gravação dos itens 1 e 2.");
        } else {
            System.out.println("RESULTADO: FALHA! Existem itens salvos parcialmente.");
            for (Tratamento t : salvos) {
                System.out.println("- " + t.getDescricao());
            }
        }
    }
}