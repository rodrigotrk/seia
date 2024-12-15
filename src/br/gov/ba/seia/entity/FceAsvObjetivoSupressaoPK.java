package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
public class FceAsvObjetivoSupressaoPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_asv")
	private Integer ideFceAsv;

	@Column(name="ide_objetivo_supressao")
	private Integer ideObjetivoSupressao;

	public FceAsvObjetivoSupressaoPK() {

	}

	public FceAsvObjetivoSupressaoPK(Integer ideFceAsv,	Integer ideObjetivoSupressao) {
		this.ideFceAsv = ideFceAsv;
		this.ideObjetivoSupressao = ideObjetivoSupressao;
	}

	public Integer getIdeFceAsv() {
		return ideFceAsv;
	}

	public void setIdeFceAsv(Integer ideFceAsv) {
		this.ideFceAsv = ideFceAsv;
	}

	public Integer getIdeObjetivoSupressao() {
		return ideObjetivoSupressao;
	}

	public void setIdeObjetivoSupressao(Integer ideObjetivoSupressao) {
		this.ideObjetivoSupressao = ideObjetivoSupressao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceAsv == null) ? 0 : ideFceAsv.hashCode());
		result = prime
				* result
				+ ((ideObjetivoSupressao == null) ? 0 : ideObjetivoSupressao
						.hashCode());
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
		FceAsvObjetivoSupressaoPK other = (FceAsvObjetivoSupressaoPK) obj;
		if (ideFceAsv == null) {
			if (other.ideFceAsv != null)
				return false;
		} else if (!ideFceAsv.equals(other.ideFceAsv))
			return false;
		if (ideObjetivoSupressao == null) {
			if (other.ideObjetivoSupressao != null)
				return false;
		} else if (!ideObjetivoSupressao.equals(other.ideObjetivoSupressao))
			return false;
		return true;
	}
}