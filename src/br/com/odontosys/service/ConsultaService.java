package br.com.odontosys.service;

import br.com.odontosys.domain.Consulta;
import dao.IDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ConsultaService {

    private final IDao<Consulta> consultaDao;

    public ConsultaService(IDao<Consulta> consultaDao) {
        this.consultaDao = consultaDao;
    }

    public Consulta agendar(Consulta consulta) {

        if (consulta.getDentista() == null || consulta.getDentista().getId() == null) {
            throw new RuntimeException("Erro: É obrigatório informar um Dentista válido.");
        }
        if (consulta.getPaciente() == null || consulta.getPaciente().getId() == null) {
            throw new RuntimeException("Erro: É obrigatório informar um Paciente válido.");
        }
        if (consulta.getData() == null || consulta.getHorario() == null) {
            throw new RuntimeException("Erro: Data e Horário da consulta são obrigatórios.");
        }


        if (consulta.getData().isBefore(LocalDate.now())) {
            throw new RuntimeException("Erro: Não é possível agendar consultas para uma data passada.");
        }

        if (consulta.getData().equals(LocalDate.now()) && consulta.getHorario().isBefore(LocalTime.now())) {
            throw new RuntimeException("Erro: Este horário já passou. Escolha um horário futuro.");
        }

        List<Consulta> todasConsultas = consultaDao.buscarTodos();

        for (Consulta c : todasConsultas) {
            if (c.getDentista().getId().equals(consulta.getDentista().getId()) &&
                    c.getData().equals(consulta.getData()) &&
                    c.getHorario().equals(consulta.getHorario())) {

                throw new RuntimeException("Erro: O dentista " + c.getDentista().getNome() +
                        " já possui agendamento para este dia e horário!");
            }
        }

        return consultaDao.salvar(consulta);
    }

    public void cancelar(Long id) {
        consultaDao.excluir(id);
    }

    public List<Consulta> buscarTodas() {
        return consultaDao.buscarTodos();
    }
}