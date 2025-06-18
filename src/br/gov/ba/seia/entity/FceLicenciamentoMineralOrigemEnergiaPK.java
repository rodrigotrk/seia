package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_mineral_origem_energia
 * database table.
 * 
 */
@Embeddable
public class FceLicenciamentoMineralOrigemEnergiaPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_tipo_origem_energia", insertable = false, updatable = false)
	private Integer ideTipoOrigemEnergia;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	public FceLicenciamentoMineralOrigemEnergiaPK() {
	}

	public FceLicenciamentoMineralOrigemEnergiaPK(FceLicenciamentoMineral fceLicenciamentoMineral, TipoOrigemEnergia ideTipoOrigemEnergia) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia.getIdeTipoOrigemEnergia();
	}

	public Integer getIdeTipoOrigemEnergia() {
		return this.ideTipoOrigemEnergia;
	}

	public void setIdeTipoOrigemEnergia(Integer ideTipoOrigemEnergia) {
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia;
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
		if(!(other instanceof FceLicenciamentoMineralOrigemEnergiaPK)){
			return false;
		}
		FceLicenciamentoMineralOrigemEnergiaPK castOther = (FceLicenciamentoMineralOrigemEnergiaPK) other;
		return this.ideTipoOrigemEnergia.equals(castOther.ideTipoOrigemEnergia) && this.ideFceLicenciamentoMineral.equals(castOther.ideFceLicenciamentoMineral);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideTipoOrigemEnergia.hashCode();
		hash = hash * prime + this.ideFceLicenciamentoMineral.hashCode();

		return hash;
	}
}