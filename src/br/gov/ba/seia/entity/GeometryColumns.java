package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "geometry_columns")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GeometryColumns.findAll", query = "SELECT g FROM GeometryColumns g"),
    @NamedQuery(name = "GeometryColumns.findByFTableCatalog", query = "SELECT g FROM GeometryColumns g WHERE g.geometryColumnsPK.fTableCatalog = :fTableCatalog"),
    @NamedQuery(name = "GeometryColumns.findByFTableSchema", query = "SELECT g FROM GeometryColumns g WHERE g.geometryColumnsPK.fTableSchema = :fTableSchema"),
    @NamedQuery(name = "GeometryColumns.findByFTableName", query = "SELECT g FROM GeometryColumns g WHERE g.geometryColumnsPK.fTableName = :fTableName"),
    @NamedQuery(name = "GeometryColumns.findByFGeometryColumn", query = "SELECT g FROM GeometryColumns g WHERE g.geometryColumnsPK.fGeometryColumn = :fGeometryColumn"),
    @NamedQuery(name = "GeometryColumns.findByCoordDimension", query = "SELECT g FROM GeometryColumns g WHERE g.coordDimension = :coordDimension"),
    @NamedQuery(name = "GeometryColumns.findBySrid", query = "SELECT g FROM GeometryColumns g WHERE g.srid = :srid"),
    @NamedQuery(name = "GeometryColumns.findByType", query = "SELECT g FROM GeometryColumns g WHERE g.type = :type")})
public class GeometryColumns implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GeometryColumnsPK geometryColumnsPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "coord_dimension", nullable = false)
    private int coordDimension;
    @Basic(optional = false)
    @NotNull
    @Column(name = "srid", nullable = false)
    private int srid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "type", nullable = false, length = 30)
    private String type;

    public GeometryColumns() {
    }

    public GeometryColumns(GeometryColumnsPK geometryColumnsPK) {
        this.geometryColumnsPK = geometryColumnsPK;
    }

    public GeometryColumns(GeometryColumnsPK geometryColumnsPK, int coordDimension, int srid, String type) {
        this.geometryColumnsPK = geometryColumnsPK;
        this.coordDimension = coordDimension;
        this.srid = srid;
        this.type = type;
    }

    public GeometryColumns(String fTableCatalog, String fTableSchema, String fTableName, String fGeometryColumn) {
        this.geometryColumnsPK = new GeometryColumnsPK(fTableCatalog, fTableSchema, fTableName, fGeometryColumn);
    }

    public GeometryColumnsPK getGeometryColumnsPK() {
        return geometryColumnsPK;
    }

    public void setGeometryColumnsPK(GeometryColumnsPK geometryColumnsPK) {
        this.geometryColumnsPK = geometryColumnsPK;
    }

    public int getCoordDimension() {
        return coordDimension;
    }

    public void setCoordDimension(int coordDimension) {
        this.coordDimension = coordDimension;
    }

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (geometryColumnsPK != null ? geometryColumnsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof GeometryColumns)) {
            return false;
        }
        GeometryColumns other = (GeometryColumns) object;
        if ((this.geometryColumnsPK == null && other.geometryColumnsPK != null) || (this.geometryColumnsPK != null && !this.geometryColumnsPK.equals(other.geometryColumnsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.GeometryColumns[ geometryColumnsPK=" + geometryColumnsPK + " ]";
    }
    
}
