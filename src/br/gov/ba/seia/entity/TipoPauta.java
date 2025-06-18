package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_pauta", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_tipo_pauta"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPauta.findAll", query = "SELECT t FROM TipoPauta t"),
    @NamedQuery(name = "TipoPauta.findByIdeTipoPauta", query = "SELECT t FROM TipoPauta t WHERE t.ideTipoPauta = :ideTipoPauta"),
    @NamedQuery(name = "TipoPauta.findByDscTipoPauta", query = "SELECT t FROM TipoPauta t WHERE t.dscTipoPauta = :dscTipoPauta")})
public class TipoPauta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_pauta", nullable = false)
    private Integer ideTipoPauta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_tipo_pauta", nullable = false, length = 50)
    private String dscTipoPauta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoPauta", fetch = FetchType.LAZY)
    private Collection<Pauta> pautaCollection;

    public TipoPauta() {
    }

    public TipoPauta(Integer ideTipoPauta) {
        this.ideTipoPauta = ideTipoPauta;
    }

    public TipoPauta(Integer ideTipoPauta, String dscTipoPauta) {
        this.ideTipoPauta = ideTipoPauta;
        this.dscTipoPauta = dscTipoPauta;
    }

    public Integer getIdeTipoPauta() {
        return ideTipoPauta;
    }

    public void setIdeTipoPauta(Integer ideTipoPauta) {
        this.ideTipoPauta = ideTipoPauta;
    }

    public String getDscTipoPauta() {
        return dscTipoPauta;
    }

    public void setDscTipoPauta(String dscTipoPauta) {
        this.dscTipoPauta = dscTipoPauta;
    }

    @XmlTransient
    public Collection<Pauta> getPautaCollection() {
        return pautaCollection;
    }

    public void setPautaCollection(Collection<Pauta> pautaCollection) {
        this.pautaCollection = pautaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoPauta != null ? ideTipoPauta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoPauta)) {
            return false;
        }
        TipoPauta other = (TipoPauta) object;
        if ((this.ideTipoPauta == null && other.ideTipoPauta != null) || (this.ideTipoPauta != null && !this.ideTipoPauta.equals(other.ideTipoPauta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoPauta[ ideTipoPauta=" + ideTipoPauta + " ]";
    }
    
}
