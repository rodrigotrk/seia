package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_mineral_destino_residuo
 * database table.
 * 
 */
@Embeddable
public class FceLicenciamentoMineralDestinoResiduoPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	@Column(name = "ide_destino_residuo", insertable = false, updatable = false)
	private Integer ideDestinoResiduo;

	public FceLicenciamentoMineralDestinoResiduoPK() {

	}

	public FceLicenciamentoMineralDestinoResiduoPK(FceLicenciamentoMineral fceLicenciamentoMineral, DestinoResiduo destinoResiduo) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideDestinoResiduo = destinoResiduo.getIdeDestinoResiduo();
	}

	public Integer getIdeFceLicenciamentoMineral() {
		return this.ideFceLicenciamentoMineral;
	}

	public void setIdeFceLicenciamentoMineral(Integer ideFceLicenciamentoMineral) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public Integer getIdeDestinoResiduo() {
		return this.ideDestinoResiduo;
	}

	public void setIdeDestinoResiduo(Integer ideDestinoResiduo) {
		this.ideDestinoResiduo = ideDestinoResiduo;
	}

	public boolean equals(Object other) {
		if(this == other){
			return true;
		}
		if(!(other instanceof FceLicenciamentoMineralDestinoResiduoPK)){
			return false;
		}
		FceLicenciamentoMineralDestinoResiduoPK castOther = (FceLicenciamentoMineralDestinoResiduoPK) other;
		return this.ideFceLicenciamentoMineral.equals(castOther.ideFceLicenciamentoMineral) && this.ideDestinoResiduo.equals(castOther.ideDestinoResiduo);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideFceLicenciamentoMineral.hashCode();
		hash = hash * prime + this.ideDestinoResiduo.hashCode();

		return hash;
	}
}