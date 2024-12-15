package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class FuncionalidadeAcaoPessoaFisicaPautaPK implements Serializable {
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_funcionalidade_acao_pessoa_fisica")
	private int idefuncionalidadeAcaoPessoaFisica;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_pauta")
	private int idePauta;
	
	public FuncionalidadeAcaoPessoaFisicaPautaPK() {
	}	

	public FuncionalidadeAcaoPessoaFisicaPautaPK(int idefuncionalidadeAcaoPessoaFisica, int idePauta) {
		this.idefuncionalidadeAcaoPessoaFisica = idefuncionalidadeAcaoPessoaFisica;
		this.idePauta = idePauta;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (int) idefuncionalidadeAcaoPessoaFisica;
		hash += (int) idePauta;
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof FuncionalidadeAcaoPessoaFisicaPautaPK)) {
			return false;
		}
		FuncionalidadeAcaoPessoaFisicaPautaPK other = (FuncionalidadeAcaoPessoaFisicaPautaPK) object;
		if (this.idefuncionalidadeAcaoPessoaFisica != other.idefuncionalidadeAcaoPessoaFisica) {
			return false;
		}
		if (this.idePauta != other.idePauta) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.FuncionalidadeAcaoPessoaFisicaPautaPK[ idefuncionalidadeAcaoPessoaFisica="
				+ idefuncionalidadeAcaoPessoaFisica
				+ ", idePauta="
				+ idePauta
				+ " ]";
	}

	public int getIdefuncionalidadeAcaoPessoaFisica() {
		return idefuncionalidadeAcaoPessoaFisica;
	}

	public void setIdefuncionalidadeAcaoPessoaFisica(
			int idefuncionalidadeAcaoPessoaFisica) {
		this.idefuncionalidadeAcaoPessoaFisica = idefuncionalidadeAcaoPessoaFisica;
	}

	public int getIdePauta() {
		return idePauta;
	}

	public void setIdePauta(int idePauta) {
		this.idePauta = idePauta;
	}

}
