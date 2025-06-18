package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_asv_ocorrencia_area database table.
 * 
 */
@Embeddable
public class FceAsvOcorrenciaAreaPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_asv")
	private Integer ideFceAsv;

	@Column(name="ide_ocorrencia_area")
	private Integer ideOcorrenciaArea;

	public FceAsvOcorrenciaAreaPK() {
	}

	public FceAsvOcorrenciaAreaPK(Integer ideFceAsv, Integer ideObjetivoSupressao) {
		this.ideFceAsv = ideFceAsv;
		ideOcorrenciaArea = ideObjetivoSupressao;
	}

	public Integer getIdeObjetivoSupressao() {
		return ideOcorrenciaArea;
	}

	public void setIdeOcorrenciaArea(Integer ideObjetivoSupressao) {
		ideOcorrenciaArea = ideObjetivoSupressao;
	}

	public Integer getIdeFceAsv() {
		return ideFceAsv;
	}

	public void setIdeFceAsv(Integer ideFceAsv) {
		this.ideFceAsv = ideFceAsv;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceAsv == null) ? 0 : ideFceAsv.hashCode());
		result = prime
				* result
				+ ((ideOcorrenciaArea == null) ? 0 : ideOcorrenciaArea
						.hashCode());
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
		FceAsvOcorrenciaAreaPK other = (FceAsvOcorrenciaAreaPK) obj;
		if (ideFceAsv == null) {
			if (other.ideFceAsv != null) {
				return false;
			}
		} else if (!ideFceAsv.equals(other.ideFceAsv)) {
			return false;
		}
		if (ideOcorrenciaArea == null) {
			if (other.ideOcorrenciaArea != null) {
				return false;
			}
		} else if (!ideOcorrenciaArea.equals(other.ideOcorrenciaArea)) {
			return false;
		}
		return true;
	}
}