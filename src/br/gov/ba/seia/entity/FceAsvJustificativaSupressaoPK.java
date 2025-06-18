package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * The primary key class for the fce_asv_ocorrencia_area database table.
 * 
 */
@Embeddable
public class FceAsvJustificativaSupressaoPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(name="ide_fce_asv")
	private Integer ideFceAsv;

	@Basic(optional = false)
	@NotNull
	@Column(name="ide_justificativa_supressao")
	private Integer ideJustificativaSupressao;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceAsv == null) ? 0 : ideFceAsv.hashCode());
		result = prime
				* result
				+ ((ideJustificativaSupressao == null) ? 0
						: ideJustificativaSupressao.hashCode());
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
		FceAsvJustificativaSupressaoPK other = (FceAsvJustificativaSupressaoPK) obj;
		if (ideFceAsv == null) {
			if (other.ideFceAsv != null) {
				return false;
			}
		} else if (!ideFceAsv.equals(other.ideFceAsv)) {
			return false;
		}
		if (ideJustificativaSupressao == null) {
			if (other.ideJustificativaSupressao != null) {
				return false;
			}
		} else if (!ideJustificativaSupressao
				.equals(other.ideJustificativaSupressao)) {
			return false;
		}
		return true;
	}

	public FceAsvJustificativaSupressaoPK() {

	}

	public FceAsvJustificativaSupressaoPK(Integer ideFceAsv, Integer ideJustificativaSupressao) {
		this.ideFceAsv = ideFceAsv;
		this.ideJustificativaSupressao = ideJustificativaSupressao;
	}

	public Integer getIdeFceAsv() {
		return ideFceAsv;
	}

	public void setIdeFceAsv(Integer ideFceAsv) {
		this.ideFceAsv = ideFceAsv;
	}

	public Integer getIdeJustificativaSupressao() {
		return ideJustificativaSupressao;
	}

	public void setIdeJustificativaSupressao(Integer ideJustificativaSupressao) {
		this.ideJustificativaSupressao = ideJustificativaSupressao;
	}
}