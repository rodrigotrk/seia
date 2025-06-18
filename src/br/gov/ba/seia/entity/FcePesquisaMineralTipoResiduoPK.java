package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FcePesquisaMineralTipoResiduoPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_tipo_residuo_gerado", insertable = false, updatable = false, nullable = false)
	private Integer ideTipoResiduoGerado;

	@Column(name = "ide_fce_pesquisa_mineral", insertable = false, updatable = false, nullable = false)
	private Integer ideFcePesquisaMineral;

	public FcePesquisaMineralTipoResiduoPK() {
	}

	public FcePesquisaMineralTipoResiduoPK(FcePesquisaMineral fcePesquisaMineral, TipoResiduoGerado tipoResiduoGerado) {
		this.ideFcePesquisaMineral = fcePesquisaMineral.getIdeFcePesquisaMineral();
		this.ideTipoResiduoGerado = tipoResiduoGerado.getIdeTipoResiduoGerado();
	}

	public Integer getIdeTipoResiduoGerado() {
		return this.ideTipoResiduoGerado;
	}

	public void setIdeTipoResiduoGerado(Integer ideTipoResiduoGerado) {
		this.ideTipoResiduoGerado = ideTipoResiduoGerado;
	}

	public Integer getIdeFcePesquisaMineral() {
		return this.ideFcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(Integer ideFcePesquisaMineral) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime	* result+ ((ideTipoResiduoGerado == null) ? 0 : ideTipoResiduoGerado.hashCode());
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
		FcePesquisaMineralTipoResiduoPK other = (FcePesquisaMineralTipoResiduoPK) obj;
		if (ideTipoResiduoGerado == null) {
			if (other.ideTipoResiduoGerado != null)
				return false;
		} else if (!ideTipoResiduoGerado.equals(other.ideTipoResiduoGerado))
			return false;
		return true;
	}


}