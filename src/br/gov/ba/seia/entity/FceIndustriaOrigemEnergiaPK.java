package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_industria_origem_energia database table.
 * 
 */
@Embeddable
public class FceIndustriaOrigemEnergiaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_industria")
	private Integer ideFceIndustria;

	@Column(name="ide_tipo_origem_energia")
	private Integer ideTipoOrigemEnergia;

	public FceIndustriaOrigemEnergiaPK() {
	}

	public FceIndustriaOrigemEnergiaPK(FceIndustria fceIndustria, TipoOrigemEnergia tipoOrigemEnergia){
		ideFceIndustria = fceIndustria.getIdeFceIndustria();
		ideTipoOrigemEnergia = tipoOrigemEnergia.getIdeTipoOrigemEnergia();
	}

	public Integer getIdeFceIndustria() {
		return ideFceIndustria;
	}
	public void setIdeFceIndustria(Integer ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}
	public Integer getIdeTipoOrigemEnergia() {
		return ideTipoOrigemEnergia;
	}
	public void setIdeTipoOrigemEnergia(Integer ideTipoOrigemEnergia) {
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceIndustria == null) ? 0 : ideFceIndustria.hashCode());
		result = prime
				* result
				+ ((ideTipoOrigemEnergia == null) ? 0 : ideTipoOrigemEnergia
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
		FceIndustriaOrigemEnergiaPK other = (FceIndustriaOrigemEnergiaPK) obj;
		if (ideFceIndustria == null) {
			if (other.ideFceIndustria != null) {
				return false;
			}
		} else if (!ideFceIndustria.equals(other.ideFceIndustria)) {
			return false;
		}
		if (ideTipoOrigemEnergia == null) {
			if (other.ideTipoOrigemEnergia != null) {
				return false;
			}
		} else if (!ideTipoOrigemEnergia.equals(other.ideTipoOrigemEnergia)) {
			return false;
		}
		return true;
	}
}