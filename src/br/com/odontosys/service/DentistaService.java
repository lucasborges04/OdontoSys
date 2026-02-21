package br.com.odontosys.service;

import br.com.odontosys.domain.Dentista;
import dao.IDao;
import java.util.List;

public class DentistaService {

    private final IDao<Dentista> dentistaDao;

    public DentistaService(IDao<Dentista> dentistaDao) {
        this.dentistaDao = dentistaDao;
    }

    public Dentista registrarDentista(Dentista dentista) {
        List<Dentista> todos = dentistaDao.buscarTodos();

        for (Dentista existente : todos) {
            if (existente.getCro() != null && existente.getCro().equals(dentista.getCro())) {
                throw new RuntimeException("Erro: Já existe um dentista cadastrado com o CRO: " + dentista.getCro());
            }
        }

        return dentistaDao.salvar(dentista);
    }

    public List<Dentista> listarTodos() {
        return dentistaDao.buscarTodos();
    }

    public Dentista buscarPorId(Long id) {
        List<Dentista> todos = listarTodos();

        for (Dentista d : todos) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }

}