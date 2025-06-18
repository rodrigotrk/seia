package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_industria_substancia database table.
 * 
 */
@Embeddable
public class FceIndustriaSubstanciaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_industria")
	private Integer ideFceIndustria;

	@Column(name="ide_tipo_substancia")
	private Integer ideTipoSubstancia;

	public FceIndustriaSubstanciaPK() {
	}
	public FceIndustriaSubstanciaPK(FceIndustria ideFceIndustria, TipoSubstancia ideTipoSubstancia) {
		this.ideFceIndustria = ideFceIndustria.getIdeFceIndustria();
		this.ideTipoSubstancia = ideTipoSubstancia.getIdeTipoSubstancia();
	}

	public Integer getIdeFceIndustria() {
		return ideFceIndustria;
	}
	public void setIdeFceIndustria(Integer ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}
	public Integer getIdeTipoSubstancia() {
		return ideTipoSubstancia;
	}
	public void setIdeTipoSubstancia(Integer ideTipoSubstancia) {
		this.ideTipoSubstancia = ideTipoSubstancia;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceIndustria == null) ? 0 : ideFceIndustria.hashCode());
		result = prime
				* result
				+ ((ideTipoSubstancia == null) ? 0 : ideTipoSubstancia
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
		FceIndustriaSubstanciaPK other = (FceIndustriaSubstanciaPK) obj;
		if (ideFceIndustria == null) {
			if (other.ideFceIndustria != null) {
				return false;
			}
		} else if (!ideFceIndustria.equals(other.ideFceIndustria)) {
			return false;
		}
		if (ideTipoSubstancia == null) {
			if (other.ideTipoSubstancia != null) {
				return false;
			}
		} else if (!ideTipoSubstancia.equals(other.ideTipoSubstancia)) {
			return false;
		}
		return true;
	}
}