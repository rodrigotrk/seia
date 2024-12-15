package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: CondicionanteAtoAmbiental.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 11 de set de 2017
 * Objetivo: 	
	
 */
@Entity
@Table(name="condicionante_ato_ambiental")
@XmlRootElement
@NamedQueries({ 
	@NamedQuery(name = "CondicionanteAto.findAll", query = "SELECT c FROM CondicionanteAtoAmbiental c"),
	@NamedQuery(name = "CondicionanteAto.findByIdeAtoAmbiental", query = "SELECT c FROM CondicionanteAtoAmbiental c WHERE c.ideAtoAmbiental.ideAtoAmbiental = :ideAtoAmbiental"),
	@NamedQuery(name = "CondicionanteAto.findAllIdeCondicionante", query = "SELECT c FROM CondicionanteAtoAmbiental c WHERE c.ideCondicionante.ideCondicionante = :ideCondicionante"),
	@NamedQuery(name = "CondicionanteAto.remove", query = "DELETE FROM CondicionanteAtoAmbiental c WHERE c.condicionanteAtoAmbientalPK.ideAtoAmbiental = :ideAtoAmbiental  and c.condicionanteAtoAmbientalPK.ideCondicionante = :ideCondicionante"),
	})
public class CondicionanteAtoAmbiental implements Serializable{

	
	/**
	 * Propriedade: serialVersionUID
	 * @type: long
	 */
	private static final long serialVersionUID = 8166295377786585157L;
	
	/**
	 * Propriedade: ideCondicionanteAtoAmbiental
	 * @type: Integer
	 */
	@EmbeddedId
	private CondicionanteAtoAmbientalPK condicionanteAtoAmbientalPK;
	/**
	 * Propriedade: ideAtoAmbiental
	 * @type: AtoAmbiental
	 */
	@JoinColumn(name="ide_ato_ambiental", referencedColumnName="ide_ato_ambiental", nullable=false,updatable=false, insertable=false)
	@ManyToOne
	private AtoAmbiental ideAtoAmbiental;
	
	/**
	 * Propriedade: condicionanteAmbiental
	 * @type: CondicionanteAmbiental
	 */
	@JoinColumn(name="ide_condicionante", referencedColumnName="ide_condicionante", nullable=false,updatable=false, insertable=false)
	@ManyToOne
	private CondicionanteAmbiental ideCondicionante;


	/**
	 * Getter do campo ideCondicionante
	 *	
	 * @return the ideCondicionante
	 */
	public CondicionanteAmbiental getIdeCondicionante() {
		return ideCondicionante;
	}

	/**
	 * Setter do campo  ideCondicionante
	 * @param ideCondicionante the ideCondicionante to set
	 */
	public void setIdeCondicionante(CondicionanteAmbiental ideCondicionante) {
		this.ideCondicionante = ideCondicionante;
	}

	/**
	 * Getter do campo ideAtoAmbiental
	 *	
	 * @return the ideAtoAmbiental
	 */
	public AtoAmbiental getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	/**
	 * Setter do campo  ideAtoAmbiental
	 * @param ideAtoAmbiental the ideAtoAmbiental to set
	 */
	public void setIdeAtoAmbiental(AtoAmbiental ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	/**
	 * Getter do campo condicionanteAtoAmbientalPK
	 *	
	 * @return the condicionanteAtoAmbientalPK
	 */
	public CondicionanteAtoAmbientalPK getCondicionanteAtoAmbientalPK() {
		return condicionanteAtoAmbientalPK;
	}

	/**
	 * Setter do campo  condicionanteAtoAmbientalPK
	 * @param condicionanteAtoAmbientalPK the condicionanteAtoAmbientalPK to set
	 */
	public void setCondicionanteAtoAmbientalPK(CondicionanteAtoAmbientalPK condicionanteAtoAmbientalPK) {
		this.condicionanteAtoAmbientalPK = condicionanteAtoAmbientalPK;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((condicionanteAtoAmbientalPK == null) ? 0 : condicionanteAtoAmbientalPK.hashCode());
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
		CondicionanteAtoAmbiental other = (CondicionanteAtoAmbiental) obj;
		if (condicionanteAtoAmbientalPK == null) {
			if (other.condicionanteAtoAmbientalPK != null)
				return false;
		} else if (!condicionanteAtoAmbientalPK.equals(other.condicionanteAtoAmbientalPK))
			return false;
		return true;
	}


	
	
	
}
