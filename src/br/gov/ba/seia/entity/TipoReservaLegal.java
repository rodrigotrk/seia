package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_reserva_legal")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoReservaLegal.findAll", query = "SELECT t FROM TipoReservaLegal t order by t.nomTipoReservaLegal"),
		@NamedQuery(name = "TipoReservaLegal.findByIdeTipoReservaLegal", query = "SELECT t FROM TipoReservaLegal t WHERE t.ideTipoReservaLegal = :ideTipoReservaLegal"),
		@NamedQuery(name = "TipoReservaLegal.findByNomTipoReservaLegal", query = "SELECT t FROM TipoReservaLegal t WHERE t.nomTipoReservaLegal = :nomTipoReservaLegal") })
public class TipoReservaLegal implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_reserva_legal", nullable = false)
	private Integer ideTipoReservaLegal;

	@Basic(optional = false)
	@NotNull
	
	@Column(name = "nom_tipo_reserva_legal")
	private String nomTipoReservaLegal;

	public TipoReservaLegal() {

	}

	public TipoReservaLegal(Integer ideTipoReservaLegal, String nomTipoReservaLegal) {
		this.ideTipoReservaLegal = ideTipoReservaLegal;
		this.nomTipoReservaLegal = nomTipoReservaLegal;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideTipoReservaLegal);
	}

	public Integer getIdeTipoReservaLegal() {
		return ideTipoReservaLegal;
	}

	public void setIdeTipoReservaLegal(Integer ideTipoReservaLegal) {
		this.ideTipoReservaLegal = ideTipoReservaLegal;
	}

	public String getNomTipoReservaLegal() {
		return nomTipoReservaLegal;
	}

	public void setNomTipoReservaLegal(String nomTipoReservaLegal) {
		this.nomTipoReservaLegal = nomTipoReservaLegal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTipoReservaLegal == null) ? 0 : ideTipoReservaLegal.hashCode());
		result = prime * result + ((nomTipoReservaLegal == null) ? 0 : nomTipoReservaLegal.hashCode());
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
		TipoReservaLegal other = (TipoReservaLegal) obj;
		if (ideTipoReservaLegal == null) {
			if (other.ideTipoReservaLegal != null)
				return false;
		} else if (!ideTipoReservaLegal.equals(other.ideTipoReservaLegal))
			return false;
		if (nomTipoReservaLegal == null) {
			if (other.nomTipoReservaLegal != null)
				return false;
		} else if (!nomTipoReservaLegal.equals(other.nomTipoReservaLegal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.TipoReservaLegal [ideTipoReservaLegal=" + ideTipoReservaLegal + "]";
	}

}
