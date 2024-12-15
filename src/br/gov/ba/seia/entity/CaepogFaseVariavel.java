package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Tabela que armazena a associação da variável com a fase juntamente com os valores medidos
 */
@Entity
@Table(name = "caepog_fase_variavel")
public class CaepogFaseVariavel implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária composta da tabela CAEPOG_FASE_VARIAVEL
	 */
	@EmbeddedId
	@Basic(optional = false)
	private CaepogFaseVariavelPK ideCaepogFaseVariavelPK;
	
	/**
	 * Valor medido da variável na fase de perfuração
	 */

	@Column(name = "num_valor_fase_variavel",  precision = 10, scale = 2)
	private BigDecimal numValorFaseVariavel;
	
	/**
	 * Chave estrangeira da tabela CAEPOG_FASE_PERFURACAO
	 */
	@JoinColumn(name = "ide_caepog_fase_perfuracao", referencedColumnName = "ide_caepog_fase_perfuracao", nullable = false, unique = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogFasePerfuracao ideCaepogFasePerfuracao;
	
	/**
	 * Chave estrangeira da tabela CAEPOG_VARIAVEL
	 */
	@JoinColumn(name = "ide_caepog_variavel", referencedColumnName = "ide_caepog_variavel", nullable = false, unique = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogVariavel ideCaepogVariavel;

	public CaepogFaseVariavel() {
		super();
	}
	
	public CaepogFaseVariavel(CaepogFasePerfuracao cfp, CaepogVariavel cv) {
		super();
		this.ideCaepogFasePerfuracao = cfp;
		this.ideCaepogVariavel = cv;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogFaseVariavelPK == null) ? 0 : ideCaepogFaseVariavelPK.hashCode());
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
		CaepogFaseVariavel other = (CaepogFaseVariavel) obj;
		if (ideCaepogFaseVariavelPK == null) {
			if (other.ideCaepogFaseVariavelPK != null)
				return false;
		} else if (!ideCaepogFaseVariavelPK.equals(other.ideCaepogFaseVariavelPK))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogFaseVariavel [ideCaepogFaseVariavelPK=" + ideCaepogFaseVariavelPK + "]";
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public CaepogFaseVariavelPK getIdeCaepogFaseVariavelPK() {
		return ideCaepogFaseVariavelPK;
	}

	public void setIdeCaepogFaseVariavelPK(CaepogFaseVariavelPK ideCaepogFaseVariavelPK) {
		this.ideCaepogFaseVariavelPK = ideCaepogFaseVariavelPK;
	}

	public BigDecimal getNumValorFaseVariavel() {
		return numValorFaseVariavel;
	}

	public void setNumValorFaseVariavel(BigDecimal numValorFaseVariavel) {
		this.numValorFaseVariavel = numValorFaseVariavel;
	}

	public CaepogFasePerfuracao getIdeCaepogFasePerfuracao() {
		return ideCaepogFasePerfuracao;
	}

	public void setIdeCaepogFasePerfuracao(CaepogFasePerfuracao ideCaepogFasePerfuracao) {
		this.ideCaepogFasePerfuracao = ideCaepogFasePerfuracao;
	}

	public CaepogVariavel getIdeCaepogVariavel() {
		return ideCaepogVariavel;
	}

	public void setIdeCaepogVariavel(CaepogVariavel ideCaepogVariavel) {
		this.ideCaepogVariavel = ideCaepogVariavel;
	}
}