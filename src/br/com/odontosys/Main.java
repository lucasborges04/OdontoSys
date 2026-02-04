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
import config.ConfiguracaoJDBC;
import dao.DentistaDaoH2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConfiguracaoJDBC configuracao = new ConfiguracaoJDBC();

        DentistaDaoH2 dao = new DentistaDaoH2(configuracao);

        Dentista d = new Dentista(null, "Dr. House", "Diagnóstico", "CRO-1234", "9999-8888");
        //dao.salvar(d);

        System.out.println("Comando enviado! Verifique o banco de dados.");

        System.out.println("\n--- LISTA DE DENTISTAS NO BANCO ---");

        List<Dentista> lista = dao.buscarTodos();

        for (Dentista dentista : lista) {
            System.out.println("ID: " + dentista.getId() +
                    " | Nome: " + dentista.getNome() +
                    " | CRO: " + dentista.getCro());
        }
    }
}