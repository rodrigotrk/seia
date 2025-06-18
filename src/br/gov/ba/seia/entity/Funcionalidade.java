package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "funcionalidade", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_funcionalidade"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Funcionalidade.findAll", query = "SELECT f FROM Funcionalidade f"),
    @NamedQuery(name = "Funcionalidade.findByIdeFuncionalidade", query = "SELECT f FROM Funcionalidade f WHERE f.ideFuncionalidade = :ideFuncionalidade"),
    @NamedQuery(name = "Funcionalidade.findByDscFuncionalidade", query = "SELECT f FROM Funcionalidade f WHERE f.dscFuncionalidade = :dscFuncionalidade"),
    @NamedQuery(name = "Funcionalidade.findByIndExcluido", query = "SELECT f FROM Funcionalidade f WHERE f.indExcluido = :indExcluido"),
    @NamedQuery(name = "Funcionalidade.findByDtcCriacao", query = "SELECT f FROM Funcionalidade f WHERE f.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Funcionalidade.findByDtcExclusao", query = "SELECT f FROM Funcionalidade f WHERE f.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "Funcionalidade.findBySecao", query = "SELECT f FROM Funcionalidade f left join f.ideSecao s WHERE s.ideSecao = :ideSecao")})
public class Funcionalidade implements Serializable, BaseEntity, Comparable<Funcionalidade> {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FUNCIONALIDADE_IDE_FUNCIONALIDADE_seq")    
    @SequenceGenerator(name="FUNCIONALIDADE_IDE_FUNCIONALIDADE_seq", sequenceName="FUNCIONALIDADE_IDE_FUNCIONALIDADE_seq", allocationSize=1)
    @NotNull
    @Column(name = "ide_funcionalidade", nullable = false)
    private Integer ideFuncionalidade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_funcionalidade", nullable = false, length = 50)
    private String dscFuncionalidade;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    @JoinColumn(name = "ide_secao", referencedColumnName = "ide_secao", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Secao ideSecao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionalidade", fetch = FetchType.LAZY)
    private Collection<RelGrupoPerfilFuncionalidade> relGrupoPerfilFuncionalidadeCollection;
    
    @OneToMany(mappedBy="ideFuncionalidade", fetch=FetchType.LAZY)
    private Collection<FuncionalidadeAcaoPessoaFisica> funcionalidadeAcaoPessoaFisicaCollection;

    public Funcionalidade() {
    }

    public Funcionalidade(Integer ideFuncionalidade) {
        this.ideFuncionalidade = ideFuncionalidade;
    }
    
    public Funcionalidade(String dscFuncionalidade) {
        this.dscFuncionalidade = dscFuncionalidade;
    }
    
    public Funcionalidade(Integer ideFuncionalidade, String dscFuncionalidade, boolean indExcluido, Date dtcCriacao) {
        this.ideFuncionalidade = ideFuncionalidade;
        this.dscFuncionalidade = dscFuncionalidade;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }
    
    public Funcionalidade(Integer ideFuncionalidade, String dscFuncionalidade) {
        this.ideFuncionalidade = ideFuncionalidade;
        this.dscFuncionalidade = dscFuncionalidade;
    }

    public Integer getIdeFuncionalidade() {
        return ideFuncionalidade;
    }

    public void setIdeFuncionalidade(Integer ideFuncionalidade) {
        this.ideFuncionalidade = ideFuncionalidade;
    }

    public String getDscFuncionalidade() {
        return dscFuncionalidade;
    }

    public void setDscFuncionalidade(String dscFuncionalidade) {
        this.dscFuncionalidade = dscFuncionalidade;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    public Secao getIdeSecao() {
        return ideSecao;
    }

    public void setIdeSecao(Secao ideSecao) {
        this.ideSecao = ideSecao;
    }

    @XmlTransient
    public Collection<RelGrupoPerfilFuncionalidade> getRelGrupoPerfilFuncionalidadeCollection() {
        return relGrupoPerfilFuncionalidadeCollection;
    }

    public void setRelGrupoPerfilFuncionalidadeCollection(Collection<RelGrupoPerfilFuncionalidade> relGrupoPerfilFuncionalidadeCollection) {
        this.relGrupoPerfilFuncionalidadeCollection = relGrupoPerfilFuncionalidadeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideFuncionalidade != null ? ideFuncionalidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Funcionalidade)) {
            return false;
        }
        Funcionalidade other = (Funcionalidade) object;
        if ((this.ideFuncionalidade == null && other.ideFuncionalidade != null) || (this.ideFuncionalidade != null && !this.ideFuncionalidade.equals(other.ideFuncionalidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.valueOf(ideFuncionalidade);
    }

	@Override
	public Long getId() {
		return new Long(getIdeFuncionalidade());
	}

	public Collection<FuncionalidadeAcaoPessoaFisica> getFuncionalidadeAcaoPessoaFisicaCollection() {
		return funcionalidadeAcaoPessoaFisicaCollection;
	}

	public void setFuncionalidadeAcaoPessoaFisicaCollection(
			Collection<FuncionalidadeAcaoPessoaFisica> funcionalidadeAcaoPessoaFisicaCollection) {
		this.funcionalidadeAcaoPessoaFisicaCollection = funcionalidadeAcaoPessoaFisicaCollection;
	}

	@Override
	public int compareTo(Funcionalidade o) {
		if(Util.isNullOuVazio(o.getDscFuncionalidade())){
			return 0;
		} else {
			return this.getDscFuncionalidade().compareTo(o.getDscFuncionalidade());
		}
	}
}
