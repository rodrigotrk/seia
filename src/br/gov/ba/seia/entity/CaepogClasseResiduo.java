package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Tabela que armazena as classificações dos resíduos
 */
@Entity
@Table(name = "caepog_classe_residuo")
public class CaepogClasseResiduo implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_CLASSE_RESIDUO
	 */
	@Id
	@Basic(optional = false)
	@Column(name = "ide_caepog_classe_residuo", nullable = false)
	private Integer ideCaepogClasseResiduo;
	
	/**
	 * Nome da classificação do resíduo
	 */
	@Column(name = "nom_classe_residuo", length = 120)
	private String nomClasseResiduo;
	
	public CaepogClasseResiduo() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogClasseResiduo == null) ? 0 : ideCaepogClasseResiduo.hashCode());
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
		CaepogClasseResiduo other = (CaepogClasseResiduo) obj;
		if (ideCaepogClasseResiduo == null) {
			if (other.ideCaepogClasseResiduo != null)
				return false;
		} else if (!ideCaepogClasseResiduo.equals(other.ideCaepogClasseResiduo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogClasseResiduo [ideCaepogClasseResiduo=" + ideCaepogClasseResiduo + "]";
	}

	@Override
	public Long getId() {
		return ideCaepogClasseResiduo.longValue();
	}
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogClasseResiduo() {
		return ideCaepogClasseResiduo;
	}

	public void setIdeCaepogClasseResiduo(Integer ideCaepogClasseResiduo) {
		this.ideCaepogClasseResiduo = ideCaepogClasseResiduo;
	}

	public String getNomClasseResiduo() {
		return nomClasseResiduo;
	}

	public void setNomClasseResiduo(String nomClasseResiduo) {
		this.nomClasseResiduo = nomClasseResiduo;
	}
}