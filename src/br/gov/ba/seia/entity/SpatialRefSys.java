package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "spatial_ref_sys")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SpatialRefSys.findAll", query = "SELECT s FROM SpatialRefSys s"),
    @NamedQuery(name = "SpatialRefSys.findBySrid", query = "SELECT s FROM SpatialRefSys s WHERE s.srid = :srid"),
    @NamedQuery(name = "SpatialRefSys.findByAuthName", query = "SELECT s FROM SpatialRefSys s WHERE s.authName = :authName"),
    @NamedQuery(name = "SpatialRefSys.findByAuthSrid", query = "SELECT s FROM SpatialRefSys s WHERE s.authSrid = :authSrid"),
    @NamedQuery(name = "SpatialRefSys.findBySrtext", query = "SELECT s FROM SpatialRefSys s WHERE s.srtext = :srtext"),
    @NamedQuery(name = "SpatialRefSys.findByProj4text", query = "SELECT s FROM SpatialRefSys s WHERE s.proj4text = :proj4text")})
public class SpatialRefSys implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "srid", nullable = false)
    private Integer srid;
    @Size(max = 256)
    @Column(name = "auth_name", length = 256)
    private String authName;
    @Column(name = "auth_srid")
    private Integer authSrid;
    @Size(max = 2048)
    @Column(name = "srtext", length = 2048)
    private String srtext;
    @Size(max = 2048)
    @Column(name = "proj4text", length = 2048)
    private String proj4text;

    public SpatialRefSys() {
    }

    public SpatialRefSys(Integer srid) {
        this.srid = srid;
    }

    public Integer getSrid() {
        return srid;
    }

    public void setSrid(Integer srid) {
        this.srid = srid;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public Integer getAuthSrid() {
        return authSrid;
    }

    public void setAuthSrid(Integer authSrid) {
        this.authSrid = authSrid;
    }

    public String getSrtext() {
        return srtext;
    }

    public void setSrtext(String srtext) {
        this.srtext = srtext;
    }

    public String getProj4text() {
        return proj4text;
    }

    public void setProj4text(String proj4text) {
        this.proj4text = proj4text;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (srid != null ? srid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof SpatialRefSys)) {
            return false;
        }
        SpatialRefSys other = (SpatialRefSys) object;
        if ((this.srid == null && other.srid != null) || (this.srid != null && !this.srid.equals(other.srid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.SpatialRefSys[ srid=" + srid + " ]";
    }
    
}
