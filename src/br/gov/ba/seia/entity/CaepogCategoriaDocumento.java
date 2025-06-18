package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Tabela que armazena a categoria do documento
 */
@Entity
@Table(name = "caepog_categoria_documento")
public class CaepogCategoriaDocumento implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_CATEGORIA_DOCUMENTO
	 */
	@Id
	@Basic(optional = false)
	@Column(name = "ide_caepog_categoria_documento", nullable = false)
	private Integer ideCaepogCategoriaDocumento;
	
	/**
	 * Nome da categoria do documento
	 */
	@Column(name = "nom_caepog_categoria_documento", length = 120)
	private String nomCaepogCategoriaDocumento;
	
	public CaepogCategoriaDocumento() {
		super();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogCategoriaDocumento == null) ? 0 : ideCaepogCategoriaDocumento.hashCode());
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
		CaepogCategoriaDocumento other = (CaepogCategoriaDocumento) obj;
		if (ideCaepogCategoriaDocumento == null) {
			if (other.ideCaepogCategoriaDocumento != null)
				return false;
		} else if (!ideCaepogCategoriaDocumento.equals(other.ideCaepogCategoriaDocumento))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "CaepogCategoriaDocumento [ideCaepogCategoriaDocumento=" + ideCaepogCategoriaDocumento + "]";
	}

	@Override
	public Long getId() {
		return ideCaepogCategoriaDocumento.longValue();
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */
	
	public Integer getIdeCaepogCategoriaDocumento() {
		return ideCaepogCategoriaDocumento;
	}

	public void setIdeCaepogCategoriaDocumento(Integer ideCaepogCategoriaDocumento) {
		this.ideCaepogCategoriaDocumento = ideCaepogCategoriaDocumento;
	}

	public String getNomCaepogCategoriaDocumento() {
		return nomCaepogCategoriaDocumento;
	}

	public void setNomCaepogCategoriaDocumento(String nomCaepogCategoriaDocumento) {
		this.nomCaepogCategoriaDocumento = nomCaepogCategoriaDocumento;
	}
}