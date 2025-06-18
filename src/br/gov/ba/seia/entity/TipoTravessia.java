package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_travessia", uniqueConstraints = { @UniqueConstraint(columnNames = { "nom_tipo_travessia" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoTravessia.findAll", query = "SELECT t FROM TipoTravessia t order by t.nomTipoTravessia"),
		@NamedQuery(name = "TipoTravessia.findByIdeTipoTravessia", query = "SELECT t FROM TipoTravessia t WHERE t.ideTipoTravessia = :ideTipoTravessia"),
		@NamedQuery(name = "TipoTravessia.findByNomTipoTravessia", query = "SELECT t FROM TipoTravessia t WHERE t.nomTipoTravessia = :nomTipoTravessia") })
public class TipoTravessia implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_travessia", nullable = false)
	private Integer ideTipoTravessia;

	@Basic(optional = true)
	@Column(name = "ide_tipo_travessia_pai")
	private Integer ideTipoTravessiaPai;

	@Basic(optional = false)
	@NotNull
	
	@Column(name = "nom_tipo_travessia")
	private String nomTipoTravessia;

	public TipoTravessia() {
	}

	public TipoTravessia(Integer ideTipoTravessia) {
		this.ideTipoTravessia = ideTipoTravessia;
	}

	public TipoTravessia(Integer ideTipoTravessia, String nomTipoTravessia) {
		this.ideTipoTravessia = ideTipoTravessia;
		this.nomTipoTravessia = nomTipoTravessia;
	}

	public Integer getIdeTipoTravessia() {
		return ideTipoTravessia;
	}

	public void setIdeTipoTravessia(Integer ideTipoTravessia) {
		this.ideTipoTravessia = ideTipoTravessia;
	}

	public String getNomTipoTravessia() {
		return nomTipoTravessia;
	}

	public void setNomTipoTravessia(String nomTipoTravessia) {
		this.nomTipoTravessia = nomTipoTravessia;
	}

	public Integer getIdeTipoTravessiaPai() {
		return ideTipoTravessiaPai;
	}

	public void setIdeTipoTravessiaPai(Integer ideTipoTravessiaPai) {
		this.ideTipoTravessiaPai = ideTipoTravessiaPai;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTipoTravessia == null) ? 0 : ideTipoTravessia.hashCode());
		result = prime * result + ((ideTipoTravessiaPai == null) ? 0 : ideTipoTravessiaPai.hashCode());
		result = prime * result + ((nomTipoTravessia == null) ? 0 : nomTipoTravessia.hashCode());
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
		TipoTravessia other = (TipoTravessia) obj;
		if (ideTipoTravessia == null) {
			if (other.ideTipoTravessia != null)
				return false;
		} else if (!ideTipoTravessia.equals(other.ideTipoTravessia))
			return false;
		if (ideTipoTravessiaPai == null) {
			if (other.ideTipoTravessiaPai != null)
				return false;
		} else if (!ideTipoTravessiaPai.equals(other.ideTipoTravessiaPai))
			return false;
		if (nomTipoTravessia == null) {
			if (other.nomTipoTravessia != null)
				return false;
		} else if (!nomTipoTravessia.equals(other.nomTipoTravessia))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.TipoTravessia[ ideTipoTravessia=" + ideTipoTravessia + " ]";
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoTravessia);
	}

}
