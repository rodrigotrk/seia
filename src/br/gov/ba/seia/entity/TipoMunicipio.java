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

@Entity
@Table(name = "tipo_municipio", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_tipo_municipio"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoMunicipio.findAll", query = "SELECT t FROM TipoMunicipio t"),
    @NamedQuery(name = "TipoMunicipio.findByIdeTipoMunicipio", query = "SELECT t FROM TipoMunicipio t WHERE t.ideTipoMunicipio = :ideTipoMunicipio"),
    @NamedQuery(name = "TipoMunicipio.findByDscTipoMunicipio", query = "SELECT t FROM TipoMunicipio t WHERE t.dscTipoMunicipio = :dscTipoMunicipio")})
public class TipoMunicipio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_municipio", nullable = false)
    private Integer ideTipoMunicipio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_tipo_municipio", nullable = false, length = 100)
    private String dscTipoMunicipio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoMunicipio", fetch = FetchType.LAZY)
    private Collection<Municipio> municipioCollection;

    public TipoMunicipio() {
    }

    public TipoMunicipio(Integer ideTipoMunicipio) {
        this.ideTipoMunicipio = ideTipoMunicipio;
    }

    public TipoMunicipio(Integer ideTipoMunicipio, String dscTipoMunicipio) {
        this.ideTipoMunicipio = ideTipoMunicipio;
        this.dscTipoMunicipio = dscTipoMunicipio;
    }

    public Integer getIdeTipoMunicipio() {
        return ideTipoMunicipio;
    }

    public void setIdeTipoMunicipio(Integer ideTipoMunicipio) {
        this.ideTipoMunicipio = ideTipoMunicipio;
    }

    public String getDscTipoMunicipio() {
        return dscTipoMunicipio;
    }

    public void setDscTipoMunicipio(String dscTipoMunicipio) {
        this.dscTipoMunicipio = dscTipoMunicipio;
    }

    @XmlTransient
    public Collection<Municipio> getMunicipioCollection() {
        return municipioCollection;
    }

    public void setMunicipioCollection(Collection<Municipio> municipioCollection) {
        this.municipioCollection = municipioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoMunicipio != null ? ideTipoMunicipio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoMunicipio)) {
            return false;
        }
        TipoMunicipio other = (TipoMunicipio) object;
        if ((this.ideTipoMunicipio == null && other.ideTipoMunicipio != null) || (this.ideTipoMunicipio != null && !this.ideTipoMunicipio.equals(other.ideTipoMunicipio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoMunicipio[ ideTipoMunicipio=" + ideTipoMunicipio + " ]";
    }
    
}
