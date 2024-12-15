package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "tipo_delimitacao_terreno", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_tipo_delimitacao_terreno"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoDelimitacaoTerreno.findAll", query = "SELECT t FROM TipoDelimitacaoTerreno t"),
    @NamedQuery(name = "TipoDelimitacaoTerreno.findByIdeTipoDelimitacaoTerreno", query = "SELECT t FROM TipoDelimitacaoTerreno t WHERE t.ideTipoDelimitacaoTerreno = :ideTipoDelimitacaoTerreno"),
    @NamedQuery(name = "TipoDelimitacaoTerreno.findByDscTipoDelimitacaoTerreno", query = "SELECT t FROM TipoDelimitacaoTerreno t WHERE t.dscTipoDelimitacaoTerreno = :dscTipoDelimitacaoTerreno")})
	@NamedNativeQuery(name = "TipoDelimitacaoTerreno.findByIdeLacErb", query = "select tdt.* from lac_erb_tipo_delimitacao td,tipo_delimitacao_terreno tdt where td.ide_tipo_delimitacao_terreno = tdt.ide_tipo_delimitacao_terreno and ide_lac_erb = :pIdeLacErb",resultClass=TipoDelimitacaoTerreno.class)
public class TipoDelimitacaoTerreno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="tipo_delimitacao_terreno_ide_tipo_delimitacao_terreno_seq")    
    @SequenceGenerator(name="tipo_delimitacao_terreno_ide_tipo_delimitacao_terreno_seq", sequenceName="tipo_delimitacao_terreno_ide_tipo_delimitacao_terreno_seq", allocationSize=1)
    @NotNull
    @Column(name = "ide_tipo_delimitacao_terreno", nullable = false)
    private Integer ideTipoDelimitacaoTerreno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_tipo_delimitacao_terreno", nullable = false, length = 50)
    private String dscTipoDelimitacaoTerreno;
    @ManyToMany(mappedBy = "tipoDelimitacaoTerrenoCollection", fetch = FetchType.LAZY)
    private Collection<LacErb> lacErbCollection;

    public TipoDelimitacaoTerreno() {
    }

    public TipoDelimitacaoTerreno(Integer ideTipoDelimitacaoTerreno) {
        this.ideTipoDelimitacaoTerreno = ideTipoDelimitacaoTerreno;
    }

    public TipoDelimitacaoTerreno(Integer ideTipoDelimitacaoTerreno, String dscTipoDelimitacaoTerreno) {
        this.ideTipoDelimitacaoTerreno = ideTipoDelimitacaoTerreno;
        this.dscTipoDelimitacaoTerreno = dscTipoDelimitacaoTerreno;
    }

    public Integer getIdeTipoDelimitacaoTerreno() {
        return ideTipoDelimitacaoTerreno;
    }

    public void setIdeTipoDelimitacaoTerreno(Integer ideTipoDelimitacaoTerreno) {
        this.ideTipoDelimitacaoTerreno = ideTipoDelimitacaoTerreno;
    }

    public String getDscTipoDelimitacaoTerreno() {
        return dscTipoDelimitacaoTerreno;
    }

    public void setDscTipoDelimitacaoTerreno(String dscTipoDelimitacaoTerreno) {
        this.dscTipoDelimitacaoTerreno = dscTipoDelimitacaoTerreno;
    }

    @XmlTransient
    public Collection<LacErb> getLacErbCollection() {
        return Util.isNull(lacErbCollection) ? lacErbCollection = new ArrayList<LacErb>():lacErbCollection;
    }

    public void setLacErbCollection(Collection<LacErb> lacErbCollection) {
        this.lacErbCollection = lacErbCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoDelimitacaoTerreno != null ? ideTipoDelimitacaoTerreno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoDelimitacaoTerreno)) {
            return false;
        }
        TipoDelimitacaoTerreno other = (TipoDelimitacaoTerreno) object;
        if ((this.ideTipoDelimitacaoTerreno == null && other.ideTipoDelimitacaoTerreno != null) || (this.ideTipoDelimitacaoTerreno != null && !this.ideTipoDelimitacaoTerreno.equals(other.ideTipoDelimitacaoTerreno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.valueOf(ideTipoDelimitacaoTerreno);
    }
    
}
