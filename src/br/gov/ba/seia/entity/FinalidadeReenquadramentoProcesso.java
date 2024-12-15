package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.FinalidadeReenquadramentoEnum;

@Entity
@Table(name = "finalidade_reenquadramento_processo")
public class FinalidadeReenquadramentoProcesso implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FinalidadeReenquadramentoProcessoPK finalidadeReenquadramentoProcessoPK;

	@JoinColumn(name = "ide_finalidade_reenquadramento", referencedColumnName = "ide_finalidade_reenquadramento", nullable = false, insertable=false, updatable=false)
	@ManyToOne(fetch = FetchType.EAGER)
	private FinalidadeReenquadramento ideFinalidadeReenquadramento;

	@JoinColumn(name = "ide_reenquadramento_processo", referencedColumnName = "ide_reenquadramento_processo", nullable = false, insertable=false, updatable=false)
	@ManyToOne(fetch = FetchType.EAGER)
	private ReenquadramentoProcesso ideReenquadramentoProcesso;

	public FinalidadeReenquadramentoProcesso() {
	}
	
	public FinalidadeReenquadramentoProcesso(ReenquadramentoProcesso ideReenquadramentoProcesso, FinalidadeReenquadramentoEnum finalidadeReenquadramentoEnum) {
		this.ideReenquadramentoProcesso = ideReenquadramentoProcesso;
		this.ideFinalidadeReenquadramento = new FinalidadeReenquadramento(finalidadeReenquadramentoEnum);
		this.finalidadeReenquadramentoProcessoPK = new FinalidadeReenquadramentoProcessoPK(ideReenquadramentoProcesso,ideFinalidadeReenquadramento);
	}
	
	public void gerarPK() {
		this.finalidadeReenquadramentoProcessoPK = new FinalidadeReenquadramentoProcessoPK(ideReenquadramentoProcesso,ideFinalidadeReenquadramento);
	}
	
	public FinalidadeReenquadramentoProcessoPK getFinalidadeReenquadramentoProcessoPK() {
		return finalidadeReenquadramentoProcessoPK;
	}

	public void setFinalidadeReenquadramentoProcessoPK(
			FinalidadeReenquadramentoProcessoPK finalidadeReenquadramentoProcessoPK) {
		this.finalidadeReenquadramentoProcessoPK = finalidadeReenquadramentoProcessoPK;
	}

	public FinalidadeReenquadramento getIdeFinalidadeReenquadramento() {
		return ideFinalidadeReenquadramento;
	}

	public void setIdeFinalidadeReenquadramento(FinalidadeReenquadramento ideFinalidadeReenquadramento) {
		this.ideFinalidadeReenquadramento = ideFinalidadeReenquadramento;
	}

	public ReenquadramentoProcesso getIdeReenquadramentoProcesso() {
		return ideReenquadramentoProcesso;
	}

	public void setIdeReenquadramentoProcesso(ReenquadramentoProcesso ideReenquadramentoProcesso) {
		this.ideReenquadramentoProcesso = ideReenquadramentoProcesso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((finalidadeReenquadramentoProcessoPK == null) ? 0 : finalidadeReenquadramentoProcessoPK.hashCode());
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
		FinalidadeReenquadramentoProcesso other = (FinalidadeReenquadramentoProcesso) obj;
		if (finalidadeReenquadramentoProcessoPK == null) {
			if (other.finalidadeReenquadramentoProcessoPK != null)
				return false;
		} else if (!finalidadeReenquadramentoProcessoPK.equals(other.finalidadeReenquadramentoProcessoPK))
			return false;
		return true;
	}

}