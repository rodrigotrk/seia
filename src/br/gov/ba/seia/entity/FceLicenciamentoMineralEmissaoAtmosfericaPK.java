package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_mineral_emissao_atmosferica
 * database table.
 * 
 */
@Embeddable
public class FceLicenciamentoMineralEmissaoAtmosfericaPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_tipo_emissao_atmosferica", insertable = false, updatable = false)
	private Integer ideTipoEmissaoAtmosferica;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	public FceLicenciamentoMineralEmissaoAtmosfericaPK() {

	}

	public FceLicenciamentoMineralEmissaoAtmosfericaPK(TipoEmissaoAtmosferica ideTipoEmissaoAtmosferica, FceLicenciamentoMineral ideFceLicenciamentoMineral) {
		this.ideTipoEmissaoAtmosferica = ideTipoEmissaoAtmosferica.getIdeTipoEmissaoAtmosferica();
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
	}

	public Integer getIdeTipoEmissaoAtmosferica() {
		return this.ideTipoEmissaoAtmosferica;
	}

	public void setIdeTipoEmissaoAtmosferica(Integer ideTipoEmissaoAtmosferica) {
		this.ideTipoEmissaoAtmosferica = ideTipoEmissaoAtmosferica;
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
		if(!(other instanceof FceLicenciamentoMineralEmissaoAtmosfericaPK)){
			return false;
		}
		FceLicenciamentoMineralEmissaoAtmosfericaPK castOther = (FceLicenciamentoMineralEmissaoAtmosfericaPK) other;
		return this.ideTipoEmissaoAtmosferica.equals(castOther.ideTipoEmissaoAtmosferica) && this.ideFceLicenciamentoMineral.equals(castOther.ideFceLicenciamentoMineral);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideTipoEmissaoAtmosferica.hashCode();
		hash = hash * prime + this.ideFceLicenciamentoMineral.hashCode();

		return hash;
	}
}