package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author carlos.sousa
 */
@Embeddable
public class GeometryColumnsPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 9L;
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "f_table_catalog", nullable = false, length = 256)
    private String fTableCatalog;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "f_table_schema", nullable = false, length = 256)
    private String fTableSchema;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "f_table_name", nullable = false, length = 256)
    private String fTableName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "f_geometry_column", nullable = false, length = 256)
    private String fGeometryColumn;

    public GeometryColumnsPK() {
    }

    public GeometryColumnsPK(String fTableCatalog, String fTableSchema, String fTableName, String fGeometryColumn) {
        this.fTableCatalog = fTableCatalog;
        this.fTableSchema = fTableSchema;
        this.fTableName = fTableName;
        this.fGeometryColumn = fGeometryColumn;
    }

    public String getFTableCatalog() {
        return fTableCatalog;
    }

    public void setFTableCatalog(String fTableCatalog) {
        this.fTableCatalog = fTableCatalog;
    }

    public String getFTableSchema() {
        return fTableSchema;
    }

    public void setFTableSchema(String fTableSchema) {
        this.fTableSchema = fTableSchema;
    }

    public String getFTableName() {
        return fTableName;
    }

    public void setFTableName(String fTableName) {
        this.fTableName = fTableName;
    }

    public String getFGeometryColumn() {
        return fGeometryColumn;
    }

    public void setFGeometryColumn(String fGeometryColumn) {
        this.fGeometryColumn = fGeometryColumn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fTableCatalog != null ? fTableCatalog.hashCode() : 0);
        hash += (fTableSchema != null ? fTableSchema.hashCode() : 0);
        hash += (fTableName != null ? fTableName.hashCode() : 0);
        hash += (fGeometryColumn != null ? fGeometryColumn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof GeometryColumnsPK)) {
            return false;
        }
        GeometryColumnsPK other = (GeometryColumnsPK) object;
        if ((this.fTableCatalog == null && other.fTableCatalog != null) || (this.fTableCatalog != null && !this.fTableCatalog.equals(other.fTableCatalog))) {
            return false;
        }
        if ((this.fTableSchema == null && other.fTableSchema != null) || (this.fTableSchema != null && !this.fTableSchema.equals(other.fTableSchema))) {
            return false;
        }
        if ((this.fTableName == null && other.fTableName != null) || (this.fTableName != null && !this.fTableName.equals(other.fTableName))) {
            return false;
        }
        if ((this.fGeometryColumn == null && other.fGeometryColumn != null) || (this.fGeometryColumn != null && !this.fGeometryColumn.equals(other.fGeometryColumn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.GeometryColumnsPK[ fTableCatalog=" + fTableCatalog + ", fTableSchema=" + fTableSchema + ", fTableName=" + fTableName + ", fGeometryColumn=" + fGeometryColumn + " ]";
    }
    
}
