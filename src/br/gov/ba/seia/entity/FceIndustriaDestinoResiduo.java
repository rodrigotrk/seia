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
 * The persistent class for the fce_industria_destino_residuo database table.
 * 
 */
@Entity
@Table(name="fce_industria_destino_residuo")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceIndustriaDestinoResiduo.removeByIdeFceIndustria", query = "DELETE FROM FceIndustriaDestinoResiduo f WHERE f.ideFceIndustria.ideFceIndustria = :ideFceIndustria")})
public class FceIndustriaDestinoResiduo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceIndustriaDestinoResiduoPK ideFceIndustriaDestinoResiduoPK;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_destino_residuo", nullable = false, insertable = false, updatable = false)
	private DestinoResiduo ideDestinoResiduo;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_industria", nullable = false, insertable = false, updatable = false)
	private FceIndustria ideFceIndustria;

	public FceIndustriaDestinoResiduo() {
	}

	public FceIndustriaDestinoResiduo(FceIndustriaDestinoResiduoPK fceIndustriaDestinoResiduoPK) {
		this.ideFceIndustriaDestinoResiduoPK = fceIndustriaDestinoResiduoPK;
	}

	public FceIndustriaDestinoResiduo(FceIndustria ideFceIndustria, DestinoResiduo ideDestinoResiduo) {
		this.ideFceIndustriaDestinoResiduoPK = new FceIndustriaDestinoResiduoPK(ideFceIndustria, ideDestinoResiduo);
	}

	public FceIndustriaDestinoResiduoPK getIdeFceIndustriaDestinoResiduoPK() {
		return ideFceIndustriaDestinoResiduoPK;
	}

	public void setIdeFceIndustriaDestinoResiduoPK(FceIndustriaDestinoResiduoPK ideFceIndustriaDestinoResiduoPK) {
		this.ideFceIndustriaDestinoResiduoPK = ideFceIndustriaDestinoResiduoPK;
	}

	public DestinoResiduo getIdeDestinoResiduo() {
		return ideDestinoResiduo;
	}

	public void setIdeDestinoResiduo(DestinoResiduo ideDestinoResiduo) {
		this.ideDestinoResiduo = ideDestinoResiduo;
	}

	public FceIndustria getIdeFceIndustria() {
		return ideFceIndustria;
	}

	public void setIdeFceIndustria(FceIndustria ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceIndustriaDestinoResiduoPK == null) ? 0
						: ideFceIndustriaDestinoResiduoPK.hashCode());
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
		FceIndustriaDestinoResiduo other = (FceIndustriaDestinoResiduo) obj;
		if (ideFceIndustriaDestinoResiduoPK == null) {
			if (other.ideFceIndustriaDestinoResiduoPK != null) {
				return false;
			}
		} else if (!ideFceIndustriaDestinoResiduoPK
				.equals(other.ideFceIndustriaDestinoResiduoPK)) {
			return false;
		}
		return true;
	}
}