package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_insumo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_insumo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoInsumo.findAll", query = "SELECT t FROM TipoInsumo t"),
    @NamedQuery(name = "TipoInsumo.findByIdeTipoInsumo", query = "SELECT t FROM TipoInsumo t WHERE t.ideTipoInsumo = :ideTipoInsumo"),
    @NamedQuery(name = "TipoInsumo.findByNomTipoInsumo", query = "SELECT t FROM TipoInsumo t WHERE t.nomTipoInsumo = :nomTipoInsumo")})
public class TipoInsumo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_insumo", nullable = false)
    private Integer ideTipoInsumo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nom_tipo_insumo", nullable = false, length = 30)
    private String nomTipoInsumo;

    public TipoInsumo() {
    }

    public TipoInsumo(Integer ideTipoInsumo) {
        this.ideTipoInsumo = ideTipoInsumo;
    }

    public TipoInsumo(Integer ideTipoInsumo, String nomTipoInsumo) {
        this.ideTipoInsumo = ideTipoInsumo;
        this.nomTipoInsumo = nomTipoInsumo;
    }

    public Integer getIdeTipoInsumo() {
        return ideTipoInsumo;
    }

    public void setIdeTipoInsumo(Integer ideTipoInsumo) {
        this.ideTipoInsumo = ideTipoInsumo;
    }

    public String getNomTipoInsumo() {
        return nomTipoInsumo;
    }

    public void setNomTipoInsumo(String nomTipoInsumo) {
        this.nomTipoInsumo = nomTipoInsumo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoInsumo != null ? ideTipoInsumo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoInsumo)) {
            return false;
        }
        TipoInsumo other = (TipoInsumo) object;
        if ((this.ideTipoInsumo == null && other.ideTipoInsumo != null) || (this.ideTipoInsumo != null && !this.ideTipoInsumo.equals(other.ideTipoInsumo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoInsumo[ ideTipoInsumo=" + ideTipoInsumo + " ]";
    }
    
}
