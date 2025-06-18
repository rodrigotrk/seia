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
@Table(name = "fce_pesquisa_mineral_tipo_residuo")
@NamedQueries({
	@NamedQuery(name = "FcePesquisaMineralTipoResiduo.removeByideFcePesquisaMineral", query = "DELETE FROM FcePesquisaMineralTipoResiduo f WHERE f.ideFcePesquisaMineral.ideFcePesquisaMineral = :ideFcePesquisaMineral")
})

public class FcePesquisaMineralTipoResiduo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private FcePesquisaMineralTipoResiduoPK ideFcePesquisaMineralTipoResiduoPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_pesquisa_mineral", insertable = false, updatable = false, nullable = false)
	private FcePesquisaMineral ideFcePesquisaMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_residuo_gerado", insertable = false, updatable = false, nullable = false)
	private TipoResiduoGerado ideTipoResiduoGerado;

	public FcePesquisaMineralTipoResiduo() {

	}
	
	public FcePesquisaMineralTipoResiduo(FcePesquisaMineral ideFcePesquisaMineral,TipoResiduoGerado ideTipoResiduoGerado) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
		this.ideTipoResiduoGerado = ideTipoResiduoGerado;
		this.ideFcePesquisaMineralTipoResiduoPK = new FcePesquisaMineralTipoResiduoPK(ideFcePesquisaMineral,ideTipoResiduoGerado ); 
	}

	public FcePesquisaMineralTipoResiduoPK getIdeFcePesquisaMineralTipoResiduoPK() {
		return ideFcePesquisaMineralTipoResiduoPK;
	}

	public void setIdeFcePesquisaMineralTipoResiduoPK(
			FcePesquisaMineralTipoResiduoPK ideFcePesquisaMineralTipoResiduoPK) {
		this.ideFcePesquisaMineralTipoResiduoPK = ideFcePesquisaMineralTipoResiduoPK;
	}

	public FcePesquisaMineral getIdeFcePesquisaMineral() {
		return ideFcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
	}

	public TipoResiduoGerado getIdeTipoResiduoGerado() {
		return ideTipoResiduoGerado;
	}

	public void setIdeTipoResiduoGerado(TipoResiduoGerado ideTipoResiduoGerado) {
		this.ideTipoResiduoGerado = ideTipoResiduoGerado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFcePesquisaMineral == null) ? 0 : ideFcePesquisaMineral
						.hashCode());
		result = prime
				* result
				+ ((ideFcePesquisaMineralTipoResiduoPK == null) ? 0
						: ideFcePesquisaMineralTipoResiduoPK.hashCode());
		result = prime
				* result
				+ ((ideTipoResiduoGerado == null) ? 0 : ideTipoResiduoGerado
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
		FcePesquisaMineralTipoResiduo other = (FcePesquisaMineralTipoResiduo) obj;
		if (ideFcePesquisaMineral == null) {
			if (other.ideFcePesquisaMineral != null)
				return false;
		} else if (!ideFcePesquisaMineral.equals(other.ideFcePesquisaMineral))
			return false;
		if (ideFcePesquisaMineralTipoResiduoPK == null) {
			if (other.ideFcePesquisaMineralTipoResiduoPK != null)
				return false;
		} else if (!ideFcePesquisaMineralTipoResiduoPK
				.equals(other.ideFcePesquisaMineralTipoResiduoPK))
			return false;
		if (ideTipoResiduoGerado == null) {
			if (other.ideTipoResiduoGerado != null)
				return false;
		} else if (!ideTipoResiduoGerado.equals(other.ideTipoResiduoGerado))
			return false;
		return true;
	}

	

}