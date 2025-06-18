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


@Entity
@Table(name = "tipo_area_municipio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoAreaMunicipio.findAll", query = "SELECT t FROM TipoAreaMunicipio t"),
    @NamedQuery(name = "TipoAreaMunicipio.findByIdeTipoAreaMunicipio", query = "SELECT t FROM TipoAreaMunicipio t WHERE t.ideTipoAreaMunicipio = :ideTipoAreaMunicipio"),
    @NamedQuery(name = "TipoAreaMunicipio.findByNomTipoAreaMunicipio", query = "SELECT t FROM TipoAreaMunicipio t WHERE t.nomTipoAreaMunicipio = :nomTipoAreaMunicipio")})
public class TipoAreaMunicipio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_tipo_area_municipio", nullable = false)
    private Integer ideTipoAreaMunicipio;
    @Basic(optional = false)
    @Column(name = "nom_tipo_area_municipio", nullable = false, length = 50)
    private String nomTipoAreaMunicipio;
    
    
    public TipoAreaMunicipio() {}
    
    public TipoAreaMunicipio(Integer ideTipoAreaMunicipio) {
    	this.ideTipoAreaMunicipio =ideTipoAreaMunicipio;
    }
    
    public Integer getIdeTipoAreaMunicipio() {
		return ideTipoAreaMunicipio;
	}
	public void setIdeTipoAreaMunicipio(Integer ideTipoAreaMunicipio) {
		this.ideTipoAreaMunicipio = ideTipoAreaMunicipio;
	}
	public String getNomTipoAreaMunicipio() {
		return nomTipoAreaMunicipio;
	}
	public void setNomTipoAreaMunicipio(String nomTipoAreaMunicipio) {
		this.nomTipoAreaMunicipio = nomTipoAreaMunicipio;
	}
	
	
	  @Override
	    public boolean equals(Object object) {
	        
	        if (!(object instanceof TipoAreaMunicipio)) {
	            return false;
	        }
	        TipoAreaMunicipio other = (TipoAreaMunicipio) object;
	        if ((this.ideTipoAreaMunicipio == null && other.ideTipoAreaMunicipio != null) || (this.ideTipoAreaMunicipio != null && !this.ideTipoAreaMunicipio.equals(other.ideTipoAreaMunicipio))) {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public String toString() {
	        return ideTipoAreaMunicipio.toString();
	    }
	
	
}