package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="tipo_lote_boleto")
public class TipoLoteBoleto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_tipo_lote_boleto", nullable = false)
	private Integer ideTipoLoteBoleto;

	@Column(name="dsc_tipo_lote_boleto", nullable = false, length = 50)
	private String dscTipoLoteBoleto;


    public TipoLoteBoleto() {
    }
    
    public TipoLoteBoleto(Integer ideTipoLoteBoleto) {
    	this.ideTipoLoteBoleto = ideTipoLoteBoleto;
    }
	
	public Integer getIdeTipoLoteBoleto() {
		return ideTipoLoteBoleto;
	}
	public void setIdeTipoLoteBoleto(Integer ideTipoLoteBoleto) {
		this.ideTipoLoteBoleto = ideTipoLoteBoleto;
	}

	public String getDscTipoLoteBoleto() {
		return dscTipoLoteBoleto;
	}
	public void setDscTipoLoteBoleto(String dscTipoLoteBoleto) {
		this.dscTipoLoteBoleto = dscTipoLoteBoleto;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoLoteBoleto == null) ? 0 : ideTipoLoteBoleto.hashCode());
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
		TipoLoteBoleto other = (TipoLoteBoleto) obj;
		if (ideTipoLoteBoleto == null) {
			if (other.ideTipoLoteBoleto != null)
				return false;
		} else if (!ideTipoLoteBoleto.equals(other.ideTipoLoteBoleto))
			return false;
		return true;
	}
}