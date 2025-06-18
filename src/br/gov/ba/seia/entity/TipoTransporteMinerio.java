package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * The persistent class for the tipo_transporte_minerio database table.
 * 
 */
@Entity
@Table(name = "tipo_transporte_minerio")
public class TipoTransporteMinerio implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "TIPO_TRANSPORTE_MINERIO_IDETIPOTRANSPORTEMINERIO_GENERATOR", sequenceName = "TIPO_TRANSPORTE_MINERIO_IDE_TIPO_TRANSPORTE_MINERIO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_TRANSPORTE_MINERIO_IDETIPOTRANSPORTEMINERIO_GENERATOR")
	@Column(name = "ide_tipo_transporte_minerio")
	private Integer ideTipoTransporteMinerio;

	@Column(name = "nom_tipo_transporte_minerio")
	private String nomTipoTransporteMinerio;

	public TipoTransporteMinerio() {
	}

	public Integer getIdeTipoTransporteMinerio() {
		return this.ideTipoTransporteMinerio;
	}

	public void setIdeTipoTransporteMinerio(Integer ideTipoTransporteMinerio) {
		this.ideTipoTransporteMinerio = ideTipoTransporteMinerio;
	}

	public String getNomTipoTransporteMinerio() {
		return this.nomTipoTransporteMinerio;
	}

	public void setNomTipoTransporteMinerio(String nomTipoTransporteMinerio) {
		this.nomTipoTransporteMinerio = nomTipoTransporteMinerio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTipoTransporteMinerio == null) ? 0 : ideTipoTransporteMinerio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		TipoTransporteMinerio other = (TipoTransporteMinerio) obj;
		if(ideTipoTransporteMinerio == null){
			if(other.ideTipoTransporteMinerio != null)
				return false;
		}
		else if(!ideTipoTransporteMinerio.equals(other.ideTipoTransporteMinerio))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoTransporteMinerio);
	}

}