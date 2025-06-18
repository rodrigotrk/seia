package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FcePesquisaMineralProspeccaoGeofisicaPK implements Serializable {
		
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_geofisica", insertable = false, updatable = false)
	private Integer ideGeofisica;

	@Column(name = "ide_fce_pesquisa_mineral_prospeccao", insertable = false, updatable = false)
	private Integer ideFcePesquisaMineralProspeccao;

	public FcePesquisaMineralProspeccaoGeofisicaPK() {
	}

	public FcePesquisaMineralProspeccaoGeofisicaPK(Integer ideGeofisica,Integer ideFcePesquisaMineralProspeccao) {
		this.ideGeofisica = ideGeofisica;
		this.ideFcePesquisaMineralProspeccao = ideFcePesquisaMineralProspeccao;
	}

	public Integer getIdeGeofisica() {
		return ideGeofisica;
	}

	public void setIdeGeofisica(Integer ideGeofisica) {
		this.ideGeofisica = ideGeofisica;
	}

	public Integer getIdeFcePesquisaMineralProspeccao() {
		return ideFcePesquisaMineralProspeccao;
	}

	public void setIdeFcePesquisaMineralProspeccao(Integer ideFcePesquisaMineralProspeccao) {
		this.ideFcePesquisaMineralProspeccao = ideFcePesquisaMineralProspeccao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime	* result+ ((ideFcePesquisaMineralProspeccao == null) ? 0: ideFcePesquisaMineralProspeccao.hashCode());
		result = prime * result	+ ((ideGeofisica == null) ? 0 : ideGeofisica.hashCode());
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
		FcePesquisaMineralProspeccaoGeofisicaPK other = (FcePesquisaMineralProspeccaoGeofisicaPK) obj;
		if (ideFcePesquisaMineralProspeccao == null) {
			if (other.ideFcePesquisaMineralProspeccao != null)
				return false;
		} else if (!ideFcePesquisaMineralProspeccao	.equals(other.ideFcePesquisaMineralProspeccao))
			return false;
		if (ideGeofisica == null) {
			if (other.ideGeofisica != null)
				return false;
		} else if (!ideGeofisica.equals(other.ideGeofisica))
			return false;
		return true;
	}
}