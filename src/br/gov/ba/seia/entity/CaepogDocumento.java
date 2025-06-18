package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Tabela que armazena os documentos que serão obrigatórios para o cadastro
 */
@Entity
@Table(name = "caepog_documento")
public class CaepogDocumento implements Serializable, BaseEntity, Cloneable {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_DOCUMENTO
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caepog_documento_ide_caepog_documento_seq")
	@SequenceGenerator(name = "caepog_documento_ide_caepog_documento_seq", sequenceName = "caepog_documento_ide_caepog_documento_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_caepog_documento", nullable = false)
	private Integer ideCaepogDocumento;
	
	/**
	 * Nome do documento
	 */
	@Column(name = "nom_caepog_documento", length = 120)
	private String nomCaepogDocumento;
	
	/**
	 * Chave primária da tabela CAEPOG_CATEGORIA_DOCUMENTO
	 */
	@JoinColumn(name = "ide_caepog_categoria_documento", referencedColumnName = "ide_caepog_categoria_documento")
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogCategoriaDocumento ideCaepogCategoriaDocumento;
	
	public CaepogDocumento() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogDocumento == null) ? 0 : ideCaepogDocumento.hashCode());
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
		CaepogDocumento other = (CaepogDocumento) obj;
		if (ideCaepogDocumento == null) {
			if (other.ideCaepogDocumento != null)
				return false;
		} else if (!ideCaepogDocumento.equals(other.ideCaepogDocumento))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogDocumento [ideCaepogDocumento=" + ideCaepogDocumento + "]";
	}

	@Override
	public Long getId() {
		return ideCaepogDocumento.longValue();
	}

	public CaepogDocumento clone() throws CloneNotSupportedException {
		return (CaepogDocumento) super.clone();
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogDocumento() {
		return ideCaepogDocumento;
	}

	public void setIdeCaepogDocumento(Integer ideCaepogDocumento) {
		this.ideCaepogDocumento = ideCaepogDocumento;
	}

	public String getNomCaepogDocumento() {
		return nomCaepogDocumento;
	}

	public void setNomCaepogDocumento(String nomCaepogDocumento) {
		this.nomCaepogDocumento = nomCaepogDocumento;
	}

	public CaepogCategoriaDocumento getIdeCaepogCategoriaDocumento() {
		return ideCaepogCategoriaDocumento;
	}

	public void setIdeCaepogCategoriaDocumento(CaepogCategoriaDocumento ideCaepogCategoriaDocumento) {
		this.ideCaepogCategoriaDocumento = ideCaepogCategoriaDocumento;
	}
}