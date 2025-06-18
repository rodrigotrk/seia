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
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "fce_pesquisa_prospeccao_geofisica")
@NamedQueries({
	@NamedQuery(name = "FcePesquisaMineralProspeccaoGeofisica.removeByIdeFcePesquisaMineralProspeccao", query = "DELETE FROM FcePesquisaMineralProspeccaoGeofisica fpmp WHERE fpmp.ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao = :ideFcePesquisaMineralProspeccao")
})

public class FcePesquisaMineralProspeccaoGeofisica implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FcePesquisaMineralProspeccaoGeofisicaPK ideFcePesquisaMineralProspeccaoGeofisicaPK;

	@NotNull
	@JoinColumn(name = "ide_geofisica", referencedColumnName = "ide_geofisica", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Geofisica ideGeofisica;

	@NotNull
	@JoinColumn(name = "ide_fce_pesquisa_mineral_prospeccao", referencedColumnName = "ide_fce_pesquisa_mineral_prospeccao", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao;
	
	public FcePesquisaMineralProspeccaoGeofisica() {
	}

	public FcePesquisaMineralProspeccaoGeofisica(Geofisica ideGeofisica,FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao) {
		this.ideGeofisica = ideGeofisica;
		this.ideFcePesquisaMineralProspeccao = ideFcePesquisaMineralProspeccao;
		this.ideFcePesquisaMineralProspeccaoGeofisicaPK = new FcePesquisaMineralProspeccaoGeofisicaPK (ideGeofisica.getIdeGeofisica(), ideFcePesquisaMineralProspeccao.getIdeFcePesquisaMineralProspeccao());
	}
	
	public FcePesquisaMineralProspeccaoGeofisica(FcePesquisaMineralProspeccaoGeofisicaPK fcePesquisaMineralProspeccaoGeofisicaPK){
		this.ideFcePesquisaMineralProspeccaoGeofisicaPK = fcePesquisaMineralProspeccaoGeofisicaPK;
	}
	
	public Geofisica getIdeGeofisica() {
		return ideGeofisica;
	}

	public void setIdeGeofisica(Geofisica ideGeofisica) {
		this.ideGeofisica = ideGeofisica;
	}

	public FcePesquisaMineralProspeccao getIdeFcePesquisaMineralProspeccao() {
		return ideFcePesquisaMineralProspeccao;
	}

	public void setIdeFcePesquisaMineralProspeccao(	FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao) {
		this.ideFcePesquisaMineralProspeccao = ideFcePesquisaMineralProspeccao;
	}

	public FcePesquisaMineralProspeccaoGeofisicaPK getIdeFcePesquisaMineralProspeccaoGeofisicaPK() {
		return ideFcePesquisaMineralProspeccaoGeofisicaPK;
	}

	public void setIdeFcePesquisaMineralProspeccaoGeofisicaPK(FcePesquisaMineralProspeccaoGeofisicaPK ideFcePesquisaMineralProspeccaoGeofisicaPK) {
		this.ideFcePesquisaMineralProspeccaoGeofisicaPK = ideFcePesquisaMineralProspeccaoGeofisicaPK;
	}

	
	

}