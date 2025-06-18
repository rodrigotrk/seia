package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Tabela que armazena os tipos de resíduos
 */
@Entity
@Table(name = "caepog_tipo_residuo")
public class CaepogTipoResiduo implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_TIPO_RESIDUO
	 */
	@Id
	@Basic(optional = false)
	@Column(name = "ide_caepog_tipo_residuo", nullable = false)
	private Integer ideCaepogTipoResiduo;
	
	/**
	 * Nome do tipo de resíduo
	 */
	@Column(name = "nom_caepog_tipo_residuo", length = 100)
	private String nomCaepogTipoResiduo;
	
	public CaepogTipoResiduo() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogTipoResiduo == null) ? 0 : ideCaepogTipoResiduo.hashCode());
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
		CaepogTipoResiduo other = (CaepogTipoResiduo) obj;
		if (ideCaepogTipoResiduo == null) {
			if (other.ideCaepogTipoResiduo != null)
				return false;
		} else if (!ideCaepogTipoResiduo.equals(other.ideCaepogTipoResiduo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogTipoResiduo [ideCaepogTipoResiduo=" + ideCaepogTipoResiduo + "]";
	}
	
	@Override
	public Long getId() {
		return ideCaepogTipoResiduo.longValue();
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogTipoResiduo() {
		return ideCaepogTipoResiduo;
	}

	public void setIdeCaepogTipoResiduo(Integer ideCaepogTipoResiduo) {
		this.ideCaepogTipoResiduo = ideCaepogTipoResiduo;
	}

	public String getNomCaepogTipoResiduo() {
		return nomCaepogTipoResiduo;
	}

	public void setNomCaepogTipoResiduo(String nomCaepogTipoResiduo) {
		this.nomCaepogTipoResiduo = nomCaepogTipoResiduo;
	}
}