package br.com.odontosys;

import br.com.odontosys.domain.Consulta;
import br.com.odontosys.domain.Dentista;
import br.com.odontosys.domain.Paciente;
import br.com.odontosys.domain.enums.StatusConsulta;
import br.com.odontosys.service.ConsultaService;
import br.com.odontosys.service.DentistaService;
import br.com.odontosys.service.PacienteService;
import config.ConfiguracaoJDBC;
import dao.ConsultaDaoH2;
import dao.DentistaDaoH2;
import dao.PacienteDaoH2;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static PacienteService pacienteService;
    private static DentistaService dentistaService;
    private static ConsultaService consultaService;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarSistema();

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n==================================");
            System.out.println("      SISTEMA ODONTOSYS 1.0       ");
            System.out.println("==================================");
            System.out.println("1. Cadastrar novo Paciente");
            System.out.println("2. Cadastrar novo Dentista");
            System.out.println("3. Agendar Consulta");
            System.out.println("4. Listar todas as Consultas");
            System.out.println("0. Sair do Sistema");
            System.out.print("-> Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        cadastrarPaciente();
                        break;
                    case 2:
                        cadastrarDentista();
                        break;
                    case 3:
                        agendarConsulta();
                        break;
                    case 4:
                        listarConsultas();
                        break;
                    case 0:
                        System.out.println("Encerrando o sistema... Até logo!");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("ERRO: Por favor, digite apenas números válidos.");
            } catch (Exception e) {
                System.out.println("ERRO NO SISTEMA: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void inicializarSistema() {
        ConfiguracaoJDBC config = new ConfiguracaoJDBC();
        pacienteService = new PacienteService(new PacienteDaoH2(config));
        dentistaService = new DentistaService(new DentistaDaoH2(config));
        consultaService = new ConsultaService(new ConsultaDaoH2(config));
    }

    private static void cadastrarPaciente() {
        System.out.println("\n--- CADASTRAR PACIENTE ---");
        System.out.print("Nome Completo: ");
        String nome = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        System.out.print("Data de Nascimento (DD/MM/AAAA): ");
        String dataString = scanner.nextLine();
        LocalDate dataNascimento = LocalDate.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Paciente novoPaciente = new Paciente(null, nome, dataNascimento, telefone, email);
        pacienteService.salvar(novoPaciente);
        System.out.println("Sucesso! Paciente cadastrado com sucesso.");
    }

    private static void cadastrarDentista() {
        System.out.println("\n--- CADASTRAR DENTISTA ---");
        System.out.print("Nome do Dentista: ");
        String nome = scanner.nextLine();

        System.out.print("Especialidade (ex: Geral, Ortodontia): ");
        String especialidade = scanner.nextLine();

        System.out.print("CRO: ");
        String cro = scanner.nextLine();

        Dentista novoDentista = new Dentista(null, nome, especialidade, cro, "0000");
        dentistaService.registrarDentista(novoDentista);
        System.out.println("Sucesso! Dentista registrado.");
    }

    private static void agendarConsulta() {
        System.out.println("\n--- AGENDAR CONSULTA ---");

        System.out.print("ID do Paciente: ");
        Long pacienteId = Long.parseLong(scanner.nextLine());
        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);

        System.out.print("ID do Dentista: ");
        Long dentistaId = Long.parseLong(scanner.nextLine());
        Dentista dentista = new Dentista();
        dentista.setId(dentistaId);

        System.out.print("Data da Consulta (DD/MM/AAAA): ");
        LocalDate data = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        System.out.print("Horário da Consulta (HH:MM) - Ex: 14:30 : ");
        LocalTime horario = LocalTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("HH:mm"));

        Consulta consulta = new Consulta(null, data, horario, StatusConsulta.AGENDADA, paciente, dentista);

        Consulta agendada = consultaService.agendar(consulta);
        System.out.println("Sucesso! Consulta agendada com o ID: " + agendada.getId());
    }

    private static void listarConsultas() {
        System.out.println("\n--- LISTA DE CONSULTAS ---");
        List<Consulta> consultas = consultaService.buscarTodas();

        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta agendada.");
            return;
        }

        for (Consulta c : consultas) {
            System.out.println("ID: " + c.getId() + " | Data: " + c.getData() + " às " + c.getHorario() +
                    " | Paciente: " + c.getPaciente().getNomeCompleto() +
                    " | Dentista: " + c.getDentista().getNome() +
                    " | Status: " + c.getStatus());
        }
    }
}