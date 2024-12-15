package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_pesquisa_mineral_destino_residuo database
 * table.
 * 
 */
@Embeddable
public class FcePesquisaMineralDestinoResiduoPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_destino_residuo", insertable = false, updatable = false)
	private Integer ideDestinoResiduo;

	@Column(name = "ide_fce_pesquisa_mineral", insertable = false, updatable = false)
	private Integer ideFcePesquisaMineral;

	public FcePesquisaMineralDestinoResiduoPK() {
	}

	public FcePesquisaMineralDestinoResiduoPK(FcePesquisaMineral fcePesquisaMineral, DestinoResiduo destinoResiduo) {
		this.ideDestinoResiduo = destinoResiduo.getIdeDestinoResiduo();
		this.ideFcePesquisaMineral = fcePesquisaMineral.getIdeFcePesquisaMineral();
	}

	public Integer getIdeDestinoResiduo() {
		return this.ideDestinoResiduo;
	}

	public void setIdeDestinoResiduo(Integer ideDestinoResiduo) {
		this.ideDestinoResiduo = ideDestinoResiduo;
	}

	public Integer getIdeFcePesquisaMineral() {
		return this.ideFcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(Integer ideFcePesquisaMineral) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
	}

	public boolean equals(Object other) {
		if(this == other){
			return true;
		}
		if(!(other instanceof FcePesquisaMineralDestinoResiduoPK)){
			return false;
		}
		FcePesquisaMineralDestinoResiduoPK castOther = (FcePesquisaMineralDestinoResiduoPK) other;
		return this.ideDestinoResiduo.equals(castOther.ideDestinoResiduo) && this.ideFcePesquisaMineral.equals(castOther.ideFcePesquisaMineral);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideDestinoResiduo.hashCode();
		hash = hash * prime + this.ideFcePesquisaMineral.hashCode();

		return hash;
	}
}