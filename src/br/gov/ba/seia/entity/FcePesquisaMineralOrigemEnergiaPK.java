package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FcePesquisaMineralOrigemEnergiaPK implements Serializable {
	
	private static final long serialVersionUID = 1L;


	@Column(name = "ide_tipo_origem_energia", nullable = false, insertable = false, updatable = false)
	private Integer ideTipoOrigemEnergia;

	@Column(name = "ide_fce_pesquisa_mineral", nullable = false, insertable = false, updatable = false)
	private Integer ideFcePesquisaMineral;

	public FcePesquisaMineralOrigemEnergiaPK() {
	}
	
	public FcePesquisaMineralOrigemEnergiaPK(Integer ideFcePesquisaMineral, Integer ideTipoOrigemEnergia) {
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia;
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
	}
	
	public Integer getIdeTipoOrigemEnergia() {
		return ideTipoOrigemEnergia;
	}

	public void setIdeTipoOrigemEnergia(Integer ideTipoOrigemEnergia) {
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia;
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
		result = prime * result	+ ((ideFcePesquisaMineral == null) ? 0 : ideFcePesquisaMineral.hashCode());
		result = prime * result+ ((ideTipoOrigemEnergia == null) ? 0 : ideTipoOrigemEnergia.hashCode());
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
		FcePesquisaMineralOrigemEnergiaPK other = (FcePesquisaMineralOrigemEnergiaPK) obj;
		if (ideFcePesquisaMineral == null) {
			if (other.ideFcePesquisaMineral != null)
				return false;
		} else if (!ideFcePesquisaMineral.equals(other.ideFcePesquisaMineral))
			return false;
		if (ideTipoOrigemEnergia == null) {
			if (other.ideTipoOrigemEnergia != null)
				return false;
		} else if (!ideTipoOrigemEnergia.equals(other.ideTipoOrigemEnergia))
			return false;
		return true;
	}



}