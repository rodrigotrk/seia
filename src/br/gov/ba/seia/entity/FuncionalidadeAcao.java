package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "funcionalidade_acao")
@NamedQueries({@NamedQuery(name = "FuncionalidadeAcao.findByFuncionalidade", query = "SELECT f FROM FuncionalidadeAcao f WHERE f.funcionalidade.ideFuncionalidade = :ideFuncionalidade")})
public class FuncionalidadeAcao implements Serializable {
	
	private static final long serialVersionUID = 7144184672976521039L;
	
	@Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="funcionalidade_acao_ide_funcionalidade_seq")    
    @SequenceGenerator(name="funcionalidade_acao_ide_funcionalidade_seq", sequenceName="funcionalidade_acao_ide_funcionalidade_seq", allocationSize=1)
    @NotNull
    @Column(name = "ide_funcionalidade_acao", nullable = false)
    private Integer ideFuncionalidadeAcao;

    @JoinColumn(name = "ide_funcionalidade", referencedColumnName = "ide_funcionalidade", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch=FetchType.EAGER)
    private Funcionalidade funcionalidade;
    
    @JoinColumn(name="ide_acao", referencedColumnName = "ide_acao", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch=FetchType.EAGER)
    private Acao acao;
    
    public FuncionalidadeAcao() {
    	super();
    }
    
	/**
	 * @param ideFuncionalidadeAcao
	 * @param funcionalidade
	 * @param acao
	 */
	public FuncionalidadeAcao(Integer ideFuncionalidadeAcao, Funcionalidade funcionalidade, Acao acao) {

		super();
		this.ideFuncionalidadeAcao = ideFuncionalidadeAcao;
		this.funcionalidade = funcionalidade;
		this.acao = acao;
	}

	/**
	 * @return the ideFuncionalidadeAcao
	 */
	public Integer getIdeFuncionalidadeAcao() {
	
		return ideFuncionalidadeAcao;
	}

	/**
	 * @param ideFuncionalidadeAcao the ideFuncionalidadeAcao to set
	 */
	public void setIdeFuncionalidadeAcao(Integer ideFuncionalidadeAcao) {
	
		this.ideFuncionalidadeAcao = ideFuncionalidadeAcao;
	}

	/**
	 * @return the funcionalidade
	 */
	public Funcionalidade getFuncionalidade() {
	
		return funcionalidade;
	}

	/**
	 * @param funcionalidade the funcionalidade to set
	 */
	public void setFuncionalidade(Funcionalidade funcionalidade) {
	
		this.funcionalidade = funcionalidade;
	}

	/**
	 * @return the acao
	 */
	public Acao getAcao() {
	
		return acao;
	}

	/**
	 * @param acao the acao to set
	 */
	public void setAcao(Acao acao) {
	
		this.acao = acao;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFuncionalidadeAcao == null) ? 0 : ideFuncionalidadeAcao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FuncionalidadeAcao other = (FuncionalidadeAcao) obj;
		if (ideFuncionalidadeAcao == null) {
			if (other.ideFuncionalidadeAcao != null) return false;
		} else if (!ideFuncionalidadeAcao.equals(other.ideFuncionalidadeAcao)) return false;
		return true;
	}

	@Override
	public String toString() {

		return "FuncionalidadeAcao [ideFuncionalidadeAcao=" + ideFuncionalidadeAcao + "]";
	}
}