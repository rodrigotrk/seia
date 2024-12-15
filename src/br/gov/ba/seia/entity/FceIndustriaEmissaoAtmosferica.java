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
 * The persistent class for the fce_industria_emissao_atmosferica database table.
 * 
 */
@Entity
@Table(name="fce_industria_emissao_atmosferica")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceIndustriaEmissaoAtmosferica.removeByIdeFceIndustria", query = "DELETE FROM FceIndustriaEmissaoAtmosferica f WHERE f.ideFceIndustria.ideFceIndustria = :ideFceIndustria")})
public class FceIndustriaEmissaoAtmosferica implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceIndustriaEmissaoAtmosfericaPK ideFceIndustriaEmissaoAtmosfericaPK;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_industria", nullable = false, insertable = false, updatable = false)
	private FceIndustria ideFceIndustria;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_emissao_atmosferica", nullable = false, insertable = false, updatable = false)
	private TipoEmissaoAtmosferica ideTipoEmissaoAtmosferica;

	public FceIndustriaEmissaoAtmosferica() {
	}

	public FceIndustriaEmissaoAtmosferica(FceIndustriaEmissaoAtmosfericaPK ideFceIndustriaEmissaoAtmosfericaPK) {
		this.ideFceIndustriaEmissaoAtmosfericaPK = ideFceIndustriaEmissaoAtmosfericaPK;
	}

	public FceIndustriaEmissaoAtmosferica(FceIndustria ideFceIndustria,TipoEmissaoAtmosferica ideTipoEmissaoAtmosferica) {
		this.ideFceIndustriaEmissaoAtmosfericaPK = new FceIndustriaEmissaoAtmosfericaPK(ideFceIndustria, ideTipoEmissaoAtmosferica);
	}

	public FceIndustriaEmissaoAtmosfericaPK getIdeFceIndustriaEmissaoAtmosfericaPK() {
		return ideFceIndustriaEmissaoAtmosfericaPK;
	}

	public void setIdeFceIndustriaEmissaoAtmosfericaPK(
			FceIndustriaEmissaoAtmosfericaPK ideFceIndustriaEmissaoAtmosfericaPK) {
		this.ideFceIndustriaEmissaoAtmosfericaPK = ideFceIndustriaEmissaoAtmosfericaPK;
	}

	public FceIndustria getIdeFceIndustria() {
		return ideFceIndustria;
	}

	public void setIdeFceIndustria(FceIndustria ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}

	public TipoEmissaoAtmosferica getIdeTipoEmissaoAtmosferica() {
		return ideTipoEmissaoAtmosferica;
	}

	public void setIdeTipoEmissaoAtmosferica(
			TipoEmissaoAtmosferica ideTipoEmissaoAtmosferica) {
		this.ideTipoEmissaoAtmosferica = ideTipoEmissaoAtmosferica;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceIndustriaEmissaoAtmosfericaPK == null) ? 0
						: ideFceIndustriaEmissaoAtmosfericaPK.hashCode());
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
		FceIndustriaEmissaoAtmosferica other = (FceIndustriaEmissaoAtmosferica) obj;
		if (ideFceIndustriaEmissaoAtmosfericaPK == null) {
			if (other.ideFceIndustriaEmissaoAtmosfericaPK != null) {
				return false;
			}
		} else if (!ideFceIndustriaEmissaoAtmosfericaPK
				.equals(other.ideFceIndustriaEmissaoAtmosfericaPK)) {
			return false;
		}
		return true;
	}

}