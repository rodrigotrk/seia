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
 * PRODEB - Companhia de processamento de dados do Estado da Bahia 
 * Classe: TipoUnidadeConservacaoTorre.java 
 * Projeto: seia Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 19 de set de 2017 
 * Objetivo: Entidade que define a unidade de conservação em que uma determinada torre
 * será instalada
 */
@Entity
@Table(name = "tipo_unidade_conservacao_torre")
@NamedQuery(name="TipoUnidadeConservacaoTorre.findAll", query="SELECT t FROM TipoUnidadeConservacaoTorre t")
public class TipoUnidadeConservacaoTorre implements Serializable, BaseEntity {

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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_unidade_conservacao_torre_ide_tipo_unidade_conservacao_torre_seq")
	@SequenceGenerator(name = "tipo_unidade_conservacao_torre_ide_tipo_unidade_conservacao_torre_seq", sequenceName = "tipo_unidade_conservacao_torre_ide_tipo_unidade_conservacao_torre_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_tipo_unidade_conservacao_torre", nullable = false)
	private Long ideTipoUnidadeConservacaoTorre;

	/**
	 * Propriedade: nomNatureza
	 * 
	 * @type: String
	 */
	@Column(name = "nom_tipo_unidade_conservacao_torre")
	private String nomTipoUnidadeConservacaoTorre;

	/**
	 * Getter do campo ideTipoUnidadeConservacaoTorre
	 *	
	 * @return the ideTipoUnidadeConservacaoTorre
	 */
	public Long getIdeTipoUnidadeConservacaoTorre() {
		return ideTipoUnidadeConservacaoTorre;
	}

	/**
	 * Setter do campo  ideTipoUnidadeConservacaoTorre
	 * @param ideTipoUnidadeConservacaoTorre the ideTipoUnidadeConservacaoTorre to set
	 */
	public void setIdeTipoUnidadeConservacaoTorre(Long ideTipoUnidadeConservacaoTorre) {
		this.ideTipoUnidadeConservacaoTorre = ideTipoUnidadeConservacaoTorre;
	}

	/**
	 * Getter do campo nomTipoUnidadeConservacaoTorre
	 *	
	 * @return the nomTipoUnidadeConservacaoTorre
	 */
	public String getNomTipoUnidadeConservacaoTorre() {
		return nomTipoUnidadeConservacaoTorre;
	}

	/**
	 * Setter do campo  nomTipoUnidadeConservacaoTorre
	 * @param nomTipoUnidadeConservacaoTorre the nomTipoUnidadeConservacaoTorre to set
	 */
	public void setNomTipoUnidadeConservacaoTorre(String nomTipoUnidadeConservacaoTorre) {
		this.nomTipoUnidadeConservacaoTorre = nomTipoUnidadeConservacaoTorre;
	}
	
	
	/* (non-Javadoc)
	 * @see br.gov.ba.seia.interfaces.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		
		return ideTipoUnidadeConservacaoTorre;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoUnidadeConservacaoTorre == null) ? 0 : ideTipoUnidadeConservacaoTorre.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		TipoUnidadeConservacaoTorre other = (TipoUnidadeConservacaoTorre) obj;
		if (ideTipoUnidadeConservacaoTorre == null) {
			if (other.ideTipoUnidadeConservacaoTorre != null)
				return false;
		}
		
		return ideTipoUnidadeConservacaoTorre.equals(other.ideTipoUnidadeConservacaoTorre);
	}

	
}
