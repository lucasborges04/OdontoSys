package br.com.odontosys.domain;

public class Dentista {
    private Long id;
    private String nome;
    private String especialidade;
    private String cro; // Conselho Regional de Odontologia
    private String telefone;

    public Dentista() {}

    public Dentista(Long id, String nome, String especialidade, String cro, String telefone) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
        this.cro = cro;
        this.telefone = telefone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getCro() { return cro; }
    public void setCro(String cro) { this.cro = cro; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}