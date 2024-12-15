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
 * Tabela que armazena as formações geológicas impactadas pela perfuração do poço
 */
@Entity
@Table(name = "caepog_formacao_geologica_poco")
public class CaepogFormacaoGeologicaPoco implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária composta da tabela CAEPOG_FORMACAO_GEOLOGICA_POCO
	 */
	@EmbeddedId
	@Basic(optional = false)
	private CaepogFormacaoGeologicaPocoPK ideCaepogFormacaoGeologicaPocoPK;

	/**
	 * Chave primária composta da tabela CAEPOG_FORMACAO_GEOLOGICA
	 */
	@Basic(optional = false)
	@JoinColumn(name = "ide_caepog_formacao_geologica", referencedColumnName = "ide_caepog_formacao_geologica", nullable = false, unique = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogFormacaoGeologica ideCaepogFormacaoGeologica;

	/**
	 * Chave primária composta da tabela CAEPOG_POCO
	 */
	@Basic(optional = false)
	@JoinColumn(name = "ide_caepog_poco", referencedColumnName = "ide_caepog_poco", nullable = false, unique = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogPoco ideCaepogPoco;
	
	public CaepogFormacaoGeologicaPoco() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogFormacaoGeologicaPocoPK == null) ? 0 : ideCaepogFormacaoGeologicaPocoPK.hashCode());
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
		CaepogFormacaoGeologicaPoco other = (CaepogFormacaoGeologicaPoco) obj;
		if (ideCaepogFormacaoGeologicaPocoPK == null) {
			if (other.ideCaepogFormacaoGeologicaPocoPK != null)
				return false;
		} else if (!ideCaepogFormacaoGeologicaPocoPK.equals(other.ideCaepogFormacaoGeologicaPocoPK))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogFormacaoGeologicaPoco [ideCaepogFormacaoGeologicaPocoPK=" + ideCaepogFormacaoGeologicaPocoPK + "]";
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public CaepogFormacaoGeologicaPocoPK getIdeCaepogFormacaoGeologicaPocoPK() {
		return ideCaepogFormacaoGeologicaPocoPK;
	}

	public void setIdeCaepogFormacaoGeologicaPocoPK(CaepogFormacaoGeologicaPocoPK ideCaepogFormacaoGeologicaPocoPK) {
		this.ideCaepogFormacaoGeologicaPocoPK = ideCaepogFormacaoGeologicaPocoPK;
	}

	public CaepogFormacaoGeologica getIdeCaepogFormacaoGeologica() {
		return ideCaepogFormacaoGeologica;
	}

	public void setIdeCaepogFormacaoGeologica(CaepogFormacaoGeologica ideCaepogFormacaoGeologica) {
		this.ideCaepogFormacaoGeologica = ideCaepogFormacaoGeologica;
	}
	
	public CaepogPoco getIdeCaepogPoco() {
		return ideCaepogPoco;
	}
	
	public void setIdeCaepogPoco(CaepogPoco ideCaepogPoco) {
		this.ideCaepogPoco = ideCaepogPoco;
	}
}