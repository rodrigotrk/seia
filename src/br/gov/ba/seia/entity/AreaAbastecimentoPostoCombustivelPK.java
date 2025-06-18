package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author luis
 */
@Embeddable
public class AreaAbastecimentoPostoCombustivelPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_area_abastecimento")
	private int ideTipoAreaAbastecimento;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_posto_combustivel")
	private int idePostoCombustivel;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_permeabilidade_antes_reforma")
	private int ideAntesReforma;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_permeabilidade_depois_reforma")
	private int ideDepoisReforma;

	public AreaAbastecimentoPostoCombustivelPK() {
	}

	public AreaAbastecimentoPostoCombustivelPK(int ideTipoAreaAbastecimento, int idePostoCombustivel) {
		this.ideTipoAreaAbastecimento = ideTipoAreaAbastecimento;
		this.idePostoCombustivel = idePostoCombustivel;
	}

	public int getIdeTipoAreaAbastecimento() {
		return ideTipoAreaAbastecimento;
	}

	public void setIdeTipoAreaAbastecimento(int ideTipoAreaAbastecimento) {
		this.ideTipoAreaAbastecimento = ideTipoAreaAbastecimento;
	}

	public int getIdePostoCombustivel() {
		return idePostoCombustivel;
	}

	public void setIdePostoCombustivel(int idePostoCombustivel) {
		this.idePostoCombustivel = idePostoCombustivel;
	}

	public int getIdeAntesReforma() {
		return ideAntesReforma;
	}

	public void setIdeAntesReforma(int ideAntesReforma) {
		this.ideAntesReforma = ideAntesReforma;
	}

	public int getIdeDepoisReforma() {
		return ideDepoisReforma;
	}

	public void setIdeDepoisReforma(int ideDepoisReforma) {
		this.ideDepoisReforma = ideDepoisReforma;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ideAntesReforma;
		result = prime * result + ideDepoisReforma;
		result = prime * result + idePostoCombustivel;
		result = prime * result + ideTipoAreaAbastecimento;
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
		AreaAbastecimentoPostoCombustivelPK other = (AreaAbastecimentoPostoCombustivelPK) obj;
		if (ideAntesReforma != other.ideAntesReforma)
			return false;
		if (ideDepoisReforma != other.ideDepoisReforma)
			return false;
		if (idePostoCombustivel != other.idePostoCombustivel)
			return false;
		if (ideTipoAreaAbastecimento != other.ideTipoAreaAbastecimento)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "entity.AreaAbastecimentoPostoCombustivelPK[ ideTipoAreaAbastecimento=" + ideTipoAreaAbastecimento + ", idePostoCombustivel="
				+ idePostoCombustivel + " ]";
	}

}
