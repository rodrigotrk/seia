package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_volume_florestal_remanescente")
@XmlRootElement
public class TipoVolumeFlorestalRemanescente implements Serializable, BaseEntity {

	private static final long serialVersionUID = 3478012313229468530L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_VOLUME_FLORESTAL_REMANESCENTE_SEQ")
	@SequenceGenerator(name = "TIPO_VOLUME_FLORESTAL_REMANESCENTE_SEQ", sequenceName = "TIPO_VOLUME_FLORESTAL_REMANESCENTE_SEQ", allocationSize = 1)
	@NotNull
	@Column(name = "ide_tipo_volume_florestal_remanescente")
	private Integer ideTipoVolumeFlorestalRemanescente;
	
	@Column(name = "nom_tipo_volume_florestal_remanescente", nullable=false)
	private String nomTipoVolumeFlorestalRemanescente;

	public Integer getIdeTipoVolumeFlorestalRemanescente() {
		return ideTipoVolumeFlorestalRemanescente;
	}

	public void setIdeTipoVolumeFlorestalRemanescente(
			Integer ideTipoVolumeFlorestalRemanescente) {
		this.ideTipoVolumeFlorestalRemanescente = ideTipoVolumeFlorestalRemanescente;
	}

	public String getNomTipoVolumeFlorestalRemanescente() {
		return nomTipoVolumeFlorestalRemanescente;
	}

	public void setNomTipoVolumeFlorestalRemanescente(
			String nomTipoVolumeFlorestalRemanescente) {
		this.nomTipoVolumeFlorestalRemanescente = nomTipoVolumeFlorestalRemanescente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoVolumeFlorestalRemanescente == null) ? 0
						: ideTipoVolumeFlorestalRemanescente.hashCode());
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
		TipoVolumeFlorestalRemanescente other = (TipoVolumeFlorestalRemanescente) obj;
		if (ideTipoVolumeFlorestalRemanescente == null) {
			if (other.ideTipoVolumeFlorestalRemanescente != null)
				return false;
		} else if (!ideTipoVolumeFlorestalRemanescente
				.equals(other.ideTipoVolumeFlorestalRemanescente))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoVolumeFlorestalRemanescente);
	}
	
}
