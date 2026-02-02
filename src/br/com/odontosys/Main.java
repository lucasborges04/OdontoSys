package br.com.odontosys;

import br.com.odontosys.domain.Paciente;
import br.com.odontosys.repository.PacienteRepository;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Iniciando Testes de Paciente ===");

        PacienteRepository pacienteRepo = new PacienteRepository();

        Paciente novoPaciente = new Paciente(
                null,
                "Ana Paula Silva",
                LocalDate.of(1995, 5, 20),
                "1199999-8888",
                "ana.paula@email.com"
        );

        System.out.println("\n1. Salvando paciente...");
        pacienteRepo.salvar(novoPaciente);

        System.out.println("\n2. Buscando paciente ID " + novoPaciente.getId() + "...");
        Paciente recuperado = pacienteRepo.buscarPorId(novoPaciente.getId());
        if (recuperado != null) {
            System.out.println("Nome: " + recuperado.getNomeCompleto());
            System.out.println("Nascimento: " + recuperado.getDataNascimento());
        }

        System.out.println("\n3. Alterando telefone...");
        recuperado.setTelefone("1100000-0000");
        pacienteRepo.atualizar(recuperado);

        System.out.println("\n4. Lista Geral de Pacientes:");
        List<Paciente> lista = pacienteRepo.listarTodos();
        for (Paciente p : lista) {
            System.out.println("- " + p.getId() + " | " + p.getNomeCompleto() + " | " + p.getDataNascimento() + " | " + p.getTelefone());
        }

        pacienteRepo.deletar(novoPaciente.getId());
    }
}