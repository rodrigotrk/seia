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
 * The persistent class for the fce_industria_residuo_civil database table.
 * 
 */
@Entity
@Table(name="fce_industria_residuo_civil")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceIndustriaResiduoCivil.removeByIdeFceIndustria", query = "DELETE FROM FceIndustriaResiduoCivil f WHERE f.ideFceIndustria.ideFceIndustria = :ideFceIndustria")})
public class FceIndustriaResiduoCivil implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceIndustriaResiduoCivilPK ideFceIndustriaResiduoCivilPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_fce_industria", nullable = false, insertable = false, updatable = false)
	private FceIndustria ideFceIndustria;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_tipo_residuo_cons_civil", nullable = false, insertable = false, updatable = false)
	private TipoResiduoConsCivil ideTipoResiduoConsCivil;

	public FceIndustriaResiduoCivil() {
	}

	public FceIndustriaResiduoCivil(FceIndustriaResiduoCivilPK ideFceIndustriaResiduoCivilPK) {
		this.ideFceIndustriaResiduoCivilPK = ideFceIndustriaResiduoCivilPK;
	}

	public FceIndustriaResiduoCivil(FceIndustria ideFceIndustria,TipoResiduoConsCivil ideTipoResiduoConsCivil) {
		this.ideFceIndustriaResiduoCivilPK = new FceIndustriaResiduoCivilPK(ideFceIndustria, ideTipoResiduoConsCivil);
	}

	public FceIndustriaResiduoCivilPK getIdeFceIndustriaResiduoCivilPK() {
		return ideFceIndustriaResiduoCivilPK;
	}

	public void setIdeFceIndustriaResiduoCivilPK(FceIndustriaResiduoCivilPK ideFceIndustriaResiduoCivilPK) {
		this.ideFceIndustriaResiduoCivilPK = ideFceIndustriaResiduoCivilPK;
	}

	public FceIndustria getIdeFceIndustria() {
		return ideFceIndustria;
	}

	public void setIdeFceIndustria(FceIndustria ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}

	public TipoResiduoConsCivil getIdeTipoResiduoConsCivil() {
		return ideTipoResiduoConsCivil;
	}

	public void setIdeTipoResiduoConsCivil(
			TipoResiduoConsCivil ideTipoResiduoConsCivil) {
		this.ideTipoResiduoConsCivil = ideTipoResiduoConsCivil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceIndustriaResiduoCivilPK == null) ? 0
						: ideFceIndustriaResiduoCivilPK.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FceIndustriaResiduoCivil other = (FceIndustriaResiduoCivil) obj;
		if (ideFceIndustriaResiduoCivilPK == null) {
			if (other.ideFceIndustriaResiduoCivilPK != null) {
				return false;
			}
		} else if (!ideFceIndustriaResiduoCivilPK
				.equals(other.ideFceIndustriaResiduoCivilPK)) {
			return false;
		}
		return true;
	}


}