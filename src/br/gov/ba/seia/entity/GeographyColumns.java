package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "geography_columns")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GeographyColumns.findAll", query = "SELECT g FROM GeographyColumns g"),
    @NamedQuery(name = "GeographyColumns.findByFTableCatalog", query = "SELECT g FROM GeographyColumns g WHERE g.fTableCatalog = :fTableCatalog"),
    @NamedQuery(name = "GeographyColumns.findByFTableSchema", query = "SELECT g FROM GeographyColumns g WHERE g.fTableSchema = :fTableSchema"),
    @NamedQuery(name = "GeographyColumns.findByFTableName", query = "SELECT g FROM GeographyColumns g WHERE g.fTableName = :fTableName"),
    @NamedQuery(name = "GeographyColumns.findByFGeographyColumn", query = "SELECT g FROM GeographyColumns g WHERE g.fGeographyColumn = :fGeographyColumn"),
    @NamedQuery(name = "GeographyColumns.findByCoordDimension", query = "SELECT g FROM GeographyColumns g WHERE g.coordDimension = :coordDimension"),
    @NamedQuery(name = "GeographyColumns.findBySrid", query = "SELECT g FROM GeographyColumns g WHERE g.srid = :srid"),
    @NamedQuery(name = "GeographyColumns.findByType", query = "SELECT g FROM GeographyColumns g WHERE g.type = :type")})
public class GeographyColumns implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 2147483647)
    @Column(name = "f_table_catalog", length = 2147483647)
    private String fTableCatalog;
    @Size(max = 2147483647)
    @Column(name = "f_table_schema", length = 2147483647)
    private String fTableSchema;
    @Size(max = 2147483647)
    @Column(name = "f_table_name", length = 2147483647)
    private String fTableName;
    @Size(max = 2147483647)
    @Column(name = "f_geography_column", length = 2147483647)
    private String fGeographyColumn;
    @Column(name = "coord_dimension")
    private Integer coordDimension;
    @Column(name = "srid")
    private Integer srid;
    @Size(max = 2147483647)
    @Column(name = "type", length = 2147483647)
    private String type;

    public GeographyColumns() {
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

    public String getFGeographyColumn() {
        return fGeographyColumn;
    }

    public void setFGeographyColumn(String fGeographyColumn) {
        this.fGeographyColumn = fGeographyColumn;
    }

    public Integer getCoordDimension() {
        return coordDimension;
    }

    public void setCoordDimension(Integer coordDimension) {
        this.coordDimension = coordDimension;
    }

    public Integer getSrid() {
        return srid;
    }

    public void setSrid(Integer srid) {
        this.srid = srid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
