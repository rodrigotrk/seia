package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_mineral_tipo_app database
 * table.
 * 
 */
@Embeddable
public class FceLicenciamentoMineralTipoAppPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_tipo_app", insertable = false, updatable = false)
	private Integer ideTipoApp;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	public FceLicenciamentoMineralTipoAppPK() {
	}

	public FceLicenciamentoMineralTipoAppPK(FceLicenciamentoMineral fceLicenciamentoMineral, TipoApp tipoApp) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideTipoApp = tipoApp.getIdeTipoApp();
	}

	public Integer getIdeTipoApp() {
		return this.ideTipoApp;
	}

	public void setIdeTipoApp(Integer ideTipoApp) {
		this.ideTipoApp = ideTipoApp;
	}

	public Integer getIdeFceLicenciamentoMineral() {
		return this.ideFceLicenciamentoMineral;
	}

	public void setIdeFceLicenciamentoMineral(Integer ideFceLicenciamentoMineral) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public boolean equals(Object other) {
		if(this == other){
			return true;
		}
		if(!(other instanceof FceLicenciamentoMineralTipoAppPK)){
			return false;
		}
		FceLicenciamentoMineralTipoAppPK castOther = (FceLicenciamentoMineralTipoAppPK) other;
		return this.ideTipoApp.equals(castOther.ideTipoApp) && this.ideFceLicenciamentoMineral.equals(castOther.ideFceLicenciamentoMineral);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideTipoApp.hashCode();
		hash = hash * prime + this.ideFceLicenciamentoMineral.hashCode();

		return hash;
	}
}