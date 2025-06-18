package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_industria_residuo_civil database table.
 * 
 */
@Embeddable
public class FceIndustriaResiduoCivilPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_industria")
	private Integer ideFceIndustria;

	@Column(name="ide_tipo_residuo_cons_civil")
	private Integer ideTipoResiduoConsCivil;

	public FceIndustriaResiduoCivilPK() {
	}

	public FceIndustriaResiduoCivilPK(FceIndustria ideFceIndustria,TipoResiduoConsCivil ideTipoResiduoConsCivil) {
		this.ideFceIndustria = ideFceIndustria.getIdeFceIndustria();
		this.ideTipoResiduoConsCivil = ideTipoResiduoConsCivil.getIdeTipoResiduoConsCivil();
	}

	public Integer getIdeFceIndustria() {
		return ideFceIndustria;
	}
	public void setIdeFceIndustria(Integer ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}
	public Integer getIdeTipoResiduoConsCivil() {
		return ideTipoResiduoConsCivil;
	}
	public void setIdeTipoResiduoConsCivil(Integer ideTipoResiduoConsCivil) {
		this.ideTipoResiduoConsCivil = ideTipoResiduoConsCivil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceIndustria == null) ? 0 : ideFceIndustria.hashCode());
		result = prime
				* result
				+ ((ideTipoResiduoConsCivil == null) ? 0
						: ideTipoResiduoConsCivil.hashCode());
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
		FceIndustriaResiduoCivilPK other = (FceIndustriaResiduoCivilPK) obj;
		if (ideFceIndustria == null) {
			if (other.ideFceIndustria != null) {
				return false;
			}
		} else if (!ideFceIndustria.equals(other.ideFceIndustria)) {
			return false;
		}
		if (ideTipoResiduoConsCivil == null) {
			if (other.ideTipoResiduoConsCivil != null) {
				return false;
			}
		} else if (!ideTipoResiduoConsCivil
				.equals(other.ideTipoResiduoConsCivil)) {
			return false;
		}
		return true;
	}
}