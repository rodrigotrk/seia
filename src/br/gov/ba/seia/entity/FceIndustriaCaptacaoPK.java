package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_industria_captacao database table.
 * 
 */
@Embeddable
public class FceIndustriaCaptacaoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_industria")
	private Integer ideFceIndustria;

	@Column(name="ide_tipo_captacao")
	private Integer ideTipoCaptacao;

	public FceIndustriaCaptacaoPK() {
	}

	public FceIndustriaCaptacaoPK(FceIndustria fceIndustria, TipoCaptacao tipoCaptacao){
		ideFceIndustria = fceIndustria.getIdeFceIndustria();
		ideTipoCaptacao = tipoCaptacao.getIdeTipoCaptacao();
	}

	public Integer getIdeFceIndustria() {
		return ideFceIndustria;
	}
	public void setIdeFceIndustria(Integer ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}
	public Integer getIdeTipoCaptacao() {
		return ideTipoCaptacao;
	}
	public void setIdeTipoCaptacao(Integer ideTipoCaptacao) {
		this.ideTipoCaptacao = ideTipoCaptacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceIndustria == null) ? 0 : ideFceIndustria.hashCode());
		result = prime * result
				+ ((ideTipoCaptacao == null) ? 0 : ideTipoCaptacao.hashCode());
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
		FceIndustriaCaptacaoPK other = (FceIndustriaCaptacaoPK) obj;
		if (ideFceIndustria == null) {
			if (other.ideFceIndustria != null) {
				return false;
			}
		} else if (!ideFceIndustria.equals(other.ideFceIndustria)) {
			return false;
		}
		if (ideTipoCaptacao == null) {
			if (other.ideTipoCaptacao != null) {
				return false;
			}
		} else if (!ideTipoCaptacao.equals(other.ideTipoCaptacao)) {
			return false;
		}
		return true;
	}
}