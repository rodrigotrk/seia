package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.CaepogVariavelEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Tabela que armazena a variáveis que serão medidas
 */
@Entity
@Table(name = "caepog_variavel")
public class CaepogVariavel implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_VARIAVEL
	 */
	@Id
	@Basic(optional = false)
	@Column(name = "ide_caepog_variavel", nullable = false)
	private Integer ideCaepogVariavel;
	
	/**
	 * Nome da unidade de medida (Metro, Metro cúbico, Miligrama por Litro, etc)
	 */
	@Column(name = "nom_caepog_variavel", length = 100)
	private String nomCaepogVariavel;
	
	/**
	 * Número de casas decimais que contem a variável
	 */
	@Column(name = "decimal_caepog_variavel")
	private Integer decimalCaepogVariavel;

	/**
	 * Sistema de medida da unidade (m, m3, mg/l, etc)
	 */
	@Column(name = "sistema_caepog_variavel", length = 8)
	private String sistemaCaepogVariavel;
	
	public CaepogVariavel() {
		super();
	}
	
	public CaepogVariavel(CaepogVariavelEnum c) {
		super();
		this.ideCaepogVariavel = c.getId();
		this.nomCaepogVariavel = c.getNome();
		this.sistemaCaepogVariavel = c.getUnidade();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogVariavel == null) ? 0 : ideCaepogVariavel.hashCode());
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
		CaepogVariavel other = (CaepogVariavel) obj;
		if (ideCaepogVariavel == null) {
			if (other.ideCaepogVariavel != null)
				return false;
		} else if (!ideCaepogVariavel.equals(other.ideCaepogVariavel))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogVariavel [ideCaepogVariavel=" + ideCaepogVariavel + "]";
	}

	@Override
	public Long getId() {
		return ideCaepogVariavel.longValue();
	}
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogVariavel() {
		return ideCaepogVariavel;
	}

	public void setIdeCaepogVariavel(Integer ideCaepogVariavel) {
		this.ideCaepogVariavel = ideCaepogVariavel;
	}

	public String getNomCaepogVariavel() {
		return nomCaepogVariavel;
	}

	public void setNomCaepogVariavel(String nomCaepogVariavel) {
		this.nomCaepogVariavel = nomCaepogVariavel;
	}

	public Integer getDecimalCaepogVariavel() {
		return decimalCaepogVariavel;
	}

	public void setDecimalCaepogVariavel(Integer decimalCaepogVariavel) {
		this.decimalCaepogVariavel = decimalCaepogVariavel;
	}

	public String getSistemaCaepogVariavel() {
		return sistemaCaepogVariavel;
	}

	public void setSistemaCaepogVariavel(String sistemaCaepogVariavel) {
		this.sistemaCaepogVariavel = sistemaCaepogVariavel;
	}
}