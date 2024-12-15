package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * The primary key class for the base_operacional_controle_ambiental database table.
 * 
 */
@Embeddable
public class BaseOperacionalControleAmbientalPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
    @NotNull
	@Column(name="ide_base_operacional", nullable = false)
	private Integer ideBaseOperacional;
	
	@Basic(optional = false)
    @NotNull
	@Column(name="ide_tipo_controle_ambiental", nullable = false)
	private Integer ideTipoControleAmbiental;

    public BaseOperacionalControleAmbientalPK() {
    }
	public Integer getIdeBaseOperacional() {
		return this.ideBaseOperacional;
	}
	
	public BaseOperacionalControleAmbientalPK(Integer ideTipoControleAmbiental) {
		this.ideTipoControleAmbiental = ideTipoControleAmbiental;
	}
	
	
	public BaseOperacionalControleAmbientalPK(Integer ideBaseOperacional, Integer ideTipoControleAmbiental) {
		this.ideBaseOperacional = ideBaseOperacional;
		this.ideTipoControleAmbiental = ideTipoControleAmbiental;
	}
	public void setIdeBaseOperacional(Integer ideBaseOperacional) {
		this.ideBaseOperacional = ideBaseOperacional;
	}
	public Integer getIdeTipoControleAmbiental() {
		return this.ideTipoControleAmbiental;
	}
	public void setIdeTipoControleAmbiental(Integer ideTipoControleAmbiental) {
		this.ideTipoControleAmbiental = ideTipoControleAmbiental;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BaseOperacionalControleAmbientalPK)) {
			return false;
		}
		BaseOperacionalControleAmbientalPK castOther = (BaseOperacionalControleAmbientalPK)other;
		return 
			this.ideBaseOperacional.equals(castOther.ideBaseOperacional)
			&& this.ideTipoControleAmbiental.equals(castOther.ideTipoControleAmbiental);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideBaseOperacional.hashCode();
		hash = hash * prime + this.ideTipoControleAmbiental.hashCode();
		
		return hash;
    }
}