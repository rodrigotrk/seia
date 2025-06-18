package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Chave primária composta da tabela CAEPOG_OBJETIVO_ATIVIDADE_POCO
 */
@Embeddable
public class CaepogObjetivoAtividadePocoPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Chave primária da tabela CAEPOG_POCO
	 */
	@Basic(optional = false)
	@Column(name = "ide_caepog_poco", nullable = false)
	private int ideCaepogPoco;

	/**
	 * Chave primária da tabela CAEPOG_OBJETIVO_ATIVIDADE
	 */
	@Basic(optional = false)
	@Column(name = "ide_caepog_objetivo_atividade", nullable = false)
	private int ideCaepogObjetivoAtividade;

	public CaepogObjetivoAtividadePocoPK() {
		super();
	}

	public CaepogObjetivoAtividadePocoPK(int ideCaepogPoco, int ideCaepogObjetivoAtividade) {
		super();
		this.ideCaepogPoco = ideCaepogPoco;
		this.ideCaepogObjetivoAtividade = ideCaepogObjetivoAtividade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ideCaepogObjetivoAtividade;
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
		CaepogObjetivoAtividadePocoPK other = (CaepogObjetivoAtividadePocoPK) obj;
		if (ideCaepogObjetivoAtividade != other.ideCaepogObjetivoAtividade)
			return false;
		if (ideCaepogPoco != other.ideCaepogPoco)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogObjetivoAtividadePocoPK [ideCaepogPoco=" + ideCaepogPoco + ", ideCaepogObjetivoAtividade=" + ideCaepogObjetivoAtividade + "]";
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public int getIdeCaepogPoco() {
		return ideCaepogPoco;
	}

	public void setIdeCaepogPoco(int ideCaepogPoco) {
		this.ideCaepogPoco = ideCaepogPoco;
	}

	public int getIdeCaepogObjetivoAtividade() {
		return ideCaepogObjetivoAtividade;
	}

	public void setIdeCaepogObjetivoAtividade(int ideCaepogObjetivoAtividade) {
		this.ideCaepogObjetivoAtividade = ideCaepogObjetivoAtividade;
	}
}