package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_industria_residuo_gerado database table.
 * 
 */
@Embeddable
public class FceIndustriaResiduoGeradoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_industria")
	private Integer ideFceIndustria;

	@Column(name="ide_tipo_residuo_gerado")
	private Integer ideTipoResiduoGerado;

	public FceIndustriaResiduoGeradoPK() {

	}

	public FceIndustriaResiduoGeradoPK(FceIndustria ideFceIndustria,TipoResiduoGerado ideTipoResiduoGerado) {
		this.ideFceIndustria = ideFceIndustria.getIdeFceIndustria();
		this.ideTipoResiduoGerado = ideTipoResiduoGerado.getIdeTipoResiduoGerado();
	}

	public Integer getIdeFceIndustria() {
		return ideFceIndustria;
	}
	public void setIdeFceIndustria(Integer ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}
	public Integer getIdeTipoResiduoGerado() {
		return ideTipoResiduoGerado;
	}
	public void setIdeTipoResiduoGerado(Integer ideTipoResiduoGerado) {
		this.ideTipoResiduoGerado = ideTipoResiduoGerado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceIndustria == null) ? 0 : ideFceIndustria.hashCode());
		result = prime
				* result
				+ ((ideTipoResiduoGerado == null) ? 0 : ideTipoResiduoGerado
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
		FceIndustriaResiduoGeradoPK other = (FceIndustriaResiduoGeradoPK) obj;
		if (ideFceIndustria == null) {
			if (other.ideFceIndustria != null) {
				return false;
			}
		} else if (!ideFceIndustria.equals(other.ideFceIndustria)) {
			return false;
		}
		if (ideTipoResiduoGerado == null) {
			if (other.ideTipoResiduoGerado != null) {
				return false;
			}
		} else if (!ideTipoResiduoGerado.equals(other.ideTipoResiduoGerado)) {
			return false;
		}
		return true;
	}
}