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
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the fce_industria_tipo_app database table.
 * 
 */
@Entity
@Table(name="fce_industria_tipo_app")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name="FceIndustriaTipoApp.findAll", query="SELECT f FROM FceIndustriaTipoApp f"),
	@NamedQuery(name = "FceIndustriaTipoApp.removeByIdeFceIndustria", query = "DELETE FROM FceIndustriaTipoApp f WHERE f.ideFceIndustria.ideFceIndustria = :ideFceIndustria")})
public class FceIndustriaTipoApp implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceIndustriaTipoAppPK ideFceIndustriaTipoAppPK;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_industria", nullable = false, insertable = false, updatable = false)
	private FceIndustria ideFceIndustria;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_app", nullable = false, insertable = false, updatable = false)
	private TipoApp ideTipoApp;
	

	public FceIndustriaTipoApp() {
	}


	public FceIndustriaTipoApp(FceIndustriaTipoAppPK ideFceIndustriaTipoAppPK) {
		this.ideFceIndustriaTipoAppPK = ideFceIndustriaTipoAppPK;
	}


	public FceIndustriaTipoApp(FceIndustria ideFceIndustria, TipoApp ideTipoApp) {
		this.ideFceIndustriaTipoAppPK = new FceIndustriaTipoAppPK(ideFceIndustria.getIdeFceIndustria(), ideTipoApp.getIdeTipoApp());
	}


	public FceIndustriaTipoAppPK getIdeFceIndustriaTipoAppPK() {
		return ideFceIndustriaTipoAppPK;
	}


	public void setIdeFceIndustriaTipoAppPK(
			FceIndustriaTipoAppPK ideFceIndustriaTipoAppPK) {
		this.ideFceIndustriaTipoAppPK = ideFceIndustriaTipoAppPK;
	}


	public FceIndustria getIdeFceIndustria() {
		return ideFceIndustria;
	}


	public void setIdeFceIndustria(FceIndustria ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}


	public TipoApp getIdeTipoApp() {
		return ideTipoApp;
	}


	public void setIdeTipoApp(TipoApp ideTipoApp) {
		this.ideTipoApp = ideTipoApp;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceIndustriaTipoAppPK == null) ? 0
						: ideFceIndustriaTipoAppPK.hashCode());
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
		FceIndustriaTipoApp other = (FceIndustriaTipoApp) obj;
		if (ideFceIndustriaTipoAppPK == null) {
			if (other.ideFceIndustriaTipoAppPK != null)
				return false;
		} else if (!ideFceIndustriaTipoAppPK
				.equals(other.ideFceIndustriaTipoAppPK))
			return false;
		return true;
	}

}