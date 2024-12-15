package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipologia_irrigacao database table.
 * 
 */
@Entity
@Table(name="tipologia_irrigacao")
public class TipologiaIrrigacao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_tipologia_irrigacao")
	private Integer ideTipologiaIrrigacao;

	@Column(name="dsc_tipologia_irrigacao")
	private String dscTipologiaIrrigacao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public TipologiaIrrigacao() {
	}

	public Integer getIdeTipologiaIrrigacao() {
		return this.ideTipologiaIrrigacao;
	}

	public void setIdeTipologiaIrrigacao(Integer ideTipologiaIrrigacao) {
		this.ideTipologiaIrrigacao = ideTipologiaIrrigacao;
	}

	public String getDscTipologiaIrrigacao() {
		return this.dscTipologiaIrrigacao;
	}

	public void setDscTipologiaIrrigacao(String dscTipologiaIrrigacao) {
		this.dscTipologiaIrrigacao = dscTipologiaIrrigacao;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipologiaIrrigacao == null) ? 0 : ideTipologiaIrrigacao
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TipologiaIrrigacao other = (TipologiaIrrigacao) obj;
		if (ideTipologiaIrrigacao == null) {
			if (other.ideTipologiaIrrigacao != null) {
				return false;
			}
		} else if (!ideTipologiaIrrigacao.equals(other.ideTipologiaIrrigacao)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipologiaIrrigacao);
	}
}