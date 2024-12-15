package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_industria_emissao_atmosferica database table.
 * 
 */
@Embeddable
public class FceIndustriaEmissaoAtmosfericaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_industria")
	private Integer ideFceIndustria;

	@Column(name="ide_tipo_emissao_atmosferica")
	private Integer ideTipoEmissaoAtmosferica;

	public FceIndustriaEmissaoAtmosfericaPK() {
	}

	public FceIndustriaEmissaoAtmosfericaPK(FceIndustria ideFceIndustria,TipoEmissaoAtmosferica ideTipoEmissaoAtmosferica) {
		this.ideFceIndustria = ideFceIndustria.getIdeFceIndustria();
		this.ideTipoEmissaoAtmosferica = ideTipoEmissaoAtmosferica.getIdeTipoEmissaoAtmosferica();
	}

	public Integer getIdeFceIndustria() {
		return ideFceIndustria;
	}
	public void setIdeFceIndustria(Integer ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}
	public Integer getIdeTipoEmissaoAtmosferica() {
		return ideTipoEmissaoAtmosferica;
	}
	public void setIdeTipoEmissaoAtmosferica(Integer ideTipoEmissaoAtmosferica) {
		this.ideTipoEmissaoAtmosferica = ideTipoEmissaoAtmosferica;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceIndustria == null) ? 0 : ideFceIndustria.hashCode());
		result = prime
				* result
				+ ((ideTipoEmissaoAtmosferica == null) ? 0
						: ideTipoEmissaoAtmosferica.hashCode());
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
		FceIndustriaEmissaoAtmosfericaPK other = (FceIndustriaEmissaoAtmosfericaPK) obj;
		if (ideFceIndustria == null) {
			if (other.ideFceIndustria != null) {
				return false;
			}
		} else if (!ideFceIndustria.equals(other.ideFceIndustria)) {
			return false;
		}
		if (ideTipoEmissaoAtmosferica == null) {
			if (other.ideTipoEmissaoAtmosferica != null) {
				return false;
			}
		} else if (!ideTipoEmissaoAtmosferica
				.equals(other.ideTipoEmissaoAtmosferica)) {
			return false;
		}
		return true;
	}
}