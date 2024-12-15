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
 * The persistent class for the fce_industria_sistema_tratamento database table.
 * 
 */
@Entity
@Table(name="fce_industria_sistema_tratamento")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceIndustriaSistemaTratamento.removeByIdeFceIndustria", query = "DELETE FROM FceIndustriaSistemaTratamento f WHERE f.ideFceIndustria.ideFceIndustria = :ideFceIndustria")})
public class FceIndustriaSistemaTratamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceIndustriaSistemaTratamentoPK ideFceIndustriaSistemaTratamentoPK;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_industria", nullable = false, insertable = false, updatable = false)
	private FceIndustria ideFceIndustria;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_sistema_tratamento", nullable = false, insertable = false, updatable = false)
	private TipoSistemaTratamento ideTipoSistemaTratamento;


	public FceIndustriaSistemaTratamento() {
	}

	public FceIndustriaSistemaTratamento(FceIndustriaSistemaTratamentoPK ideFceIndustriaSistemaTratamentoPK) {
		this.ideFceIndustriaSistemaTratamentoPK = ideFceIndustriaSistemaTratamentoPK;
	}

	public FceIndustriaSistemaTratamento(FceIndustria ideFceIndustria,TipoSistemaTratamento ideTipoSistemaTratamento) {
		this.ideFceIndustriaSistemaTratamentoPK = new FceIndustriaSistemaTratamentoPK(ideFceIndustria, ideTipoSistemaTratamento);
	}

	public FceIndustriaSistemaTratamentoPK getIdeFceIndustriaSistemaTratamentoPK() {
		return ideFceIndustriaSistemaTratamentoPK;
	}

	public void setIdeFceIndustriaSistemaTratamentoPK(
			FceIndustriaSistemaTratamentoPK ideFceIndustriaSistemaTratamentoPK) {
		this.ideFceIndustriaSistemaTratamentoPK = ideFceIndustriaSistemaTratamentoPK;
	}

	public FceIndustria getIdeFceIndustria() {
		return ideFceIndustria;
	}

	public void setIdeFceIndustria(FceIndustria ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}

	public TipoSistemaTratamento getIdeTipoSistemaTratamento() {
		return ideTipoSistemaTratamento;
	}

	public void setIdeTipoSistemaTratamento(
			TipoSistemaTratamento ideTipoSistemaTratamento) {
		this.ideTipoSistemaTratamento = ideTipoSistemaTratamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceIndustriaSistemaTratamentoPK == null) ? 0
						: ideFceIndustriaSistemaTratamentoPK.hashCode());
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
		FceIndustriaSistemaTratamento other = (FceIndustriaSistemaTratamento) obj;
		if (ideFceIndustriaSistemaTratamentoPK == null) {
			if (other.ideFceIndustriaSistemaTratamentoPK != null) {
				return false;
			}
		} else if (!ideFceIndustriaSistemaTratamentoPK
				.equals(other.ideFceIndustriaSistemaTratamentoPK)) {
			return false;
		}
		return true;
	}

}