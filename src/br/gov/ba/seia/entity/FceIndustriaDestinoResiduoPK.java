package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_industria_destino_residuo database table.
 * 
 */
@Embeddable
public class FceIndustriaDestinoResiduoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_industria")
	private Integer ideFceIndustria;

	@Column(name="ide_destino_residuo")
	private Integer ideDestinoResiduo;

	public FceIndustriaDestinoResiduoPK() {
	}

	public FceIndustriaDestinoResiduoPK(FceIndustria fceIndustria, DestinoResiduo destinoResiduo) {
		ideFceIndustria = fceIndustria.getIdeFceIndustria();
		ideDestinoResiduo = destinoResiduo.getIdeDestinoResiduo();
	}

	public Integer getIdeFceIndustria() {
		return ideFceIndustria;
	}
	public void setIdeFceIndustria(Integer ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}
	public Integer getIdeDestinoResiduo() {
		return ideDestinoResiduo;
	}
	public void setIdeDestinoResiduo(Integer ideDestinoResiduo) {
		this.ideDestinoResiduo = ideDestinoResiduo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideDestinoResiduo == null) ? 0 : ideDestinoResiduo
						.hashCode());
		result = prime * result
				+ ((ideFceIndustria == null) ? 0 : ideFceIndustria.hashCode());
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
		FceIndustriaDestinoResiduoPK other = (FceIndustriaDestinoResiduoPK) obj;
		if (ideDestinoResiduo == null) {
			if (other.ideDestinoResiduo != null) {
				return false;
			}
		} else if (!ideDestinoResiduo.equals(other.ideDestinoResiduo)) {
			return false;
		}
		if (ideFceIndustria == null) {
			if (other.ideFceIndustria != null) {
				return false;
			}
		} else if (!ideFceIndustria.equals(other.ideFceIndustria)) {
			return false;
		}
		return true;
	}
}