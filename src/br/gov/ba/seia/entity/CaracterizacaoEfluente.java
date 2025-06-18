package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the caracterizacao_efluente database table.
 * 
 */
@Entity
@Table(name="caracterizacao_efluente")
public class CaracterizacaoEfluente implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ide_caracterizacao_efluente")
	private Integer ideCaracterizacaoEfluente;

	@Column(name="dsc_caracterizacao_efluente")
	private String dscCaracterizacaoEfluente;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public CaracterizacaoEfluente() {
	}

	public Integer getIdeCaracterizacaoEfluente() {
		return this.ideCaracterizacaoEfluente;
	}

	public void setIdeCaracterizacaoEfluente(Integer ideCaracterizacaoEfluente) {
		this.ideCaracterizacaoEfluente = ideCaracterizacaoEfluente;
	}

	public String getDscCaracterizacaoEfluente() {
		return this.dscCaracterizacaoEfluente;
	}

	public void setDscCaracterizacaoEfluente(String dscCaracterizacaoEfluente) {
		this.dscCaracterizacaoEfluente = dscCaracterizacaoEfluente;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return new Long(this.ideCaracterizacaoEfluente);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCaracterizacaoEfluente == null) ? 0
						: ideCaracterizacaoEfluente.hashCode());
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
		CaracterizacaoEfluente other = (CaracterizacaoEfluente) obj;
		if (ideCaracterizacaoEfluente == null) {
			if (other.ideCaracterizacaoEfluente != null) {
				return false;
			}
		} else if (!ideCaracterizacaoEfluente
				.equals(other.ideCaracterizacaoEfluente)) {
			return false;
		}
		return true;
	}
}