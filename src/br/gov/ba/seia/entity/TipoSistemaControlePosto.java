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
@Table(name = "tipo_sistema_controle_posto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSistemaControlePosto.findAll", query = "SELECT t FROM TipoSistemaControlePosto t"),
    @NamedQuery(name = "TipoSistemaControlePosto.findByIdeTipoSistemaControlePosto", query = "SELECT t FROM TipoSistemaControlePosto t WHERE t.ideTipoSistemaControlePosto = :ideTipoSistemaControlePosto"),
    @NamedQuery(name = "TipoSistemaControlePosto.findByDscTipoSistemaControlePosto", query = "SELECT t FROM TipoSistemaControlePosto t WHERE t.dscTipoSistemaControlePosto = :dscTipoSistemaControlePosto")})
public class TipoSistemaControlePosto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_sistema_controle_posto")
    private Integer ideTipoSistemaControlePosto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_tipo_sistema_controle_posto")
    private String dscTipoSistemaControlePosto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoSistemaControlePosto")
    private Collection<SistemaControlePosto> sistemaControlePostoCollection;

    public TipoSistemaControlePosto() {
    }

    public TipoSistemaControlePosto(Integer ideTipoSistemaControlePosto) {
        this.ideTipoSistemaControlePosto = ideTipoSistemaControlePosto;
    }

    public TipoSistemaControlePosto(Integer ideTipoSistemaControlePosto, String dscTipoSistemaControlePosto) {
        this.ideTipoSistemaControlePosto = ideTipoSistemaControlePosto;
        this.dscTipoSistemaControlePosto = dscTipoSistemaControlePosto;
    }

    public Integer getIdeTipoSistemaControlePosto() {
        return ideTipoSistemaControlePosto;
    }

    public void setIdeTipoSistemaControlePosto(Integer ideTipoSistemaControlePosto) {
        this.ideTipoSistemaControlePosto = ideTipoSistemaControlePosto;
    }

    public String getDscTipoSistemaControlePosto() {
        return dscTipoSistemaControlePosto;
    }

    public void setDscTipoSistemaControlePosto(String dscTipoSistemaControlePosto) {
        this.dscTipoSistemaControlePosto = dscTipoSistemaControlePosto;
    }

    @XmlTransient
    public Collection<SistemaControlePosto> getSistemaControlePostoCollection() {
        return sistemaControlePostoCollection;
    }

    public void setSistemaControlePostoCollection(Collection<SistemaControlePosto> sistemaControlePostoCollection) {
        this.sistemaControlePostoCollection = sistemaControlePostoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoSistemaControlePosto != null ? ideTipoSistemaControlePosto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoSistemaControlePosto)) {
            return false;
        }
        TipoSistemaControlePosto other = (TipoSistemaControlePosto) object;
        if ((this.ideTipoSistemaControlePosto == null && other.ideTipoSistemaControlePosto != null) || (this.ideTipoSistemaControlePosto != null && !this.ideTipoSistemaControlePosto.equals(other.ideTipoSistemaControlePosto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(this.ideTipoSistemaControlePosto);
    }
    
}
