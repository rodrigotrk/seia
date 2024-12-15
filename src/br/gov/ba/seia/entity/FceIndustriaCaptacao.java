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
 * The persistent class for the fce_industria_captacao database table.
 * 
 */
@Entity
@Table(name="fce_industria_captacao")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceIndustriaCaptacao.removeByIdeFceIndustria", query = "DELETE FROM FceIndustriaCaptacao f WHERE f.ideFceIndustria.ideFceIndustria = :ideFceIndustria")})
public class FceIndustriaCaptacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceIndustriaCaptacaoPK ideFceIndustriaCaptacaoPK;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_industria", nullable = false, insertable = false, updatable = false)
	private FceIndustria ideFceIndustria;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_captacao", nullable = false, insertable = false, updatable = false)
	private TipoCaptacao ideTipoCaptacao;

	public FceIndustriaCaptacao() {
	}

	public FceIndustriaCaptacao(FceIndustriaCaptacaoPK ideFceIndustriaCaptacaoPK) {
		this.ideFceIndustriaCaptacaoPK = ideFceIndustriaCaptacaoPK;
	}

	public FceIndustriaCaptacao(FceIndustria ideFceIndustria, TipoCaptacao ideTipoCaptacao) {
		this.ideFceIndustriaCaptacaoPK = new FceIndustriaCaptacaoPK(ideFceIndustria, ideTipoCaptacao);
	}

	public FceIndustriaCaptacaoPK getIdeFceIndustriaCaptacaoPK() {
		return ideFceIndustriaCaptacaoPK;
	}

	public void setIdeFceIndustriaCaptacaoPK(
			FceIndustriaCaptacaoPK ideFceIndustriaCaptacaoPK) {
		this.ideFceIndustriaCaptacaoPK = ideFceIndustriaCaptacaoPK;
	}

	public FceIndustria getIdeFceIndustria() {
		return ideFceIndustria;
	}

	public void setIdeFceIndustria(FceIndustria ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}

	public TipoCaptacao getIdeTipoCaptacao() {
		return ideTipoCaptacao;
	}

	public void setIdeTipoCaptacao(TipoCaptacao ideTipoCaptacao) {
		this.ideTipoCaptacao = ideTipoCaptacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceIndustriaCaptacaoPK == null) ? 0
						: ideFceIndustriaCaptacaoPK.hashCode());
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
		FceIndustriaCaptacao other = (FceIndustriaCaptacao) obj;
		if (ideFceIndustriaCaptacaoPK == null) {
			if (other.ideFceIndustriaCaptacaoPK != null) {
				return false;
			}
		} else if (!ideFceIndustriaCaptacaoPK
				.equals(other.ideFceIndustriaCaptacaoPK)) {
			return false;
		}
		return true;
	}


}