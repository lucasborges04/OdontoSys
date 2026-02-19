package br.com.odontosys.service; // Note que o pacote continua o mesmo!

import br.com.odontosys.domain.Dentista;
import config.ConfiguracaoJDBC;
import dao.DentistaDaoH2;
import br.com.odontosys.service.DentistaService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DentistaServiceTest {

    @Test
    public void naoDevePermitirCroDuplicado() {
        DentistaDaoH2 dao = new DentistaDaoH2(new ConfiguracaoJDBC());
        DentistaService service = new DentistaService(dao);

        String croTeste = "CRO-" + System.currentTimeMillis();

        Dentista d1 = new Dentista(null, "Original", "Geral", croTeste, "0000");
        service.registrarDentista(d1);

        Dentista d2 = new Dentista(null, "Clone", "Geral", croTeste, "1111");

        assertThrows(RuntimeException.class, () -> {
            service.registrarDentista(d2);
        });
    }
}