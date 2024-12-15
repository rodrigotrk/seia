package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author luis
 */
@Entity
@Table(name = "tipo_parede_tanque")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoParedeTanque.findAll", query = "SELECT t FROM TipoParedeTanque t"),
    @NamedQuery(name = "TipoParedeTanque.findByIdeTipoParedeTanque", query = "SELECT t FROM TipoParedeTanque t WHERE t.ideTipoParedeTanque = :ideTipoParedeTanque"),
    @NamedQuery(name = "TipoParedeTanque.findByDscTipoParedeTanque", query = "SELECT t FROM TipoParedeTanque t WHERE t.dscTipoParedeTanque = :dscTipoParedeTanque")})
public class TipoParedeTanque implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_tipo_parede_tanque")
    private Integer ideTipoParedeTanque;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_tipo_parede_tanque")
    private String dscTipoParedeTanque;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoParedeTanque")
    private Collection<PostoCombustivelTanque> postoCombustivelTanqueCollection;

    public TipoParedeTanque() {
    }

    public TipoParedeTanque(Integer ideTipoParedeTanque) {
        this.ideTipoParedeTanque = ideTipoParedeTanque;
    }

    public TipoParedeTanque(Integer ideTipoParedeTanque, String dscTipoParedeTanque) {
        this.ideTipoParedeTanque = ideTipoParedeTanque;
        this.dscTipoParedeTanque = dscTipoParedeTanque;
    }

    public Integer getIdeTipoParedeTanque() {
        return ideTipoParedeTanque;
    }

    public void setIdeTipoParedeTanque(Integer ideTipoParedeTanque) {
        this.ideTipoParedeTanque = ideTipoParedeTanque;
    }

    public String getDscTipoParedeTanque() {
        return dscTipoParedeTanque;
    }

    public void setDscTipoParedeTanque(String dscTipoParedeTanque) {
        this.dscTipoParedeTanque = dscTipoParedeTanque;
    }

    @XmlTransient
    public Collection<PostoCombustivelTanque> getPostoCombustivelTanqueCollection() {
        return postoCombustivelTanqueCollection;
    }

    public void setPostoCombustivelTanqueCollection(Collection<PostoCombustivelTanque> postoCombustivelTanqueCollection) {
        this.postoCombustivelTanqueCollection = postoCombustivelTanqueCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoParedeTanque != null ? ideTipoParedeTanque.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoParedeTanque)) {
            return false;
        }
        TipoParedeTanque other = (TipoParedeTanque) object;
        if ((this.ideTipoParedeTanque == null && other.ideTipoParedeTanque != null) || (this.ideTipoParedeTanque != null && !this.ideTipoParedeTanque.equals(other.ideTipoParedeTanque))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TipoParedeTanque[ ideTipoParedeTanque=" + ideTipoParedeTanque + " ]";
    }
    
}
