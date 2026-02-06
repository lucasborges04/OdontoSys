package br.com.odontosys;

import br.com.odontosys.domain.Dentista;
import config.ConfiguracaoJDBC;
import dao.DentistaDaoH2;
import service.DentistaService;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        ConfiguracaoJDBC configuracao = new ConfiguracaoJDBC();
        DentistaDaoH2 dao = new DentistaDaoH2(configuracao);

        DentistaService service = new DentistaService(dao);

        System.out.println("--- INICIANDO SISTEMA ---");

        try {
            Dentista d1 = new Dentista(null, "Dr. House Clone", "Clonagem", "CRO-1234", "0000-0000");
            service.registrarDentista(d1);
            System.out.println("Sucesso! Dentista cadastrado.");
        } catch (Exception e) {
            System.err.println("FALHA AO CADASTRAR: " + e.getMessage());
        }

        try {
            Dentista d2 = new Dentista(null, "Dra. Cuca", "Ortodontia", "CRO-9999", "1111-2222");
            service.registrarDentista(d2);
            System.out.println("\nSucesso! " + d2.getNome() + " foi cadastrada.");
        } catch (Exception e) {
            System.err.println("FALHA: " + e.getMessage());
        }

        System.out.println("\n--- LISTAGEM FINAL ---");
        List<Dentista> lista = service.listarTodos();
        for (Dentista d : lista) {
            System.out.println(d.getNome() + " - " + d.getCro());
        }
    }
}