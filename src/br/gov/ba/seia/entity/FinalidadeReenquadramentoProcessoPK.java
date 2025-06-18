package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FinalidadeReenquadramentoProcessoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ide_finalidade_reenquadramento", nullable = false)
	private Integer ideFinalidadeReenquadramento;

	@Column(name = "ide_reenquadramento_processo", nullable = false)
	private Integer ideReenquadramentoProcesso;

	public FinalidadeReenquadramentoProcessoPK() {

	}
	
	public FinalidadeReenquadramentoProcessoPK(ReenquadramentoProcesso ideReenquadramentoProcesso, FinalidadeReenquadramento ideFinalidadeReenquadramento) {
		this.ideReenquadramentoProcesso = ideReenquadramentoProcesso.getIdeReenquadramentoProcesso();
		this.ideFinalidadeReenquadramento = ideFinalidadeReenquadramento.getIdeFinalidadeReenquadramento();
	}

	public Integer getIdeFinalidadeReenquadramento() {
		return ideFinalidadeReenquadramento;
	}

	public void setIdeFinalidadeReenquadramento(Integer ideFinalidadeReenquadramento) {
		this.ideFinalidadeReenquadramento = ideFinalidadeReenquadramento;
	}

	public Integer getIdeReenquadramentoProcesso() {
		return ideReenquadramentoProcesso;
	}

	public void setIdeReenquadramentoProcesso(Integer ideReenquadramentoProcesso) {
		this.ideReenquadramentoProcesso = ideReenquadramentoProcesso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFinalidadeReenquadramento == null) ? 0 : ideFinalidadeReenquadramento.hashCode());
		result = prime * result + ((ideReenquadramentoProcesso == null) ? 0 : ideReenquadramentoProcesso.hashCode());
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
		FinalidadeReenquadramentoProcessoPK other = (FinalidadeReenquadramentoProcessoPK) obj;
		if (ideFinalidadeReenquadramento == null) {
			if (other.ideFinalidadeReenquadramento != null)
				return false;
		} else if (!ideFinalidadeReenquadramento.equals(other.ideFinalidadeReenquadramento))
			return false;
		if (ideReenquadramentoProcesso == null) {
			if (other.ideReenquadramentoProcesso != null)
				return false;
		} else if (!ideReenquadramentoProcesso.equals(other.ideReenquadramentoProcesso))
			return false;
		return true;
	}

}
