package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import flexjson.JSON;


/**
 * The persistent class for the tipo_alerta database table.
 * 
 */
@Entity
@Table(name="tipo_alerta")
public class TipoAlerta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_tipo_alerta", nullable = false)
	private Integer ideTipoAlerta;

	@Column(name="dsc_tipo_alerta", nullable = false, length = 50)
	private String dscTipoAlerta;

	

    public TipoAlerta() {
    }

	public TipoAlerta(Integer ideTipoAlerta) {
		this.ideTipoAlerta = ideTipoAlerta;
	}

	public Integer getIdeTipoAlerta() {
		return this.ideTipoAlerta;
	}

	public void setIdeTipoAlerta(Integer ideTipoAlerta) {
		this.ideTipoAlerta = ideTipoAlerta;
	}

	@JSON(include = false)
	public String getDscTipoAlerta() {
		return this.dscTipoAlerta;
	}

	public void setDscTipoAlerta(String dscTipoAlerta) {
		this.dscTipoAlerta = dscTipoAlerta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoAlerta == null) ? 0 : ideTipoAlerta.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoAlerta other = (TipoAlerta) obj;
		if (ideTipoAlerta == null) {
			if (other.ideTipoAlerta != null)
				return false;
		} else if (!ideTipoAlerta.equals(other.ideTipoAlerta))
			return false;
		return true;
	}
	
	
}