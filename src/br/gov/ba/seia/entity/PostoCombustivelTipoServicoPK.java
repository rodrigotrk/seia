package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class PostoCombustivelTipoServicoPK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_servico_posto", nullable = false)
	private int ideTipoServico;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_posto_combustivel", nullable = false)
	private int idePostoCombustivel;

	public PostoCombustivelTipoServicoPK() {
	}

	public int getIdeTipoServico() {
		return ideTipoServico;
	}

	public void setIdeTipoServico(int ideTipoServico) {
		this.ideTipoServico = ideTipoServico;
	}

	public int getIdePostoCombustivel() {
		return idePostoCombustivel;
	}

	public void setIdePostoCombustivel(int idePostoCombustivel) {
		this.idePostoCombustivel = idePostoCombustivel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idePostoCombustivel;
		result = prime * result + ideTipoServico;
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
		PostoCombustivelTipoServicoPK other = (PostoCombustivelTipoServicoPK) obj;
		if (idePostoCombustivel != other.idePostoCombustivel)
			return false;
		if (ideTipoServico != other.ideTipoServico)
			return false;
		return true;
	}

}
