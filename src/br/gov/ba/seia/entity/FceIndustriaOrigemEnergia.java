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
 * The persistent class for the fce_industria_origem_energia database table.
 * 
 */
@Entity
@Table(name="fce_industria_origem_energia")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceIndustriaOrigemEnergia.removeByIdeFceIndustria", query = "DELETE FROM FceIndustriaOrigemEnergia f WHERE f.ideFceIndustria.ideFceIndustria = :ideFceIndustria")})
public class FceIndustriaOrigemEnergia implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceIndustriaOrigemEnergiaPK ideFceIndustriaOrigemEnergiaPK;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_industria", nullable = false, insertable = false, updatable = false)
	private FceIndustria ideFceIndustria;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_origem_energia", nullable = false, insertable = false, updatable = false)
	private TipoOrigemEnergia ideTipoOrigemEnergia;


	public FceIndustriaOrigemEnergia() {
	}

	public FceIndustriaOrigemEnergia(FceIndustriaOrigemEnergiaPK fceIndustriaOrigemEnergiaPK) {
		this.ideFceIndustriaOrigemEnergiaPK = fceIndustriaOrigemEnergiaPK;
	}

	public FceIndustriaOrigemEnergia(FceIndustria ideFceIndustria,TipoOrigemEnergia ideTipoOrigemEnergia) {
		this.ideFceIndustriaOrigemEnergiaPK = new FceIndustriaOrigemEnergiaPK(ideFceIndustria, ideTipoOrigemEnergia);
	}

	public FceIndustriaOrigemEnergiaPK getIdeFceIndustriaOrigemEnergiaPK() {
		return ideFceIndustriaOrigemEnergiaPK;
	}

	public void setIdeFceIndustriaOrigemEnergiaPK(
			FceIndustriaOrigemEnergiaPK ideFceIndustriaOrigemEnergiaPK) {
		this.ideFceIndustriaOrigemEnergiaPK = ideFceIndustriaOrigemEnergiaPK;
	}

	public FceIndustria getIdeFceIndustria() {
		return ideFceIndustria;
	}

	public void setIdeFceIndustria(FceIndustria ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}

	public TipoOrigemEnergia getIdeTipoOrigemEnergia() {
		return ideTipoOrigemEnergia;
	}

	public void setIdeTipoOrigemEnergia(TipoOrigemEnergia ideTipoOrigemEnergia) {
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceIndustriaOrigemEnergiaPK == null) ? 0
						: ideFceIndustriaOrigemEnergiaPK.hashCode());
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
		FceIndustriaOrigemEnergia other = (FceIndustriaOrigemEnergia) obj;
		if (ideFceIndustriaOrigemEnergiaPK == null) {
			if (other.ideFceIndustriaOrigemEnergiaPK != null) {
				return false;
			}
		} else if (!ideFceIndustriaOrigemEnergiaPK
				.equals(other.ideFceIndustriaOrigemEnergiaPK)) {
			return false;
		}
		return true;
	}



}