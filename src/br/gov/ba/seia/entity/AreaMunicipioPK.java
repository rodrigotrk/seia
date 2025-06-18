package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class AreaMunicipioPK implements Serializable{
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_area")
    private int ideArea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_municipio")
    private int ideMunicipio;
    
    
	public int getIdeArea() {
		return ideArea;
	}
	public void setIdeArea(int ideArea) {
		this.ideArea = ideArea;
	}
	public int getIdeMunicipio() {
		return ideMunicipio;
	}
	public void setIdeMunicipio(int ideMunicipio) {
		this.ideMunicipio = ideMunicipio;
	}
    
	 @Override
	    public int hashCode() {
	        int hash = 0;
	        hash += (int) ideArea;
	        hash += (int) ideMunicipio;
	        return hash;
	    }

	    @Override
	    public boolean equals(Object object) {
	        
	        if (!(object instanceof AreaMunicipioPK)) {
	            return false;
	        }
	        AreaMunicipioPK other = (AreaMunicipioPK) object;
	        if (this.ideArea != other.ideArea) {
	            return false;
	        }
	        if (this.ideMunicipio != other.ideMunicipio) {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public String toString() {
	        return "entity.AreaMunicipioPK[ ideArea=" + ideArea + ", ideMunicipio=" + ideMunicipio + " ]";
	    }

}
