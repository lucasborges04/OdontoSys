package br.com.odontosys.service;

import br.com.odontosys.domain.Paciente;
import dao.IDao;
import java.util.List;

public class PacienteService {

    private final IDao<Paciente> pacienteDao;

    public PacienteService(IDao<Paciente> pacienteDao) {
        this.pacienteDao = pacienteDao;
    }

    public Paciente salvar(Paciente paciente) {
        List<Paciente> todos = pacienteDao.buscarTodos();

        for (Paciente p : todos) {
            if (p.getEmail() != null && paciente.getEmail() != null && p.getEmail().equalsIgnoreCase(paciente.getEmail())) {
                throw new RuntimeException("Erro: Paciente já cadastrado com o e-mail: " + paciente.getEmail());
            }
        }

        return pacienteDao.salvar(paciente);
    }

    public List<Paciente> buscarTodos() {
        return pacienteDao.buscarTodos();
    }
}