package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Tabela que armazena os objetivos do poço
 */
@Entity
@Table(name = "caepog_objetivo_atividade_poco")
public class CaepogObjetivoAtividadePoco implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária composta da tabela CAEPOG_OBJETIVO_ATIVIDADE_POCO
	 */
	@EmbeddedId
	@Basic(optional = false)
	private CaepogObjetivoAtividadePocoPK ideCaepogObjetivoAtividadePocoPK;
	
	/**
	 * Chave primária da tabela CAEPOG_POCO
	 */
	@Basic(optional = false)
	@JoinColumn(name = "ide_caepog_poco", referencedColumnName = "ide_caepog_poco", nullable = true, unique = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogPoco ideCaepogPoco;
	
	/**
	 * Chave primária da tabela CAEPOG_OBJETIVO_ATIVIDADE
	 */
	@Basic(optional = false)
	@JoinColumn(name = "ide_caepog_objetivo_atividade", referencedColumnName = "ide_caepog_objetivo_atividade", nullable = true, unique = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogObjetivoAtividade ideCaepogObjetivoAtividade;
	
	public CaepogObjetivoAtividadePoco() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogObjetivoAtividadePocoPK == null) ? 0 : ideCaepogObjetivoAtividadePocoPK.hashCode());
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
		CaepogObjetivoAtividadePoco other = (CaepogObjetivoAtividadePoco) obj;
		if (ideCaepogObjetivoAtividadePocoPK == null) {
			if (other.ideCaepogObjetivoAtividadePocoPK != null)
				return false;
		} else if (!ideCaepogObjetivoAtividadePocoPK.equals(other.ideCaepogObjetivoAtividadePocoPK))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogObjetivoAtividadePoco [ideCaepogObjetivoAtividadePocoPK=" + ideCaepogObjetivoAtividadePocoPK + "]";
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public CaepogObjetivoAtividadePocoPK getIdeCaepogObjetivoAtividadePocoPK() {
		return ideCaepogObjetivoAtividadePocoPK;
	}

	public void setIdeCaepogObjetivoAtividadePocoPK(CaepogObjetivoAtividadePocoPK ideCaepogObjetivoAtividadePocoPK) {
		this.ideCaepogObjetivoAtividadePocoPK = ideCaepogObjetivoAtividadePocoPK;
	}

	public CaepogPoco getIdeCaepogPoco() {
		return ideCaepogPoco;
	}

	public void setIdeCaepogPoco(CaepogPoco ideCaepogPoco) {
		this.ideCaepogPoco = ideCaepogPoco;
	}

	public CaepogObjetivoAtividade getIdeCaepogObjetivoAtividade() {
		return ideCaepogObjetivoAtividade;
	}

	public void setIdeCaepogObjetivoAtividade(CaepogObjetivoAtividade ideCaepogObjetivoAtividade) {
		this.ideCaepogObjetivoAtividade = ideCaepogObjetivoAtividade;
	}
}