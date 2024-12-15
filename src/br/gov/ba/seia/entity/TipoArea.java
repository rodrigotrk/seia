package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MJunior
 */
@Entity
@Table(name = "tipo_area")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoArea.findAll", query = "SELECT t FROM TipoArea t"),
    @NamedQuery(name = "TipoArea.findByIdeTipoArea", query = "SELECT t FROM TipoArea t WHERE t.ideTipoArea = :ideTipoArea"),
    @NamedQuery(name = "TipoArea.findByDscTipoArea", query = "SELECT t FROM TipoArea t WHERE t.dscTipoArea = :dscTipoArea")})
public class TipoArea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_tipo_area", nullable = false)
    private Integer ideTipoArea;
    @Basic(optional = false)
    @Column(name = "dsc_tipo_area", nullable = false, length = 50)
    private String dscTipoArea;

    public TipoArea() {
    }

    public TipoArea(Integer ideTipoArea) {
        this.ideTipoArea = ideTipoArea;
    }

    public TipoArea(Integer ideTipoArea, String dscTipoArea) {
        this.ideTipoArea = ideTipoArea;
        this.dscTipoArea = dscTipoArea;
    }

    public Integer getIdeTipoArea() {
        return ideTipoArea;
    }

    public void setIdeTipoArea(Integer ideTipoArea) {
        this.ideTipoArea = ideTipoArea;
    }

    public String getDscTipoArea() {
        return dscTipoArea;
    }

    public void setDscTipoArea(String dscTipoArea) {
        this.dscTipoArea = dscTipoArea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoArea != null ? ideTipoArea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoArea)) {
            return false;
        }
        TipoArea other = (TipoArea) object;
        if ((this.ideTipoArea == null && other.ideTipoArea != null) || (this.ideTipoArea != null && !this.ideTipoArea.equals(other.ideTipoArea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ideTipoArea.toString();
    }
    
}
