package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Tabela que armazena o objetivo da atividade
 */
@Entity
@Table(name = "caepog_objetivo_atividade")
public class CaepogObjetivoAtividade implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_OBJETIVO_ATIVIDADE
	 */
	@Id
	@Basic(optional = false)
	@Column(name = "ide_caepog_objetivo_atividade", nullable = false)
	private Integer ideCaepogObjetivoAtividade;
	
	/**
	 * Nome do objetivo da atividade
	 */
	@Column(name = "nom_caepog_objetivo_atividade", length = 120)
	private String nomCaepogObjetivoAtividade;
	
	public CaepogObjetivoAtividade() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogObjetivoAtividade == null) ? 0 : ideCaepogObjetivoAtividade.hashCode());
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
		CaepogObjetivoAtividade other = (CaepogObjetivoAtividade) obj;
		if (ideCaepogObjetivoAtividade == null) {
			if (other.ideCaepogObjetivoAtividade != null)
				return false;
		} else if (!ideCaepogObjetivoAtividade.equals(other.ideCaepogObjetivoAtividade))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogObjetivoAtividade [ideCaepogObjetivoAtividade=" + ideCaepogObjetivoAtividade + "]";
	}

	@Override
	public Long getId() {
		return ideCaepogObjetivoAtividade.longValue();
	}
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogObjetivoAtividade() {
		return ideCaepogObjetivoAtividade;
	}

	public void setIdeCaepogObjetivoAtividade(Integer ideCaepogObjetivoAtividade) {
		this.ideCaepogObjetivoAtividade = ideCaepogObjetivoAtividade;
	}

	public String getNomCaepogObjetivoAtividade() {
		return nomCaepogObjetivoAtividade;
	}

	public void setNomCaepogObjetivoAtividade(String nomCaepogObjetivoAtividade) {
		this.nomCaepogObjetivoAtividade = nomCaepogObjetivoAtividade;
	}
}