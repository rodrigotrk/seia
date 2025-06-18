package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "cumprimento_reposicao_florestal")
@XmlRootElement
public class CumprimentoReposicaoFlorestal implements Serializable {

	private static final long serialVersionUID = -4270823721392158123L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUMPRIMENTO_REPOSICAO_FLORESTAL_SEQ")
	@SequenceGenerator(name = "CUMPRIMENTO_REPOSICAO_FLORESTAL_SEQ", sequenceName = "CUMPRIMENTO_REPOSICAO_FLORESTAL_SEQ", allocationSize = 1)
	@NotNull
	@Column(name = "ide_cumprimento_reposicao_florestal")
	private Integer ideCumprimentoReposicaoFlorestal;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	private Requerimento requerimento;

	@JoinColumn(name = "ide_pagamento_reposicao_florestal", referencedColumnName = "ide_pagamento_reposicao_florestal")
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	private PagamentoReposicaoFlorestal idePagamentoReposicaoFlorestal;
	
	@Column(name = "vlr_pecuniario", nullable = false, precision = 16, scale = 2)
	private BigDecimal vlrPecuniario;
	
	@OneToMany(mappedBy = "ideCumprimentoReposicaoFlorestal", fetch = FetchType.LAZY)
	private Collection<DetentorReposicaoFlorestal> detentorReposicaoFlorestalCollection;

	@OneToMany(mappedBy = "ideCumprimentoReposicaoFlorestal", fetch = FetchType.LAZY)
	private Collection<ConsumidorReposicaoFlorestal> consumidorReposicaoFlorestal;

	@OneToMany(mappedBy = "ideCumprimentoReposicaoFlorestal", fetch = FetchType.LAZY)
	private Collection<DevedorReposicaoFlorestal> devedorReposicaoFlorestalCollection;

	@Column(name = "ind_ciente", nullable = false)
	private Boolean indCiente;

	public CumprimentoReposicaoFlorestal() {}
	
	public CumprimentoReposicaoFlorestal(Integer ideCumprimentoReposicaoFlorestal) {
		this.ideCumprimentoReposicaoFlorestal = ideCumprimentoReposicaoFlorestal;
	}
	
	public Integer getIdeCumprimentoReposicaoFlorestal() {
		return ideCumprimentoReposicaoFlorestal;
	}

	public void setIdeCumprimentoReposicaoFlorestal(
			Integer ideCumprimentoReposicaoFlorestal) {
		this.ideCumprimentoReposicaoFlorestal = ideCumprimentoReposicaoFlorestal;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public PagamentoReposicaoFlorestal getIdePagamentoReposicaoFlorestal() {
		return idePagamentoReposicaoFlorestal;
	}

	public void setIdePagamentoReposicaoFlorestal(
			PagamentoReposicaoFlorestal idePagamentoReposicaoFlorestal) {
		this.idePagamentoReposicaoFlorestal = idePagamentoReposicaoFlorestal;
	}

	public Collection<DetentorReposicaoFlorestal> getDetentorReposicaoFlorestalCollection() {
		return detentorReposicaoFlorestalCollection;
	}

	public void setDetentorReposicaoFlorestalCollection(
			Collection<DetentorReposicaoFlorestal> detentorReposicaoFlorestalCollection) {
		this.detentorReposicaoFlorestalCollection = detentorReposicaoFlorestalCollection;
	}

	public Collection<ConsumidorReposicaoFlorestal> getConsumidorReposicaoFlorestal() {
		return consumidorReposicaoFlorestal;
	}

	public void setConsumidorReposicaoFlorestal(
			Collection<ConsumidorReposicaoFlorestal> consumidorReposicaoFlorestal) {
		this.consumidorReposicaoFlorestal = consumidorReposicaoFlorestal;
	}

	public Collection<DevedorReposicaoFlorestal> getDevedorReposicaoFlorestalCollection() {
		return devedorReposicaoFlorestalCollection;
	}

	public void setDevedorReposicaoFlorestalCollection(
			Collection<DevedorReposicaoFlorestal> devedorReposicaoFlorestalCollection) {
		this.devedorReposicaoFlorestalCollection = devedorReposicaoFlorestalCollection;
	}

	public Boolean getIndCiente() {
		return indCiente;
	}

	public void setIndCiente(Boolean indCiente) {
		this.indCiente = indCiente;
	}

	public BigDecimal getVlrPecuniario() {
		return vlrPecuniario;
	}

	public void setVlrPecuniario(BigDecimal vlrPecuniario) {
		this.vlrPecuniario = vlrPecuniario;
	}

}
