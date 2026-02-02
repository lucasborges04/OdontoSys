package br.com.odontosys.domain;

import br.com.odontosys.domain.enums.StatusConsulta;
import java.time.LocalDate;
import java.time.LocalTime;

public class Consulta {
    private Long id;
    private LocalDate data;
    private LocalTime horario;
    private StatusConsulta status;

    private Paciente paciente;
    private Dentista dentista;

    public Consulta() {}

    public Consulta(Long id, LocalDate data, LocalTime horario, StatusConsulta status, Paciente paciente, Dentista dentista) {
        this.id = id;
        this.data = data;
        this.horario = horario;
        this.status = status;
        this.paciente = paciente;
        this.dentista = dentista;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public StatusConsulta getStatus() { return status; }
    public void setStatus(StatusConsulta status) { this.status = status; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Dentista getDentista() { return dentista; }
    public void setDentista(Dentista dentista) { this.dentista = dentista; }
}