package br.gov.ba.ws.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name="tipo_dispositivo")
public class TipoDispositivo implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name="ide_tipo_dispositivo")
	private Integer ideTipoDispositivo;
	
	@NotNull
	@Column(name="dsc_tipo_dispositivo")
	private String dscTipoDispositivo;
	
	@Column(name="ind_ativo")
	private boolean indAtivo;
	
	public TipoDispositivo(Integer tipoDispositivo){
		this.ideTipoDispositivo = tipoDispositivo;
	}
	public TipoDispositivo(){
	}
	
	public Integer getIdeTipoDispositivo() {
		return ideTipoDispositivo;
	}

	public void setIdeTipoDispositivo(Integer ideTipoDispositivo) {
		this.ideTipoDispositivo = ideTipoDispositivo;
	}

	public String getDscTipoDispositivo() {
		return dscTipoDispositivo;
	}

	public void setDscTipoDispositivo(String dscTipoDispositivo) {
		this.dscTipoDispositivo = dscTipoDispositivo;
	}

	public boolean getIndAtivo(){
		return indAtivo;
	}
	
	public void setIndAtivo(boolean indAtivo){
		this.indAtivo = indAtivo;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dscTipoDispositivo == null) ? 0 : dscTipoDispositivo
						.hashCode());
		result = prime
				* result
				+ ((ideTipoDispositivo == null) ? 0 : ideTipoDispositivo
						.hashCode());
		result = prime * result + (indAtivo ? 1231 : 1237);
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
		TipoDispositivo other = (TipoDispositivo) obj;
		if (dscTipoDispositivo == null) {
			if (other.dscTipoDispositivo != null)
				return false;
		} else if (!dscTipoDispositivo.equals(other.dscTipoDispositivo))
			return false;
		if (ideTipoDispositivo == null) {
			if (other.ideTipoDispositivo != null)
				return false;
		} else if (!ideTipoDispositivo.equals(other.ideTipoDispositivo))
			return false;
		if (indAtivo != other.indAtivo)
			return false;
		return true;
	}

	public Long getId() {
		return new Long(this.ideTipoDispositivo);
	}
}