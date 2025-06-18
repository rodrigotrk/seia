package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_industria_tipo_app database table.
 * 
 */
@Embeddable
public class FceIndustriaTipoAppPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_industria", insertable=false, updatable=false)
	private Integer ideFceIndustria;

	@Column(name="ide_tipo_app", insertable=false, updatable=false)
	private Integer ideTipoApp;

	public FceIndustriaTipoAppPK() {
	}
	
	public FceIndustriaTipoAppPK(Integer ideFceIndustria, Integer ideTipoApp) {
		this.ideFceIndustria = ideFceIndustria;
		this.ideTipoApp = ideTipoApp;
	}

	public Integer getIdeFceIndustria() {
		return this.ideFceIndustria;
	}
	public void setIdeFceIndustria(Integer ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}
	public Integer getIdeTipoApp() {
		return this.ideTipoApp;
	}
	public void setIdeTipoApp(Integer ideTipoApp) {
		this.ideTipoApp = ideTipoApp;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FceIndustriaTipoAppPK)) {
			return false;
		}
		FceIndustriaTipoAppPK castOther = (FceIndustriaTipoAppPK)other;
		return 
			this.ideFceIndustria.equals(castOther.ideFceIndustria)
			&& this.ideTipoApp.equals(castOther.ideTipoApp);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideFceIndustria.hashCode();
		hash = hash * prime + this.ideTipoApp.hashCode();
		
		return hash;
	}
}