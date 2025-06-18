package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: CondicionanteAmbiental.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 11 de set de 2017
 * Objetivo: 	
	
 */

@Entity
@Table(name="condicionante_ambiental")

public class CondicionanteAmbiental  implements Serializable{

	/**
	 * Propriedade: serialVersionUID
	 * @type: long
	 */
	private static final long serialVersionUID = -273209888512662908L;

	/**
	 * Propriedade: ideCondicionante
	 * @type: Integer
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "condicionante_ambiental_ide_condicionante_seq")
	@SequenceGenerator(name = "condicionante_ambiental_ide_condicionante_seq", sequenceName = "condicionante_ambiental_ide_condicionante_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_condicionante", nullable = false)
	private Integer ideCondicionante;
	
	/**
	 * Propriedade: nomConcionante
	 * @type: String
	 */
	@Size(max=100)
	@Column(name = "nom_condicionante", nullable= false)
	private String nomCondicionante;
	
	/**
	 * Propriedade: ideSegmento
	 * @type: Segmento
	 */
	@JoinColumn(name="ide_segmento", referencedColumnName="ide_segmento", nullable=false)
	@ManyToOne
	private Segmento ideSegmento;

	/**
	 * Propriedade: indExcluido
	 * @type: Boolean
	 */
	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido;
	
	/**
	 * Propriedade: condicionanteAtoAmbientalCollection
	 * @type: Collection<CondicionanteAtoAmbiental>
	 */
	@OneToMany(mappedBy = "ideCondicionante", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Collection<CondicionanteAtoAmbiental> condicionanteAtoAmbientalCollection;
	
	
	/**
	 * Propriedade: condicionanteAnaliseTecnicaCollection
	 * @type: Collection<CondicionanteAnaliseTecnica>
	 */
	@OneToMany(mappedBy = "ideCondicionante", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Collection<CondicionanteAnaliseTecnica> condicionanteAnaliseTecnicaCollection;
	
	
	
	@Transient
	private AtoAmbiental atoAmbiental;
	
	/**
	 * Getter do campo ideCondicionante
	 *	
	 * @return the ideCondicionante
	 */
	public Integer getIdeCondicionante() {
		return ideCondicionante;
	}

	/**
	 * Setter do campo  ideCondicionante
	 * @param ideCondicionante the ideCondicionante to set
	 */
	public void setIdeCondicionante(Integer ideCondicionante) {
		this.ideCondicionante = ideCondicionante;
	}

	/**
	 * Getter do campo ideSegmento
	 *	
	 * @return the ideSegmento
	 */
	public Segmento getIdeSegmento() {
		return ideSegmento;
	}

	/**
	 * Setter do campo  ideSegmento
	 * @param ideSegmento the ideSegmento to set
	 */
	public void setIdeSegmento(Segmento ideSegmento) {
		this.ideSegmento = ideSegmento;
	}


	/**
	 * Getter do campo nomCondicionante
	 *	
	 * @return the nomCondicionante
	 */
	public String getNomCondicionante() {
		return nomCondicionante;
	}

	/**
	 * Setter do campo  nomCondicionante
	 * @param nomCondicionante the nomCondicionante to set
	 */
	public void setNomCondicionante(String nomCondicionante) {
		this.nomCondicionante = nomCondicionante;
	}
	
	/**
	 * Getter do campo condicionanteAtoAmbientalCollection
	 *	
	 * @return the condicionanteAtoAmbientalCollection
	 */
	public Collection<CondicionanteAtoAmbiental> getCondicionanteAtoAmbientalCollection() {
		return condicionanteAtoAmbientalCollection;
	}

	/**
	 * Setter do campo  condicionanteAtoAmbientalCollection
	 * @param condicionanteAtoAmbientalCollection the condicionanteAtoAmbientalCollection to set
	 */
	public void setCondicionanteAtoAmbientalCollection(
			Collection<CondicionanteAtoAmbiental> condicionanteAtoAmbientalCollection) {
		this.condicionanteAtoAmbientalCollection = condicionanteAtoAmbientalCollection;
	}

	/**
	 * Getter do campo condicionanteAnaliseTecnicaCollection
	 *	
	 * @return the condicionanteAnaliseTecnicaCollection
	 */
	public Collection<CondicionanteAnaliseTecnica> getCondicionanteAnaliseTecnicaCollection() {
		return condicionanteAnaliseTecnicaCollection;
	}

	/**
	 * Setter do campo  condicionanteAnaliseTecnicaCollection
	 * @param condicionanteAnaliseTecnicaCollection the condicionanteAnaliseTecnicaCollection to set
	 */
	public void setCondicionanteAnaliseTecnicaCollection(
			Collection<CondicionanteAnaliseTecnica> condicionanteAnaliseTecnicaCollection) {
		this.condicionanteAnaliseTecnicaCollection = condicionanteAnaliseTecnicaCollection;
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

	/**
	 * Getter do campo atoAmbiental
	 *	
	 * @return the atoAmbiental
	 */
	public AtoAmbiental getAtoAmbiental() {
		return atoAmbiental;
	}

	/**
	 * Setter do campo  atoAmbiental
	 * @param atoAmbiental the atoAmbiental to set
	 */
	public void setAtoAmbiental(AtoAmbiental atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCondicionante == null) ? 0 : ideCondicionante.hashCode());
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
		CondicionanteAmbiental other = (CondicionanteAmbiental) obj;
		if (ideCondicionante == null) {
			if (other.ideCondicionante != null)
				return false;
		} else if (!ideCondicionante.equals(other.ideCondicionante))
			return false;
		return true;
	}
	
	
	
}
