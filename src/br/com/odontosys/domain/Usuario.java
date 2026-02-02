package br.com.odontosys.domain;

import br.com.odontosys.domain.enums.NivelPermissao;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private NivelPermissao nivelPermissao;

    public Usuario() {
    }

    public Usuario(Long id, String nome, String email, String senha, NivelPermissao nivelPermissao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.nivelPermissao = nivelPermissao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public NivelPermissao getNivelPermissao() { return nivelPermissao; }
    public void setNivelPermissao(NivelPermissao nivelPermissao) { this.nivelPermissao = nivelPermissao; }
}
