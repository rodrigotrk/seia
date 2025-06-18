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
@Table(name = "fce_pesquisa_mineral_origem_energia")
@NamedQueries({
	@NamedQuery(name = "FcePesquisaMineralOrigemEnergia.removeByIdeFcePesquisaMineral", query = "DELETE FROM FcePesquisaMineralOrigemEnergia fmoe WHERE fmoe.ideFcePesquisaMineral.ideFcePesquisaMineral = :ideFcePesquisaMineral")
})
public class FcePesquisaMineralOrigemEnergia implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FcePesquisaMineralOrigemEnergiaPK ideFcePesquisaMineralOrigemEnergiaPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_pesquisa_mineral", nullable = false, insertable = false, updatable = false)
	private FcePesquisaMineral ideFcePesquisaMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_origem_energia", nullable = false, insertable = false, updatable = false)
	private TipoOrigemEnergia ideTipoOrigemEnergia;

	public FcePesquisaMineralOrigemEnergia() {
	}
	
	public FcePesquisaMineralOrigemEnergia(FcePesquisaMineralOrigemEnergiaPK ideFcePesquisaMineralOrigemEnergiaPK) {
		this.ideFcePesquisaMineralOrigemEnergiaPK = ideFcePesquisaMineralOrigemEnergiaPK;
	}
	
	public FcePesquisaMineralOrigemEnergia(FcePesquisaMineral fcePesquisaMineral, TipoOrigemEnergia tipoOrigemEnergia) {
		this.ideFcePesquisaMineral = fcePesquisaMineral;
		this.ideTipoOrigemEnergia = tipoOrigemEnergia;
	}
	
	public FcePesquisaMineral getIdeFcePesquisaMineral() {
		return ideFcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
	}

	public TipoOrigemEnergia getIdeTipoOrigemEnergia() {
		return ideTipoOrigemEnergia;
	}

	public void setIdeTipoOrigemEnergia(TipoOrigemEnergia tipoOrigemEnergia) {
		this.ideTipoOrigemEnergia = tipoOrigemEnergia;
	}

	public FcePesquisaMineralOrigemEnergiaPK getIdeFcePesquisaMineralOrigemEnergiaPK() {
		return ideFcePesquisaMineralOrigemEnergiaPK;
	}

	public void setIdeFcePesquisaMineralOrigemEnergiaPK(FcePesquisaMineralOrigemEnergiaPK ideFcePesquisaMineralOrigemEnergiaPK) {
		this.ideFcePesquisaMineralOrigemEnergiaPK = ideFcePesquisaMineralOrigemEnergiaPK;
	}

	public FcePesquisaMineralOrigemEnergia clone() throws CloneNotSupportedException {
		return (FcePesquisaMineralOrigemEnergia) super.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFcePesquisaMineralOrigemEnergiaPK == null) ? 0
						: ideFcePesquisaMineralOrigemEnergiaPK.hashCode());
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
		FcePesquisaMineralOrigemEnergia other = (FcePesquisaMineralOrigemEnergia) obj;
		if (ideFcePesquisaMineralOrigemEnergiaPK == null) {
			if (other.ideFcePesquisaMineralOrigemEnergiaPK != null)
				return false;
		} else if (!ideFcePesquisaMineralOrigemEnergiaPK
				.equals(other.ideFcePesquisaMineralOrigemEnergiaPK))
			return false;
		return true;
	}
	
	

	
	
	
	
}