package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "fce_pesquisa_mineral_substancia_mineral_tipologia")
@NamedQueries({
	@NamedQuery(name = "FcePesquisaMineralSubstanciaMineralTipologia.removeByIdeFcePesquisaMineral",	query = "DELETE FROM FcePesquisaMineralSubstanciaMineralTipologia fpm WHERE fpm.ideFcePesquisaMineral.ideFcePesquisaMineral = :ideFcePesquisaMineral")
})

public class FcePesquisaMineralSubstanciaMineralTipologia implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FcePesquisaMineralSubstanciaMineralTipologiaPK ideFcePesquisaMineralSubstanciaMineralPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_pesquisa_mineral", nullable = false, insertable = false, updatable = false)
	private FcePesquisaMineral ideFcePesquisaMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_substancia_mineral_tipologia", nullable = false, insertable = false, updatable = false)
	private SubstanciaMineralTipologia substanciaMineralTipologia;

	public FcePesquisaMineralSubstanciaMineralTipologia() {

	}
		
	public FcePesquisaMineralSubstanciaMineralTipologia(FcePesquisaMineralSubstanciaMineralTipologiaPK ideFcePesquisaMineralSubstanciaMineralPK) {
		this.ideFcePesquisaMineralSubstanciaMineralPK = ideFcePesquisaMineralSubstanciaMineralPK;
	}

	public FcePesquisaMineralSubstanciaMineralTipologia(FcePesquisaMineral fcePesquisaMineral, SubstanciaMineralTipologia substanciaMineral) {
		this.ideFcePesquisaMineralSubstanciaMineralPK = new FcePesquisaMineralSubstanciaMineralTipologiaPK(fcePesquisaMineral, substanciaMineral);
		this.ideFcePesquisaMineral = fcePesquisaMineral;
		this.substanciaMineralTipologia = substanciaMineral;
	}

	public FcePesquisaMineral getIdeFcePesquisaMineral() {
		return ideFcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(FcePesquisaMineral fcePesquisaMineral) {
		this.ideFcePesquisaMineral = fcePesquisaMineral;
	}

	public SubstanciaMineralTipologia getSubstanciaMineralTipologia() {
		return substanciaMineralTipologia;
	}

	public void setSubstanciaMineralTipologia(SubstanciaMineralTipologia substanciaMineral) {
		this.substanciaMineralTipologia = substanciaMineral;
	}

	
	public FcePesquisaMineralSubstanciaMineralTipologiaPK getIdeFcePesquisaMineralSubstanciaMineralPK() {
		return ideFcePesquisaMineralSubstanciaMineralPK;
	}

	public void setIdeFcePesquisaMineralSubstanciaMineralPK(FcePesquisaMineralSubstanciaMineralTipologiaPK ideFcePesquisaMineralSubstanciaMineralPK) {
		this.ideFcePesquisaMineralSubstanciaMineralPK = ideFcePesquisaMineralSubstanciaMineralPK;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime	* result + ((ideFcePesquisaMineral == null) ? 0 : ideFcePesquisaMineral.hashCode());
		result = prime	* result + ((substanciaMineralTipologia == null) ? 0 : substanciaMineralTipologia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FcePesquisaMineralSubstanciaMineralTipologia other = (FcePesquisaMineralSubstanciaMineralTipologia) obj;
		if (ideFcePesquisaMineral == null) {
			if (other.ideFcePesquisaMineral != null)
				return false;
		} else if (!ideFcePesquisaMineral.equals(other.ideFcePesquisaMineral))
			return false;
		if (substanciaMineralTipologia == null) {
			if (other.substanciaMineralTipologia != null)
				return false;
		} else if (!substanciaMineralTipologia.equals(other.substanciaMineralTipologia))
			return false;
		return true;
	}
	
	@Override
	public FcePesquisaMineralSubstanciaMineralTipologia clone() throws CloneNotSupportedException {
		return (FcePesquisaMineralSubstanciaMineralTipologia) super.clone();
	}
	
	
}