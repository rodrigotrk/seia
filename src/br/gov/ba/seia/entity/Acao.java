package br.gov.ba.seia.entity;

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

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "acao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_acao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Acao.findAll", query = "SELECT a FROM Acao a"),
    @NamedQuery(name = "Acao.findByIdeAcao", query = "SELECT a FROM Acao a WHERE a.ideAcao = :ideAcao"),
    @NamedQuery(name = "Acao.findByDscAcao", query = "SELECT a FROM Acao a WHERE a.dscAcao = :dscAcao"),
    @NamedQuery(name = "Acao.findByIndExcluido", query = "SELECT a FROM Acao a WHERE a.indExcluido = :indExcluido"),
    @NamedQuery(name = "Acao.findByDtcCriacao", query = "SELECT a FROM Acao a WHERE a.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Acao.findByDtcExclusao", query = "SELECT a FROM Acao a WHERE a.dtcExclusao = :dtcExclusao")})

public class Acao extends AbstractEntity implements Comparable<Acao> {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ACAO_IDE_ACAO_seq")
    @SequenceGenerator(name="ACAO_IDE_ACAO_seq", sequenceName="ACAO_IDE_ACAO_seq", allocationSize=1)
    @Column(name = "ide_acao", nullable = false)
    private Integer ideAcao;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "dsc_acao", nullable = false, length = 20)
    private String dscAcao;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "acao", fetch = FetchType.LAZY)
    private Collection<RelGrupoPerfilFuncionalidade> relGrupoPerfilFuncionalidadeCollection;

    @OneToMany(mappedBy="ideAcao", fetch=FetchType.LAZY)
    private Collection<FuncionalidadeAcaoPessoaFisica> funcionalidadeAcaoPessoaFisicaCollection;

    public Acao() {
    }
    public Acao(Integer ideAcao) {
        this.ideAcao = ideAcao;
    }
    public Acao(String dscAcao) {
    	this.dscAcao = dscAcao;
    }
    public Acao(Integer ideAcao, String dscAcao, boolean indExcluido, Date dtcCriacao) {
        this.ideAcao = ideAcao;
        this.dscAcao = dscAcao;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeAcao() {
        return ideAcao;
    }

    public void setIdeAcao(Integer ideAcao) {
        this.ideAcao = ideAcao;
    }

    public String getDscAcao() {
        return dscAcao;
    }

    public void setDscAcao(String dscAcao) {
        this.dscAcao = dscAcao;
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

    @XmlTransient
    public Collection<RelGrupoPerfilFuncionalidade> getRelGrupoPerfilFuncionalidadeCollection() {
        return relGrupoPerfilFuncionalidadeCollection;
    }

    public void setRelGrupoPerfilFuncionalidadeCollection(Collection<RelGrupoPerfilFuncionalidade> relGrupoPerfilFuncionalidadeCollection) {
        this.relGrupoPerfilFuncionalidadeCollection = relGrupoPerfilFuncionalidadeCollection;
    }

	public Collection<FuncionalidadeAcaoPessoaFisica> getFuncionalidadeAcaoPessoaFisicaCollection() {
		return funcionalidadeAcaoPessoaFisicaCollection;
	}
	public void setFuncionalidadeAcaoPessoaFisicaCollection(Collection<FuncionalidadeAcaoPessoaFisica> funcionalidadeAcaoPessoaFisicaCollection) {
		this.funcionalidadeAcaoPessoaFisicaCollection = funcionalidadeAcaoPessoaFisicaCollection;
	}
	@Override
	public int compareTo(Acao o) {
		if(Util.isNullOuVazio(o.getDscAcao())){
			return 0;
		} else {
			return this.getDscAcao().compareTo(o.getDscAcao());
		}
	}
}