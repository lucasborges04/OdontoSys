package br.com.odontosys.domain;

public class ConsultaTratamento {
    private Long id;
    private Consulta consulta;
    private Tratamento tratamento;

    public ConsultaTratamento() {}

    public ConsultaTratamento(Long id, Consulta consulta, Tratamento tratamento) {
        this.id = id;
        this.consulta = consulta;
        this.tratamento = tratamento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Consulta getConsulta() { return consulta; }
    public void setConsulta(Consulta consulta) { this.consulta = consulta; }

    public Tratamento getTratamento() { return tratamento; }
    public void setTratamento(Tratamento tratamento) { this.tratamento = tratamento; }
}