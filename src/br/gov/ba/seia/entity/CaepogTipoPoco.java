package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Tabela que armazena os tipos de poços
 */
@Entity
@Table(name = "caepog_tipo_poco")
public class CaepogTipoPoco implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_TIPO_POCO
	 */
	@Id
	@Basic(optional = false)
	@Column(name = "ide_caepog_tipo_poco", nullable = false)
	private Integer ideCaepogTipoPoco;
	
	/**
	 * Nome do tipo do poço
	 */
	@Column(name = "nom_caepog_tipo_poco", length = 100)
	private String nomCaepogTipoPoco;
	
	public CaepogTipoPoco() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogTipoPoco == null) ? 0 : ideCaepogTipoPoco.hashCode());
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
		CaepogTipoPoco other = (CaepogTipoPoco) obj;
		if (ideCaepogTipoPoco == null) {
			if (other.ideCaepogTipoPoco != null)
				return false;
		} else if (!ideCaepogTipoPoco.equals(other.ideCaepogTipoPoco))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogTipoPoco [ideCaepoTipoPoco=" + ideCaepogTipoPoco + "]";
	}

	@Override
	public Long getId() {
		return ideCaepogTipoPoco.longValue();
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogTipoPoco() {
		return ideCaepogTipoPoco;
	}

	public void setIdeCaepogTipoPoco(Integer ideCaepogTipoPoco) {
		this.ideCaepogTipoPoco = ideCaepogTipoPoco;
	}

	public String getNomCaepogTipoPoco() {
		return nomCaepogTipoPoco;
	}

	public void setNomCaepogTipoPoco(String nomCaepogTipoPoco) {
		this.nomCaepogTipoPoco = nomCaepogTipoPoco;
	}
}