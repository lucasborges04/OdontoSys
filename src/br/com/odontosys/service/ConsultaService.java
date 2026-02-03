package br.com.odontosys.service;

import br.com.odontosys.domain.Consulta;
import br.com.odontosys.repository.ConsultaRepository;

public class ConsultaService {

    private final ConsultaRepository repository;

    public ConsultaService() {
        this.repository = new ConsultaRepository();
    }

    public void agendarConsulta(Consulta consulta) {
        boolean existeConflito = repository.existeConsultaNoHorario(
                consulta.getDentista().getId(),
                consulta.getData(),
                consulta.getHorario()
        );

        if (existeConflito) {
            throw new IllegalArgumentException("Erro: O dentista já possui agendamento para este dia e horário!");
        }

        repository.agendar(consulta);
        System.out.println(">>> Regra de negócio validada: Agendamento permitido.");
    }

    public void listarConsultas() {
        var lista = repository.listarTodas();
        lista.forEach(c -> System.out.println(c.getId() + " - " + c.getData() + " - " + c.getDentista().getNome()));
    }
}