package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipologia_pulverizacao database table.
 * 
 */
@Entity
@Table(name="tipologia_pulverizacao")
public class TipologiaPulverizacao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_tipologia_pulverizacao")
	private Integer ideTipologiaPulverizacao;

	@Column(name="dsc_tipologia_pulverizacao")
	private String dscTipologiaPulverizacao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public TipologiaPulverizacao() {
	}

	public Integer getIdeTipologiaPulverizacao() {
		return this.ideTipologiaPulverizacao;
	}

	public void setIdeTipologiaPulverizacao(Integer ideTipologiaPulverizacao) {
		this.ideTipologiaPulverizacao = ideTipologiaPulverizacao;
	}

	public String getDscTipologiaPulverizacao() {
		return this.dscTipologiaPulverizacao;
	}

	public void setDscTipologiaPulverizacao(String dscTipologiaPulverizacao) {
		this.dscTipologiaPulverizacao = dscTipologiaPulverizacao;
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
				+ ((ideTipologiaPulverizacao == null) ? 0
						: ideTipologiaPulverizacao.hashCode());
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
		TipologiaPulverizacao other = (TipologiaPulverizacao) obj;
		if (ideTipologiaPulverizacao == null) {
			if (other.ideTipologiaPulverizacao != null) {
				return false;
			}
		} else if (!ideTipologiaPulverizacao
				.equals(other.ideTipologiaPulverizacao)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipologiaPulverizacao);
	}
}