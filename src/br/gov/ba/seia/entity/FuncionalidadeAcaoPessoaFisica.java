package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "funcionalidade_acao_pessoa_fisica", uniqueConstraints = {@UniqueConstraint(columnNames = {"ide_funcionalidade_acao_pessoa_fisica"})})

public class FuncionalidadeAcaoPessoaFisica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="funcionalidade_acao_pessoa_fisica_seq")    
    @SequenceGenerator(name="funcionalidade_acao_pessoa_fisica_seq", sequenceName="funcionalidade_acao_pessoa_fisica_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_funcionalidade_acao_pessoa_fisica", nullable = false)
    private Integer ideFuncionalidadeAcaoPessoaFisica;
    
    @JoinColumn(name="ide_funcionalidade",nullable=false)
    @ManyToOne(fetch=FetchType.EAGER)
    private Funcionalidade ideFuncionalidade;
    
    @JoinColumn(name="ide_acao",nullable=false)
    @ManyToOne(fetch=FetchType.EAGER)
    private Acao ideAcao;
    
    @JoinColumn(name="ide_pessoa_fisica",nullable=false)
    @ManyToOne(fetch=FetchType.EAGER)
    private Funcionario idePessoaFisica;
    
    @OneToMany(mappedBy="ideFuncionalidadeAcaoPessoaFisica")
    private Collection<FuncionalidadeAcaoPessoaFisicaPauta> funcionalidadeAcaoPessoaFisicaPautaCollection;
    

	public FuncionalidadeAcaoPessoaFisica() {
	}

	public FuncionalidadeAcaoPessoaFisica(Funcionalidade ideFuncionalidade,Funcionario idePessoaFisica) {
		this.ideFuncionalidade = ideFuncionalidade;
		this.idePessoaFisica = idePessoaFisica;
	}
	
	public FuncionalidadeAcaoPessoaFisica(Funcionalidade ideFuncionalidade,Funcionario idePessoaFisica, Acao ideAcao) {
		this.ideFuncionalidade = ideFuncionalidade;
		this.idePessoaFisica = idePessoaFisica;
		this.ideAcao = ideAcao;
	}



	public Integer getIdeFuncionalidadeAcaoPessoaFisica() {
		return ideFuncionalidadeAcaoPessoaFisica;
	}

	public void setIdeFuncionalidadeAcaoPessoaFisica(
			Integer ideFuncionalidadeAcaoPessoaFisica) {
		this.ideFuncionalidadeAcaoPessoaFisica = ideFuncionalidadeAcaoPessoaFisica;
	}

	public Funcionalidade getIdeFuncionalidade() {
		return ideFuncionalidade;
	}

	public void setIdeFuncionalidade(Funcionalidade ideFuncionalidade) {
		this.ideFuncionalidade = ideFuncionalidade;
	}

	public Acao getIdeAcao() {
		return ideAcao;
	}

	public void setIdeAcao(Acao ideAcao) {
		this.ideAcao = ideAcao;
	}

	public Funcionario getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Funcionario idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideFuncionalidadeAcaoPessoaFisica != null ? ideFuncionalidadeAcaoPessoaFisica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof FuncionalidadeAcaoPessoaFisica)) {
            return false;
        }
        FuncionalidadeAcaoPessoaFisica other = (FuncionalidadeAcaoPessoaFisica) object;
        if ((this.ideFuncionalidadeAcaoPessoaFisica == null && other.ideFuncionalidadeAcaoPessoaFisica != null) || (this.ideFuncionalidadeAcaoPessoaFisica != null && !this.ideFuncionalidadeAcaoPessoaFisica.equals(other.ideFuncionalidadeAcaoPessoaFisica))) {
            return false;
        }
        return true;
    }

	public Collection<FuncionalidadeAcaoPessoaFisicaPauta> getFuncionalidadeAcaoPessoaFisicaPautaCollection() {
		return funcionalidadeAcaoPessoaFisicaPautaCollection;
	}

	public void setFuncionalidadeAcaoPessoaFisicaPautaCollection(
			Collection<FuncionalidadeAcaoPessoaFisicaPauta> funcionalidadeAcaoPessoaFisicaPautaCollection) {
		this.funcionalidadeAcaoPessoaFisicaPautaCollection = funcionalidadeAcaoPessoaFisicaPautaCollection;
	}
    
}