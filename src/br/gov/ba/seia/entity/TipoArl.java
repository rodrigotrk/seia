package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tipo_arl", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_tipo_arl"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoArl.findAll", query = "SELECT t FROM TipoArl t"),
    @NamedQuery(name = "TipoArl.findByIdeTipoArl", query = "SELECT t FROM TipoArl t WHERE t.ideTipoArl = :ideTipoArl"),
    @NamedQuery(name = "TipoArl.findByDscTipoArl", query = "SELECT t FROM TipoArl t WHERE t.dscTipoArl = :dscTipoArl"),
    @NamedQuery(name = "TipoArl.findByIndExcluido", query = "SELECT t FROM TipoArl t WHERE t.indExcluido = :indExcluido"),
    @NamedQuery(name = "TipoArl.findByDtcCriacao", query = "SELECT t FROM TipoArl t WHERE t.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "TipoArl.findByDtcExclusao", query = "SELECT t FROM TipoArl t WHERE t.dtcExclusao = :dtcExclusao")})
public class TipoArl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_arl", nullable = false)
    private Integer ideTipoArl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_tipo_arl", nullable = false, length = 100)
    private String dscTipoArl;
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

    public TipoArl() {
    }

    public TipoArl(Integer ideTipoArl) {
        this.ideTipoArl = ideTipoArl;
    }

    public TipoArl(Integer ideTipoArl, String dscTipoArl, boolean indExcluido, Date dtcCriacao) {
        this.ideTipoArl = ideTipoArl;
        this.dscTipoArl = dscTipoArl;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeTipoArl() {
        return ideTipoArl;
    }

    public void setIdeTipoArl(Integer ideTipoArl) {
        this.ideTipoArl = ideTipoArl;
    }

    public String getDscTipoArl() {
        return dscTipoArl;
    }

    public void setDscTipoArl(String dscTipoArl) {
        this.dscTipoArl = dscTipoArl;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoArl != null ? ideTipoArl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoArl)) {
            return false;
        }
        TipoArl other = (TipoArl) object;
        if ((this.ideTipoArl == null && other.ideTipoArl != null) || (this.ideTipoArl != null && !this.ideTipoArl.equals(other.ideTipoArl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.valueOf(ideTipoArl);
    }
    
}
