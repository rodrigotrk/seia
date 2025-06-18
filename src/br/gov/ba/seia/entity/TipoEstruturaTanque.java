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
@Table(name = "tipo_estrutura_tanque")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoEstruturaTanque.findAll", query = "SELECT t FROM TipoEstruturaTanque t"),
    @NamedQuery(name = "TipoEstruturaTanque.findByIdeTipoEstruturaTanque", query = "SELECT t FROM TipoEstruturaTanque t WHERE t.ideTipoEstruturaTanque = :ideTipoEstruturaTanque"),
    @NamedQuery(name = "TipoEstruturaTanque.findByDscTipoEstruturaTanque", query = "SELECT t FROM TipoEstruturaTanque t WHERE t.dscTipoEstruturaTanque = :dscTipoEstruturaTanque")})
public class TipoEstruturaTanque implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_tipo_estrutura_tanque")
    private Integer ideTipoEstruturaTanque;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_tipo_estrutura_tanque")
    private String dscTipoEstruturaTanque;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoEstruturaTanque")
    private Collection<PostoCombustivelTanque> postoCombustivelTanqueCollection;

    public TipoEstruturaTanque() {
    }

    public TipoEstruturaTanque(Integer ideTipoEstruturaTanque) {
        this.ideTipoEstruturaTanque = ideTipoEstruturaTanque;
    }

    public TipoEstruturaTanque(Integer ideTipoEstruturaTanque, String dscTipoEstruturaTanque) {
        this.ideTipoEstruturaTanque = ideTipoEstruturaTanque;
        this.dscTipoEstruturaTanque = dscTipoEstruturaTanque;
    }

    public Integer getIdeTipoEstruturaTanque() {
        return ideTipoEstruturaTanque;
    }

    public void setIdeTipoEstruturaTanque(Integer ideTipoEstruturaTanque) {
        this.ideTipoEstruturaTanque = ideTipoEstruturaTanque;
    }

    public String getDscTipoEstruturaTanque() {
        return dscTipoEstruturaTanque;
    }

    public void setDscTipoEstruturaTanque(String dscTipoEstruturaTanque) {
        this.dscTipoEstruturaTanque = dscTipoEstruturaTanque;
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
        hash += (ideTipoEstruturaTanque != null ? ideTipoEstruturaTanque.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoEstruturaTanque)) {
            return false;
        }
        TipoEstruturaTanque other = (TipoEstruturaTanque) object;
        if ((this.ideTipoEstruturaTanque == null && other.ideTipoEstruturaTanque != null) || (this.ideTipoEstruturaTanque != null && !this.ideTipoEstruturaTanque.equals(other.ideTipoEstruturaTanque))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TipoEstruturaTanque[ ideTipoEstruturaTanque=" + ideTipoEstruturaTanque + " ]";
    }
    
}
