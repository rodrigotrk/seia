package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the base_operacional_servico database table.
 * 
 */
@Embeddable
public class BaseOperacionalServicoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_base_operacional")
	private Integer ideBaseOperacional;

	@Column(name="ide_tipo_servico_base")
	private Integer ideTipoServicoBase;

    public BaseOperacionalServicoPK() {
    }
    
	public BaseOperacionalServicoPK(Integer ideBaseOperacional, Integer ideTipoServicoBase) {
		this.ideBaseOperacional = ideBaseOperacional;
		this.ideTipoServicoBase = ideTipoServicoBase;
	}

	public Integer getIdeBaseOperacional() {
		return this.ideBaseOperacional;
	}
	public void setIdeBaseOperacional(Integer ideBaseOperacional) {
		this.ideBaseOperacional = ideBaseOperacional;
	}
	public Integer getIdeTipoServicoBase() {
		return this.ideTipoServicoBase;
	}
	public void setIdeTipoServicoBase(Integer ideTipoServicoBase) {
		this.ideTipoServicoBase = ideTipoServicoBase;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BaseOperacionalServicoPK)) {
			return false;
		}
		BaseOperacionalServicoPK castOther = (BaseOperacionalServicoPK)other;
		return 
			this.ideBaseOperacional.equals(castOther.ideBaseOperacional)
			&& this.ideTipoServicoBase.equals(castOther.ideTipoServicoBase);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideBaseOperacional.hashCode();
		hash = hash * prime + this.ideTipoServicoBase.hashCode();
		
		return hash;
    }
}