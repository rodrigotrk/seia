package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_mineral_substancia_mineral
 * database table.
 * 
 */
@Embeddable
public class FceLicenciamentoMineralSubstanciaMineralTipologiaPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_substancia_mineral_tipologia", insertable = false, updatable = false)
	private Integer ideSubstanciaMineralTipologia;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	public FceLicenciamentoMineralSubstanciaMineralTipologiaPK() {
	}

	public FceLicenciamentoMineralSubstanciaMineralTipologiaPK(FceLicenciamentoMineral fceLicenciamentoMineral, SubstanciaMineralTipologia substanciaMineralTipologia) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideSubstanciaMineralTipologia = substanciaMineralTipologia.getIdeSubstanciaMineralTipologia();
	}

	public Integer getIdeSubstanciaMineralTipologia() {
		return this.ideSubstanciaMineralTipologia;
	}

	public void setIdeSubstanciaMineralTipologia(Integer ideSubstanciaMineral) {
		this.ideSubstanciaMineralTipologia = ideSubstanciaMineral;
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
		if(!(other instanceof FceLicenciamentoMineralSubstanciaMineralTipologiaPK)){
			return false;
		}
		FceLicenciamentoMineralSubstanciaMineralTipologiaPK castOther = (FceLicenciamentoMineralSubstanciaMineralTipologiaPK) other;
		return this.ideSubstanciaMineralTipologia.equals(castOther.ideSubstanciaMineralTipologia) && this.ideFceLicenciamentoMineral.equals(castOther.ideFceLicenciamentoMineral);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideSubstanciaMineralTipologia.hashCode();
		hash = hash * prime + this.ideFceLicenciamentoMineral.hashCode();

		return hash;
	}
}