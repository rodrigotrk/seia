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
@Table(name = "tipo_modalidade_erb", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_tipo_modalidade_erb"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoModalidadeErb.findAll", query = "SELECT t FROM TipoModalidadeErb t order by t.ideTipoModalidadeErb"),
    @NamedQuery(name = "TipoModalidadeErb.findByIdeTipoModalidadeErb", query = "SELECT t FROM TipoModalidadeErb t WHERE t.ideTipoModalidadeErb = :ideTipoModalidadeErb"),
    @NamedQuery(name = "TipoModalidadeErb.findByDscTipoModalidadeErb", query = "SELECT t FROM TipoModalidadeErb t WHERE t.dscTipoModalidadeErb = :dscTipoModalidadeErb"),
    @NamedQuery(name = "TipoModalidadeErb.findByDtcCriacao", query = "SELECT t FROM TipoModalidadeErb t WHERE t.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "TipoModalidadeErb.findByDtcExclusao", query = "SELECT t FROM TipoModalidadeErb t WHERE t.dtcExclusao = :dtcExclusao")})
public class TipoModalidadeErb implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_modalidade_erb", nullable = false)
    private Integer ideTipoModalidadeErb;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_tipo_modalidade_erb", nullable = false, length = 50)
    private String dscTipoModalidadeErb;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoModalidadeErb", fetch = FetchType.LAZY)
    private Collection<LacErb> lacErbCollection;

    public TipoModalidadeErb() {
    }

    public TipoModalidadeErb(Integer ideTipoModalidadeErb) {
        this.ideTipoModalidadeErb = ideTipoModalidadeErb;
    }

    public TipoModalidadeErb(Integer ideTipoModalidadeErb, String dscTipoModalidadeErb, Date dtcCriacao) {
        this.ideTipoModalidadeErb = ideTipoModalidadeErb;
        this.dscTipoModalidadeErb = dscTipoModalidadeErb;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeTipoModalidadeErb() {
        return ideTipoModalidadeErb;
    }

    public void setIdeTipoModalidadeErb(Integer ideTipoModalidadeErb) {
        this.ideTipoModalidadeErb = ideTipoModalidadeErb;
    }

    public String getDscTipoModalidadeErb() {
        return dscTipoModalidadeErb;
    }

    public void setDscTipoModalidadeErb(String dscTipoModalidadeErb) {
        this.dscTipoModalidadeErb = dscTipoModalidadeErb;
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
    public Collection<LacErb> getLacErbCollection() {
        return lacErbCollection;
    }

    public void setLacErbCollection(Collection<LacErb> lacErbCollection) {
        this.lacErbCollection = lacErbCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoModalidadeErb != null ? ideTipoModalidadeErb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoModalidadeErb)) {
            return false;
        }
        TipoModalidadeErb other = (TipoModalidadeErb) object;
        if ((this.ideTipoModalidadeErb == null && other.ideTipoModalidadeErb != null) || (this.ideTipoModalidadeErb != null && !this.ideTipoModalidadeErb.equals(other.ideTipoModalidadeErb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoModalidadeErb[ ideTipoModalidadeErb=" + ideTipoModalidadeErb + " ]";
    }
    
}
