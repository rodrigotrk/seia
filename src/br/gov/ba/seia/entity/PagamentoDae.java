package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="pagamento_dae")
public class PagamentoDae {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagamento_dae_seq")
	@SequenceGenerator(name = "pagamento_dae_seq", sequenceName = "pagamento_dae_seq", allocationSize = 1)
	@Column(name="ide_pagamento_dae")
	private Integer idePagamentoDae;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ide_dae")
	private Dae ideDae;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ide_arquivo_baixa_dae")
	private ArquivoBaixaDae ideArquivoBaixaDae;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dt_pagamento")
	private Date dataPagamento;
	
	@Column(name="vlr_pagamento")
	private Double valorPagamento;

	
	public PagamentoDae() {

	}
	
	public Integer getIdePagamentoDae() {
		return idePagamentoDae;
	}

	public void setIdePagamentoDae(Integer idePagamentoDae) {
		this.idePagamentoDae = idePagamentoDae;
	}

	public Dae getIdeDae() {
		return ideDae;
	}

	public void setIdeDae(Dae ideDae) {
		this.ideDae = ideDae;
	}

	public ArquivoBaixaDae getIdeArquivoBaixaDae() {
		return ideArquivoBaixaDae;
	}

	public void setIdeArquivoBaixaDae(ArquivoBaixaDae ideArquivoBaixaDae) {
		this.ideArquivoBaixaDae = ideArquivoBaixaDae;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Double getValorPagamento() {
		return valorPagamento;
	}

	public void setValorPagamento(Double valorPagamento) {
		this.valorPagamento = valorPagamento;
	}
	
}
