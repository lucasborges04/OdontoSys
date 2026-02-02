package br.com.odontosys.domain;

import br.com.odontosys.domain.enums.FormaPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {
    private Long id;
    private BigDecimal valor;
    private LocalDateTime dataPagamento; // Data e Hora exata do pagamento
    private FormaPagamento formaPagamento;
    private Consulta consulta;

    public Pagamento() {}

    public Pagamento(Long id, BigDecimal valor, LocalDateTime dataPagamento, FormaPagamento formaPagamento, Consulta consulta) {
        this.id = id;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.formaPagamento = formaPagamento;
        this.consulta = consulta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public Consulta getConsulta() { return consulta; }
    public void setConsulta(Consulta consulta) { this.consulta = consulta; }
}