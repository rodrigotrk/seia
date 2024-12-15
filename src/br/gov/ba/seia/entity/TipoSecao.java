package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "tipo_secao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_tipo_secao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSecao.findAll", query = "SELECT t FROM TipoSecao t"),
    @NamedQuery(name = "TipoSecao.findByIdeTipoSecao", query = "SELECT t FROM TipoSecao t WHERE t.ideTipoSecao = :ideTipoSecao"),
    @NamedQuery(name = "TipoSecao.findByDscTipoSecao", query = "SELECT t FROM TipoSecao t WHERE t.dscTipoSecao = :dscTipoSecao"),
    @NamedQuery(name = "TipoSecao.findByIndExcluido", query = "SELECT t FROM TipoSecao t WHERE t.indExcluido = :indExcluido"),
    @NamedQuery(name = "TipoSecao.findByDtcCriacao", query = "SELECT t FROM TipoSecao t WHERE t.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "TipoSecao.findByDtcExclusao", query = "SELECT t FROM TipoSecao t WHERE t.dtcExclusao = :dtcExclusao")})
public class TipoSecao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_secao", nullable = false)
    private Integer ideTipoSecao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "dsc_tipo_secao", nullable = false, length = 20)
    private String dscTipoSecao;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoSecao", fetch = FetchType.LAZY)
    private Collection<Secao> secaoCollection;

    public TipoSecao() {
    }
    public TipoSecao(Integer ideTipoSecao) {
        this.ideTipoSecao = ideTipoSecao;
    }
    public TipoSecao(Integer ideTipoSecao, String dscTipoSecao, boolean indExcluido, Date dtcCriacao) {
        this.ideTipoSecao = ideTipoSecao;
        this.dscTipoSecao = dscTipoSecao;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeTipoSecao() {
        return ideTipoSecao;
    }

    public void setIdeTipoSecao(Integer ideTipoSecao) {
        this.ideTipoSecao = ideTipoSecao;
    }

    public String getDscTipoSecao() {
        return dscTipoSecao;
    }

    public void setDscTipoSecao(String dscTipoSecao) {
        this.dscTipoSecao = dscTipoSecao;
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
    public Collection<Secao> getSecaoCollection() {
        return secaoCollection;
    }

    public void setSecaoCollection(Collection<Secao> secaoCollection) {
        this.secaoCollection = secaoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoSecao != null ? ideTipoSecao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoSecao)) {
            return false;
        }
        TipoSecao other = (TipoSecao) object;
        if ((this.ideTipoSecao == null && other.ideTipoSecao != null) || (this.ideTipoSecao != null && !this.ideTipoSecao.equals(other.ideTipoSecao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

    	return String.valueOf(ideTipoSecao);
    }
}