package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the caracteristica_captacao_barramento database table.
 * 
 */
@Entity
@Table(name="dominio_barramento")
public class DominioBarramento implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_dominio_barramento")
	private Integer ideDominioBarramento;

	@Column(name="dsc_dominio_barramento")
	private String dscDominioBarramento;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public DominioBarramento() {
	}

	public DominioBarramento(Integer ideDominioBarramento) {
		this.ideDominioBarramento = ideDominioBarramento;
	}

	public Integer getIdeDominioBarramento() {
		return ideDominioBarramento;
	}

	public void setIdeDominioBarramento(Integer ideDominioBarramento) {
		this.ideDominioBarramento = ideDominioBarramento;
	}

	public String getDscDominioBarramento() {
		return dscDominioBarramento;
	}

	public void setDscDominioBarramento(String dscDominioBarramento) {
		this.dscDominioBarramento = dscDominioBarramento;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return new Long(this.ideDominioBarramento);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideDominioBarramento == null) ? 0 : ideDominioBarramento
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
		DominioBarramento other = (DominioBarramento) obj;
		if (ideDominioBarramento == null) {
			if (other.ideDominioBarramento != null) {
				return false;
			}
		} else if (!ideDominioBarramento.equals(other.ideDominioBarramento)) {
			return false;
		}
		return true;
	}
}