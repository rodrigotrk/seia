package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_mineral_tipo_residuo database
 * table.
 * 
 */
@Embeddable
public class FceLicenciamentoMineralTipoResiduoPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_tipo_residuo_gerado", insertable = false, updatable = false)
	private Integer ideTipoResiduoGerado;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	public FceLicenciamentoMineralTipoResiduoPK() {
	}

	public FceLicenciamentoMineralTipoResiduoPK(FceLicenciamentoMineral fceLicenciamentoMineral, TipoResiduoGerado tipoResiduoGerado) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideTipoResiduoGerado = tipoResiduoGerado.getIdeTipoResiduoGerado();
	}

	public Integer getIdeTipoResiduoGerado() {
		return this.ideTipoResiduoGerado;
	}

	public void setIdeTipoResiduoGerado(Integer ideTipoResiduoGerado) {
		this.ideTipoResiduoGerado = ideTipoResiduoGerado;
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
		if(!(other instanceof FceLicenciamentoMineralTipoResiduoPK)){
			return false;
		}
		FceLicenciamentoMineralTipoResiduoPK castOther = (FceLicenciamentoMineralTipoResiduoPK) other;
		return this.ideTipoResiduoGerado.equals(castOther.ideTipoResiduoGerado) && this.ideFceLicenciamentoMineral.equals(castOther.ideFceLicenciamentoMineral);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideTipoResiduoGerado.hashCode();
		hash = hash * prime + this.ideFceLicenciamentoMineral.hashCode();

		return hash;
	}
}