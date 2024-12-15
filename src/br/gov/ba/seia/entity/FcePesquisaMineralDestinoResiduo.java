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
@Table(name = "fce_pesquisa_mineral_destino_residuo")
@NamedQueries({
	@NamedQuery(name = "FcePesquisaMineralDestinoResiduo.removeByIdeFcePesquisaMineral", query = "DELETE FROM FcePesquisaMineralDestinoResiduo f WHERE f.ideFcePesquisaMineral.ideFcePesquisaMineral = :ideFcePesquisaMineral")
})

public class FcePesquisaMineralDestinoResiduo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FcePesquisaMineralDestinoResiduoPK ideFcePesquisaMineralDestinoResiduoPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_pesquisa_mineral", nullable = false, insertable = false, updatable = false)
	private FcePesquisaMineral ideFcePesquisaMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_destino_residuo", nullable = false, insertable = false, updatable = false)
	private DestinoResiduo ideDestinoResiduo;

	public FcePesquisaMineralDestinoResiduo() {

	}

	public FcePesquisaMineralDestinoResiduo(FcePesquisaMineral ideFcePesquisaMineral,DestinoResiduo ideDestinoResiduo) {		
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
		this.ideDestinoResiduo = ideDestinoResiduo;
		this.ideFcePesquisaMineralDestinoResiduoPK = new FcePesquisaMineralDestinoResiduoPK(ideFcePesquisaMineral, ideDestinoResiduo);
	}
	
	public FcePesquisaMineralDestinoResiduoPK getIdeFcePesquisaMineralDestinoResiduoPK() {
		return ideFcePesquisaMineralDestinoResiduoPK;
	}

	public void setIdeFcePesquisaMineralDestinoResiduoPK(FcePesquisaMineralDestinoResiduoPK ideFcePesquisaMineralDestinoResiduoPK) {
		this.ideFcePesquisaMineralDestinoResiduoPK = ideFcePesquisaMineralDestinoResiduoPK;
	}

	public FcePesquisaMineral getIdeFcePesquisaMineral() {
		return ideFcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
	}

	public DestinoResiduo getIdeDestinoResiduo() {
		return ideDestinoResiduo;
	}

	public void setIdeDestinoResiduo(DestinoResiduo ideDestinoResiduo) {
		this.ideDestinoResiduo = ideDestinoResiduo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideDestinoResiduo == null) ? 0 : ideDestinoResiduo
						.hashCode());
		result = prime
				* result
				+ ((ideFcePesquisaMineral == null) ? 0 : ideFcePesquisaMineral
						.hashCode());
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
		FcePesquisaMineralDestinoResiduo other = (FcePesquisaMineralDestinoResiduo) obj;
		if (ideDestinoResiduo == null) {
			if (other.ideDestinoResiduo != null)
				return false;
		} else if (!ideDestinoResiduo.equals(other.ideDestinoResiduo))
			return false;
		if (ideFcePesquisaMineral == null) {
			if (other.ideFcePesquisaMineral != null)
				return false;
		} else if (!ideFcePesquisaMineral.equals(other.ideFcePesquisaMineral))
			return false;
		return true;
	}

}