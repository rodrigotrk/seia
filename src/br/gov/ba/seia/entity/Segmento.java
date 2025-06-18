package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: Segmento.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 6 de set de 2017
 * Objetivo: Entidade de que contém os segmentos de empreendimentos 
 */
@Entity
@Table(name="segmento")
@XmlRootElement
@NamedQueries({ 
	@NamedQuery(name = "Segmento.findAll", query = "SELECT seg FROM Segmento seg WHERE seg.indExcluido = false "),
	@NamedQuery(name="Segmento.findExistente" , query="SELECT seg FROM Segmento seg WHERE indExcluido = false and lower(seg.nomSegmento) = lower(:nomSegmento)")
	})
public class Segmento implements Serializable,BaseEntity {
	
	/**SELECT seg FROM Segmento seg WHERE indExcluido = false 
	 * Propriedade: serialVersionUID
	 * @type: long
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Propriedade: ideSegmento
	 * @type: Integer
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "segmento_ide_segmento_seq")
	@SequenceGenerator(name = "segmento_ide_segmento_seq", sequenceName = "segmento_ide_segmento_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_segmento", nullable = false)
	private Long ideSegmento;

	/**
	 * Propriedade: desSegmento
	 * @type: String
	
	 */
	@Size(max=100)
	@Column(name = "nom_segmento", nullable=false)
	private String nomSegmento;

	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido; 



	/**
	 * Getter do campo ideSegmento
	 *	
	 * @return the ideSegmento
	 */
	public Long getIdeSegmento() {
		return ideSegmento;
	}



	/**
	 * Setter do campo  ideSegmento
	 * @param ideSegmento the ideSegmento to set
	 */
	public void setIdeSegmento(Long ideSegmento) {
		this.ideSegmento = ideSegmento;
	}



	/**
	 * Getter do campo nomSegmento
	 *	
	 * @return the nomSegmento
	 */
	public String getNomSegmento() {
		return nomSegmento;
	}



	/**
	 * Setter do campo  nomSegmento
	 * @param nomSegmento the nomSegmento to set
	 */
	public void setNomSegmento(String nomSegmento) {
		this.nomSegmento = nomSegmento;
	}


	/**
	 * Getter do campo indExcluido
	 *	
	 * @return the indExcluido
	 */
	public Boolean getIndExcluido() {
		return indExcluido;
	}



	/**
	 * Setter do campo  indExcluido
	 * @param indExcluido the indExcluido to set
	 */
	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideSegmento == null) ? 0 : ideSegmento.hashCode());
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
		Segmento other = (Segmento) obj;
		if (ideSegmento == null) {
			if (other.ideSegmento != null)
				return false;
		} else if (!ideSegmento.equals(other.ideSegmento))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.interfaces.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		return ideSegmento;
	}
	
	

}
