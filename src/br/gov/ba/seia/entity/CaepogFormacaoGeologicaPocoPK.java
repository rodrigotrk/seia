package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Chave primária composta da tabela CAEPOG_FORMACAO_GEOLOGICA_POCO
 */
@Embeddable
public class CaepogFormacaoGeologicaPocoPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Chave primária da tabela CAEPOG_FORMACAO_GEOLOGICA
	 */
	@Basic(optional = false)
	@Column(name = "ide_caepog_formacao_geologica", nullable = false)
	private int ideCaepogFormacaoGeologica;

	/**
	 * Chave primária da tabela CAEPOG_POCO
	 */
	@Basic(optional = false)
	@Column(name = "ide_caepog_poco", nullable = false)
	private int ideCaepogPoco;

	public CaepogFormacaoGeologicaPocoPK() {
		super();
	}

	public CaepogFormacaoGeologicaPocoPK(int ideCaepogPoco, int ideCaepogFormacaoGeologica) {
		super();
		this.ideCaepogFormacaoGeologica = ideCaepogFormacaoGeologica;
		this.ideCaepogPoco = ideCaepogPoco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ideCaepogFormacaoGeologica;
		result = prime * result + ideCaepogPoco;
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
		CaepogFormacaoGeologicaPocoPK other = (CaepogFormacaoGeologicaPocoPK) obj;
		if (ideCaepogFormacaoGeologica != other.ideCaepogFormacaoGeologica)
			return false;
		if (ideCaepogPoco != other.ideCaepogPoco)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogFormacaoGeologicaPocoPK [ideCaepogFormacaoGeologica=" + ideCaepogFormacaoGeologica + ", ideCaepogPoco=" + ideCaepogPoco + "]";
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public int getIdeCaepogFormacaoGeologica() {
		return ideCaepogFormacaoGeologica;
	}

	public void setIdeCaepogFormacaoGeologica(int ideCaepogFormacaoGeologica) {
		this.ideCaepogFormacaoGeologica = ideCaepogFormacaoGeologica;
	}

	public int getIdeCaepogPoco() {
		return ideCaepogPoco;
	}

	public void setIdeCaepogPoco(int ideCaepogPoco) {
		this.ideCaepogPoco = ideCaepogPoco;
	}
}