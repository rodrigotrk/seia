package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "funcionalidade_acao_pessoa_fisica_pauta")

public class FuncionalidadeAcaoPessoaFisicaPauta implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected FuncionalidadeAcaoPessoaFisicaPautaPK funcionalidadeAcaoPessoaFisicaPautaPK;
    
	@Column(name="ind_substituto",nullable=false)
    private Boolean indSubstituto;
	
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="ide_funcionalidade_acao_pessoa_fisica", referencedColumnName="ide_funcionalidade_acao_pessoa_fisica", nullable=false, insertable=false, updatable=false)
    private FuncionalidadeAcaoPessoaFisica ideFuncionalidadeAcaoPessoaFisica;
    
    @JoinColumn(name = "ide_pauta", referencedColumnName = "ide_pauta", nullable=false, insertable=false, updatable=false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Pauta idePauta;    
    

	public FuncionalidadeAcaoPessoaFisicaPauta(){
		
	}
	
	public FuncionalidadeAcaoPessoaFisicaPauta(	FuncionalidadeAcaoPessoaFisicaPautaPK funcionalidadeAcaoPessoaFisicaPautaPK,FuncionalidadeAcaoPessoaFisica ideFuncionalidadeAcaoPessoaFisica, Pauta idePauta) {
		this.funcionalidadeAcaoPessoaFisicaPautaPK = funcionalidadeAcaoPessoaFisicaPautaPK;
		this.ideFuncionalidadeAcaoPessoaFisica = ideFuncionalidadeAcaoPessoaFisica;
		this.idePauta = idePauta;
	}
    
    public FuncionalidadeAcaoPessoaFisicaPauta(	FuncionalidadeAcaoPessoaFisicaPautaPK funcionalidadeAcaoPessoaFisicaPautaPK,Boolean indSubstituto) {
		this.funcionalidadeAcaoPessoaFisicaPautaPK = funcionalidadeAcaoPessoaFisicaPautaPK;
		this.indSubstituto = indSubstituto;
	}


	public Boolean getIndSubstituto() {
		return indSubstituto;
	}

	public void setIndSubstituto(Boolean indSubstituto) {
		this.indSubstituto = indSubstituto;
	}

	public FuncionalidadeAcaoPessoaFisica getIdeFuncionalidadeAcaoPessoaFisica() {
		return ideFuncionalidadeAcaoPessoaFisica;
	}

	public void setIdeFuncionalidadeAcaoPessoaFisica(FuncionalidadeAcaoPessoaFisica ideFuncionalidadeAcaoPessoaFisica) {
		this.ideFuncionalidadeAcaoPessoaFisica = ideFuncionalidadeAcaoPessoaFisica;
	}

	public Pauta getIdePauta() {
		return idePauta;
	}


	public void setIdePauta(Pauta idePauta) {
		this.idePauta = idePauta;
	}
	
	public FuncionalidadeAcaoPessoaFisicaPautaPK getFuncionalidadeAcaoPessoaFisicaPautaPK() {
		return funcionalidadeAcaoPessoaFisicaPautaPK;
	}

	public void setFuncionalidadeAcaoPessoaFisicaPautaPK(
			FuncionalidadeAcaoPessoaFisicaPautaPK funcionalidadeAcaoPessoaFisicaPautaPK) {
		this.funcionalidadeAcaoPessoaFisicaPautaPK = funcionalidadeAcaoPessoaFisicaPautaPK;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((funcionalidadeAcaoPessoaFisicaPautaPK == null) ? 0 : funcionalidadeAcaoPessoaFisicaPautaPK.hashCode());
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
		FuncionalidadeAcaoPessoaFisicaPauta other = (FuncionalidadeAcaoPessoaFisicaPauta) obj;
		if (funcionalidadeAcaoPessoaFisicaPautaPK == null) {
			if (other.funcionalidadeAcaoPessoaFisicaPautaPK != null)
				return false;
		} else if (!funcionalidadeAcaoPessoaFisicaPautaPK.equals(other.funcionalidadeAcaoPessoaFisicaPautaPK))
			return false;
		return true;
	}
	
}