package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Chave primária composta da tabela CAEPOG_FASE_VARIAVEL
 */
@Embeddable
public class CaepogFaseVariavelPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Chave primária da tabela CAEPOG_FASE_PERFURACAO
	 */
	@Basic(optional = false)
	@Column(name = "ide_caepog_fase_perfuracao", nullable = false)
	private int ideCaepogFasePerfuracao;

	/**
	 * Chave primária da tabela CAEPOG_VARIAVEL
	 */
	@Basic(optional = false)
	@Column(name = "ide_caepog_variavel", nullable = false)
	private int ideCaepogVariavel;

	public CaepogFaseVariavelPK() {
		super();
	}

	public CaepogFaseVariavelPK(int ideCaepogFasePerfuracao, int ideCaepogVariavel) {
		super();
		this.ideCaepogFasePerfuracao = ideCaepogFasePerfuracao;
		this.ideCaepogVariavel = ideCaepogVariavel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ideCaepogFasePerfuracao;
		result = prime * result + ideCaepogVariavel;
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
		CaepogFaseVariavelPK other = (CaepogFaseVariavelPK) obj;
		if (ideCaepogFasePerfuracao != other.ideCaepogFasePerfuracao)
			return false;
		if (ideCaepogVariavel != other.ideCaepogVariavel)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogFaseVariavelPK [ideCaepogFasePerfuracao=" + ideCaepogFasePerfuracao + ", ideCaepogVariavel=" + ideCaepogVariavel + "]";
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public int getIdeCaepogFasePerfuracao() {
		return ideCaepogFasePerfuracao;
	}

	public void setIdeCaepogFasePerfuracao(int ideCaepogFasePerfuracao) {
		this.ideCaepogFasePerfuracao = ideCaepogFasePerfuracao;
	}

	public int getIdeCaepogVariavel() {
		return ideCaepogVariavel;
	}

	public void setIdeCaepogVariavel(int ideCaepogVariavel) {
		this.ideCaepogVariavel = ideCaepogVariavel;
	}
}