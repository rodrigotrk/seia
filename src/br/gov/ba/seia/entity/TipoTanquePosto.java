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
@Table(name = "tipo_tanque_posto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoTanquePosto.findAll", query = "SELECT t FROM TipoTanquePosto t"),
    @NamedQuery(name = "TipoTanquePosto.findByIdeTipoTanquePosto", query = "SELECT t FROM TipoTanquePosto t WHERE t.ideTipoTanquePosto = :ideTipoTanquePosto"),
    @NamedQuery(name = "TipoTanquePosto.findByDscTipoTanquePosto", query = "SELECT t FROM TipoTanquePosto t WHERE t.dscTipoTanquePosto = :dscTipoTanquePosto")})
public class TipoTanquePosto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_tipo_tanque_posto")
    private Integer ideTipoTanquePosto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_tipo_tanque_posto")
    private String dscTipoTanquePosto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoTanquePosto")
    private Collection<PostoCombustivelTanque> postoCombustivelTanqueCollection;

    public TipoTanquePosto() {
    }

    public TipoTanquePosto(Integer ideTipoTanquePosto) {
        this.ideTipoTanquePosto = ideTipoTanquePosto;
    }

    public TipoTanquePosto(Integer ideTipoTanquePosto, String dscTipoTanquePosto) {
        this.ideTipoTanquePosto = ideTipoTanquePosto;
        this.dscTipoTanquePosto = dscTipoTanquePosto;
    }

    public Integer getIdeTipoTanquePosto() {
        return ideTipoTanquePosto;
    }

    public void setIdeTipoTanquePosto(Integer ideTipoTanquePosto) {
        this.ideTipoTanquePosto = ideTipoTanquePosto;
    }

    public String getDscTipoTanquePosto() {
        return dscTipoTanquePosto;
    }

    public void setDscTipoTanquePosto(String dscTipoTanquePosto) {
        this.dscTipoTanquePosto = dscTipoTanquePosto;
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
        hash += (ideTipoTanquePosto != null ? ideTipoTanquePosto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoTanquePosto)) {
            return false;
        }
        TipoTanquePosto other = (TipoTanquePosto) object;
        if ((this.ideTipoTanquePosto == null && other.ideTipoTanquePosto != null) || (this.ideTipoTanquePosto != null && !this.ideTipoTanquePosto.equals(other.ideTipoTanquePosto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TipoTanquePosto[ ideTipoTanquePosto=" + ideTipoTanquePosto + " ]";
    }
    
}
