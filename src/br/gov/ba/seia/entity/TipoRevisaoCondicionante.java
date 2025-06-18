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

/**
 * 
 * @author Lucas.Reis
 * 
 */

@Entity
@Table(name = "tipo_revisao_condicionante")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoRevisaoCondicionante.findAll", query = "SELECT t FROM TipoRevisaoCondicionante t"),
		@NamedQuery(name = "TipoRevisaoCondicionante.findByIdeTipoRevisaoCondicionante", query = "SELECT t FROM TipoRevisaoCondicionante t WHERE t.ideTipoRevisaoCondicionante = :ideTipoRevisaoCondicionante"),
		@NamedQuery(name = "TipoRevisaoCondicionante.findByNomTipoRevisaoCondicionante", query = "SELECT t FROM TipoRevisaoCondicionante t WHERE t.nomTipoRevisaoCondicionante = :nomTipoRevisaoCondicionante") })
public class TipoRevisaoCondicionante implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_revisao_condicionante", nullable = false)
	private Integer ideTipoRevisaoCondicionante;

	@Basic(optional = false)
	@NotNull
	
	@Column(name = "nom_tipo_revisao_condicionante", nullable = false, length = 40)
	private String nomTipoRevisaoCondicionante;

	public TipoRevisaoCondicionante() {
	}

	public TipoRevisaoCondicionante(Integer ideTipoRevisaoCondicionante, String nomTipoRevisaoCondicionante) {
		this.ideTipoRevisaoCondicionante = ideTipoRevisaoCondicionante;
		this.nomTipoRevisaoCondicionante = nomTipoRevisaoCondicionante;
	}

	public TipoRevisaoCondicionante(Integer ideTipoRevisaoCondicionante) {
		this.ideTipoRevisaoCondicionante = ideTipoRevisaoCondicionante;
	}

	public Integer getIdeTipoRevisaoCondicionante() {
		return ideTipoRevisaoCondicionante;
	}

	public void setIdeTipoRevisaoCondicionante(Integer ideTipoRevisaoCondicionante) {
		this.ideTipoRevisaoCondicionante = ideTipoRevisaoCondicionante;
	}

	public String getNomTipoRevisaoCondicionante() {
		return nomTipoRevisaoCondicionante;
	}

	public void setNomTipoRevisaoCondicionante(String nomTipoRevisaoCondicionante) {
		this.nomTipoRevisaoCondicionante = nomTipoRevisaoCondicionante;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTipoRevisaoCondicionante == null) ? 0 : ideTipoRevisaoCondicionante.hashCode());
		result = prime * result + ((nomTipoRevisaoCondicionante == null) ? 0 : nomTipoRevisaoCondicionante.hashCode());
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
		TipoRevisaoCondicionante other = (TipoRevisaoCondicionante) obj;
		if (ideTipoRevisaoCondicionante == null) {
			if (other.ideTipoRevisaoCondicionante != null)
				return false;
		} else if (!ideTipoRevisaoCondicionante.equals(other.ideTipoRevisaoCondicionante))
			return false;
		if (nomTipoRevisaoCondicionante == null) {
			if (other.nomTipoRevisaoCondicionante != null)
				return false;
		} else if (!nomTipoRevisaoCondicionante.equals(other.nomTipoRevisaoCondicionante))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.TipoRevisaoCondicionante [ideTipoRevisaoCondicionante="
				+ ideTipoRevisaoCondicionante + "]";
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoRevisaoCondicionante);
	}

}
