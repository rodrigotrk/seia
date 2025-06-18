package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia Classe:
 * TipoNaturezaTorre.java Projeto: seia Pacote: br.gov.ba.seia.entity
 * 
 * @autor: diegoraian em 19 de set de 2017 Objetivo: Entidade que define o
 *         natureza de uma determinada torre
 */
@Entity
@Table(name = "tipo_natureza_torre")
@NamedQuery(name="TipoNaturezaTorre.findAll", query="SELECT t FROM TipoNaturezaTorre t")
public class TipoNaturezaTorre implements Serializable, BaseEntity {

	/**
	 * Propriedade: serialVersionUID
	 * 
	 * @type: long
	 */
	private static final long serialVersionUID = 8362725174247757837L;

	/**
	 * Propriedade: ideTipoNaturezaTorre
	 * 
	 * @type: Long
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_natureza_torre_ide_tipo_natureza_torre_seq")
	@SequenceGenerator(name = "tipo_natureza_torre_ide_tipo_natureza_torre_seq", sequenceName = "tipo_natureza_torre_ide_tipo_natureza_torre_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_tipo_natureza_torre", nullable = false)
	private Long ideTipoNaturezaTorre;

	/**
	 * Propriedade: nomNatureza
	 * 
	 * @type: String
	 */
	@Column(name = "nom_tipo_natureza_torre")
	private String nomTipoNaturezaTorre;

	/**
	 * Getter do campo ideTipoNaturezaTorre
	 * 
	 * @return the ideTipoNaturezaTorre
	 */
	public Long getIdeTipoNaturezaTorre() {
		return ideTipoNaturezaTorre;
	}

	/**
	 * Setter do campo ideTipoNaturezaTorre
	 * 
	 * @param ideTipoNaturezaTorre
	 *            the ideTipoNaturezaTorre to set
	 */
	public void setIdeTipoNaturezaTorre(Long ideTipoNaturezaTorre) {
		this.ideTipoNaturezaTorre = ideTipoNaturezaTorre;
	}

	/**
	 * Getter do campo nomNatureza
	 * 
	 * @return the nomNatureza
	 */
	public String getNomTipoNaturezaTorre() {
		return nomTipoNaturezaTorre;
	}

	/**
	 * Setter do campo nomNatureza
	 * 
	 * @param nomNatureza
	 *            the nomNatureza to set
	 */
	public void setNomTipoNaturezaTorre(String nomTipoNaturezaTorre) {
		this.nomTipoNaturezaTorre = nomTipoNaturezaTorre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoNaturezaTorre other = (TipoNaturezaTorre) obj;
		return this.getId().equals(other.getId());
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.interfaces.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		return ideTipoNaturezaTorre;
	}

}
