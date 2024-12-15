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
 * The persistent class for the fce_industria_residuo_gerado database table.
 * 
 */
@Entity
@Table(name="fce_industria_residuo_gerado")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceIndustriaResiduoGerado.removeByIdeFceIndustria", query = "DELETE FROM FceIndustriaResiduoGerado f WHERE f.ideFceIndustria.ideFceIndustria = :ideFceIndustria")})
public class FceIndustriaResiduoGerado implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceIndustriaResiduoGeradoPK ideFceIndustriaResiduoGeradoPK;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_industria", nullable = false, insertable = false, updatable = false)
	private FceIndustria ideFceIndustria;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_residuo_gerado", nullable = false, insertable = false, updatable = false)
	private TipoResiduoGerado ideTipoResiduoGerado;

	public FceIndustriaResiduoGerado() {
	}

	public FceIndustriaResiduoGerado(FceIndustriaResiduoGeradoPK ideFceIndustriaResiduoGeradoPK) {
		this.ideFceIndustriaResiduoGeradoPK = ideFceIndustriaResiduoGeradoPK;
	}

	public FceIndustriaResiduoGerado(FceIndustria ideFceIndustria,TipoResiduoGerado ideTipoResiduoGerado) {
		this.ideFceIndustriaResiduoGeradoPK = new FceIndustriaResiduoGeradoPK(ideFceIndustria, ideTipoResiduoGerado);
	}

	public FceIndustriaResiduoGeradoPK getIdeFceIndustriaResiduoGeradoPK() {
		return ideFceIndustriaResiduoGeradoPK;
	}

	public void setIdeFceIndustriaResiduoGeradoPK(
			FceIndustriaResiduoGeradoPK ideFceIndustriaResiduoGeradoPK) {
		this.ideFceIndustriaResiduoGeradoPK = ideFceIndustriaResiduoGeradoPK;
	}

	public FceIndustria getIdeFceIndustria() {
		return ideFceIndustria;
	}

	public void setIdeFceIndustria(FceIndustria ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
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
				+ ((ideFceIndustriaResiduoGeradoPK == null) ? 0
						: ideFceIndustriaResiduoGeradoPK.hashCode());
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
		FceIndustriaResiduoGerado other = (FceIndustriaResiduoGerado) obj;
		if (ideFceIndustriaResiduoGeradoPK == null) {
			if (other.ideFceIndustriaResiduoGeradoPK != null) {
				return false;
			}
		} else if (!ideFceIndustriaResiduoGeradoPK
				.equals(other.ideFceIndustriaResiduoGeradoPK)) {
			return false;
		}
		return true;
	}


}