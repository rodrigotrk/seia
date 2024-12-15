package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name="arquivo_baixa_dae")
public class ArquivoBaixaDae {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arquivo_baixa_dae_seq")
	@SequenceGenerator(name = "arquivo_baixa_dae_seq", sequenceName = "arquivo_baixa_dae_seq", allocationSize = 1)
	@Column(name="ide_arquivo_baixa_dae")
	private Integer ideArquivoBaixaDae;
	
	@ManyToOne
	@JoinColumn(name="ide_usuario")
	private Usuario ideUsuario;
	
	@Column(name="nom_arquivo")
	private String nomArquivo;
	
	@Column(name="dsc_processamento")
	private String descricaoProcessamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dt_inicio_periodo_pagamento")
	private Date dataInicioPeriodoPagamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dt_fim_periodo_pagamento")
	private Date dataFimPeriodoPagamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dt_processamento")
	private Date dataProcessamento;

	@Column(name="ind_processado")
	private boolean indProcessado;
	
	@ManyToOne
	@JoinColumn(name = "ide_sefaz_codigo_receita")
	private SefazCodigoReceita sefazCodigoReceita;
	
	public ArquivoBaixaDae() {
		
	}
	
	public Integer getIdeArquivoBaixaDae() {
		return ideArquivoBaixaDae;
	}

	public void setIdeArquivoBaixaDae(Integer ideArquivoBaixaDae) {
		this.ideArquivoBaixaDae = ideArquivoBaixaDae;
	}

	public Usuario getIdeUsuario() {
		return ideUsuario;
	}

	public void setIdeUsuario(Usuario ideUsuario) {
		this.ideUsuario = ideUsuario;
	}

	public String getNomArquivo() {
		return nomArquivo;
	}

	public void setNom_arquivo(String nomArquivo) {
		this.nomArquivo = nomArquivo;
	}

	public String getDescricaoProcessamento() {
		return descricaoProcessamento;
	}

	public void setDescricaoProcessamento(String descricaoProcessamento) {
		this.descricaoProcessamento = descricaoProcessamento;
	}

	public Date getDataInicioPeriodoPagamento() {
		return dataInicioPeriodoPagamento;
	}

	public void setDataInicioPeriodoPagamento(Date dataInicioPeriodoPagamento) {
		this.dataInicioPeriodoPagamento = dataInicioPeriodoPagamento;
	}

	public Date getDataFimPeriodoPagamento() {
		return dataFimPeriodoPagamento;
	}

	public void setDataFimPeriodoPagamento(Date dataFimPeriodoPagamento) {
		this.dataFimPeriodoPagamento = dataFimPeriodoPagamento;
	}

	public Date getDataProcessamento() {
		return dataProcessamento;
	}

	public void setDataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}

	public boolean isIndProcessado() {
		return indProcessado;
	}

	public void setIndProcessado(boolean indProcessado) {
		this.indProcessado = indProcessado;
	}

	public void setNomArquivo(String nomArquivo) {
		this.nomArquivo = nomArquivo;
	}

	public SefazCodigoReceita getSefazCodigoReceita() {
		return sefazCodigoReceita;
	}

	public void setSefazCodigoReceita(SefazCodigoReceita sefazCodigoReceita) {
		this.sefazCodigoReceita = sefazCodigoReceita;
	}
	
}
