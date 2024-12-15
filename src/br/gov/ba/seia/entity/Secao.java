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

@Entity
@Table(name = "secao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_secao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Secao.findAll", query = "SELECT s FROM Secao s"),
    @NamedQuery(name = "Secao.findByIdeSecao", query = "SELECT s FROM Secao s WHERE s.ideSecao = :ideSecao"),
    @NamedQuery(name = "Secao.findByDscSecao", query = "SELECT s FROM Secao s WHERE s.dscSecao = :dscSecao"),
    @NamedQuery(name = "Secao.findByIndExcluido", query = "SELECT s FROM Secao s WHERE s.indExcluido = :indExcluido"),
    @NamedQuery(name = "Secao.findByDtcCriacao", query = "SELECT s FROM Secao s WHERE s.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Secao.findByDtcExclusao", query = "SELECT s FROM Secao s WHERE s.dtcExclusao = :dtcExclusao")})
public class Secao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SECAO_IDE_SECAO_seq")    
    @SequenceGenerator(name="SECAO_IDE_SECAO_seq", sequenceName="SECAO_IDE_SECAO_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_secao", nullable = false)
    private Integer ideSecao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "dsc_secao", nullable = false, length = 20)
    private String dscSecao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_cricao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideSecao", fetch = FetchType.LAZY)
    private Collection<Funcionalidade> funcionalidadeCollection;
    @JoinColumn(name = "ide_tipo_secao", referencedColumnName = "ide_tipo_secao", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TipoSecao ideTipoSecao;
    @OneToMany(mappedBy = "ideSecaoPai", fetch = FetchType.LAZY)
    private Collection<Secao> secaoCollection;
    @JoinColumn(name = "ide_secao_pai", referencedColumnName = "ide_secao")
    @ManyToOne(fetch = FetchType.EAGER)
    private Secao ideSecaoPai;

    public Secao() {
    }
    public Secao(Integer ideSecao) {
        this.ideSecao = ideSecao;
    }
    public Secao(Integer ideSecao, String dscSecao) {
        this.ideSecao = ideSecao;
        this.dscSecao = dscSecao;
    }
    public Secao(String dscSecao) {
        this.dscSecao = dscSecao;
    }
    public Secao(Integer ideSecao, String dscSecao, boolean indExcluido, Date dtcCriacao, Date dtcExclusao) {
        this.ideSecao = ideSecao;
        this.dscSecao = dscSecao;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
        this.dtcExclusao = dtcExclusao;
    }

    public Integer getIdeSecao() {
        return ideSecao;
    }

    public void setIdeSecao(Integer ideSecao) {
        this.ideSecao = ideSecao;
    }

    public String getDscSecao() {
        return dscSecao;
    }

    public void setDscSecao(String dscSecao) {
        this.dscSecao = dscSecao;
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
    public Collection<Funcionalidade> getFuncionalidadeCollection() {
        return funcionalidadeCollection;
    }

    public void setFuncionalidadeCollection(Collection<Funcionalidade> funcionalidadeCollection) {
        this.funcionalidadeCollection = funcionalidadeCollection;
    }

    public TipoSecao getIdeTipoSecao() {
        return ideTipoSecao;
    }

    public void setIdeTipoSecao(TipoSecao ideTipoSecao) {
        this.ideTipoSecao = ideTipoSecao;
    }

    @XmlTransient
    public Collection<Secao> getSecaoCollection() {
        return secaoCollection;
    }

    public void setSecaoCollection(Collection<Secao> secaoCollection) {
        this.secaoCollection = secaoCollection;
    }

    public Secao getIdeSecaoPai() {
        return ideSecaoPai;
    }

    public void setIdeSecaoPai(Secao ideSecaoPai) {
        this.ideSecaoPai = ideSecaoPai;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSecao != null ? ideSecao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Secao)) {
            return false;
        }
        Secao other = (Secao) object;
        if ((this.ideSecao == null && other.ideSecao != null) || (this.ideSecao != null && !this.ideSecao.equals(other.ideSecao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

    	return String.valueOf(ideSecao);
    }
}