package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FcePesquisaMineralSubstanciaMineralTipologiaPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_substancia_mineral_tipologia", insertable = false, updatable = false)
	private Integer ideSubstanciaMineralTipologia;

	@Column(name = "ide_fce_pesquisa_mineral", insertable = false, updatable = false)
	private Integer ideFcePesquisaMineral;

	public FcePesquisaMineralSubstanciaMineralTipologiaPK() {
	}

	public FcePesquisaMineralSubstanciaMineralTipologiaPK(FcePesquisaMineral fcePesquisaMineral, SubstanciaMineralTipologia substanciaMineralTipologia) {
		this.ideFcePesquisaMineral = fcePesquisaMineral.getIdeFcePesquisaMineral();
		this.ideSubstanciaMineralTipologia = substanciaMineralTipologia.getIdeSubstanciaMineralTipologia();
	}

	public Integer getIdeSubstanciaMineralTipologia() {
		return this.ideSubstanciaMineralTipologia;
	}

	public void setIdeSubstanciaMineralTipologia(Integer ideSubstanciaMineral) {
		this.ideSubstanciaMineralTipologia = ideSubstanciaMineral;
	}

	public Integer getIdeFcePesquisaMineral() {
		return ideFcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(Integer ideFcePesquisaMineral) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFcePesquisaMineral == null) ? 0 : ideFcePesquisaMineral.hashCode());
		result = prime * result + ((ideSubstanciaMineralTipologia == null) ? 0 : ideSubstanciaMineralTipologia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		FcePesquisaMineralSubstanciaMineralTipologiaPK other = (FcePesquisaMineralSubstanciaMineralTipologiaPK) obj;
		if(ideFcePesquisaMineral == null){
			if(other.ideFcePesquisaMineral != null)
				return false;
		}
		else if(!ideFcePesquisaMineral.equals(other.ideFcePesquisaMineral))
			return false;
		if(ideSubstanciaMineralTipologia == null){
			if(other.ideSubstanciaMineralTipologia != null)
				return false;
		}
		else if(!ideSubstanciaMineralTipologia.equals(other.ideSubstanciaMineralTipologia))
			return false;
		return true;
	}

}