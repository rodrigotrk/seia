package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "pagamento_reposicao_florestal")
@XmlRootElement
public class PagamentoReposicaoFlorestal implements Serializable, BaseEntity {

	private static final long serialVersionUID = -2641613446166022168L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAGAMENTO_REPOSICAO_FLORESTAL_SEQ")
	@SequenceGenerator(name = "PAGAMENTO_REPOSICAO_FLORESTAL_SEQ", sequenceName = "PAGAMENTO_REPOSICAO_FLORESTAL_SEQ", allocationSize = 1)
	@NotNull
	@Column(name = "ide_pagamento_reposicao_florestal")
	private Integer idePagamentoReposicaoFlorestal;

	@Basic(optional = false)
	@Column(name = "nom_pagamento_reposicao_florestal", length = 150, nullable = false)
	private String nomPagamentoReposicaoFlorestal;

	@JoinColumn(name = "ide_pagamento_reposicao_florestal_pai", referencedColumnName = "ide_pagamento_reposicao_florestal", nullable = true)
	@ManyToOne(fetch=FetchType.LAZY, optional = false)
	private PagamentoReposicaoFlorestal idePagamentoReposicaoFlorestalPai;

	public Integer getIdePagamentoReposicaoFlorestal() {
		return idePagamentoReposicaoFlorestal;
	}

	public void setIdePagamentoReposicaoFlorestal(
			Integer idePagamentoReposicaoFlorestal) {
		this.idePagamentoReposicaoFlorestal = idePagamentoReposicaoFlorestal;
	}

	public String getNomPagamentoReposicaoFlorestal() {
		return nomPagamentoReposicaoFlorestal;
	}

	public void setNomPagamentoReposicaoFlorestal(
			String nomPagamentoReposicaoFlorestal) {
		this.nomPagamentoReposicaoFlorestal = nomPagamentoReposicaoFlorestal;
	}

	public PagamentoReposicaoFlorestal getIdePagamentoReposicaoFlorestalPai() {
		return idePagamentoReposicaoFlorestalPai;
	}

	public void setIdePagamentoReposicaoFlorestalPai(
			PagamentoReposicaoFlorestal idePagamentoReposicaoFlorestalPai) {
		this.idePagamentoReposicaoFlorestalPai = idePagamentoReposicaoFlorestalPai;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idePagamentoReposicaoFlorestal == null) ? 0
						: idePagamentoReposicaoFlorestal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagamentoReposicaoFlorestal other = (PagamentoReposicaoFlorestal) obj;
		if (idePagamentoReposicaoFlorestal == null) {
			if (other.idePagamentoReposicaoFlorestal != null)
				return false;
		} else if (!idePagamentoReposicaoFlorestal
				.equals(other.idePagamentoReposicaoFlorestal))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.idePagamentoReposicaoFlorestal);
	}

}
