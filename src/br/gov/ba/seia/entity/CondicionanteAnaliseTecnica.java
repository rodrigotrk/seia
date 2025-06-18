/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 11 de set de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: CondicionanteAnaliseTecnica.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 11 de set de 2017
 * Objetivo: 	
	
 */
@Entity
@Table(name="condicionante_analise_tecnica")
public class CondicionanteAnaliseTecnica implements Serializable{

	/**
	 * Propriedade: serialVersionUID
	 * @type: long
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Propriedade: ideCondicionanteAnaliseTecnica
	 * @type: Integer
	 */
	@EmbeddedId
	private CondicionanteAnaliseTecnicaPK condicionanteAnaliseTecnicaPK;
	/**
	 * Propriedade: ideAnaliseTecnica
	 * @type: Integer
	 */
	@JoinColumn(name = "ide_analise_tecnica", referencedColumnName = "ide_analise_tecnica", nullable = false,updatable=false, insertable=false)
	@ManyToOne
	private AnaliseTecnica ideAnaliseTecnica;
	
	
	/**
	 * Propriedade: ideCondicionante
	 * @type: CondicionanteAmbiental
	 */
	@JoinColumn(name = "ide_condicionante", referencedColumnName = "ide_condicionante", nullable = false,updatable=false, insertable=false)
	@ManyToOne
	private CondicionanteAmbiental ideCondicionante;

	
	


	/**
	 * Getter do campo ideAnaliseTecnica
	 *	
	 * @return the ideAnaliseTecnica
	 */
	public AnaliseTecnica getIdeAnaliseTecnica() {
		return ideAnaliseTecnica;
	}


	/**
	 * Setter do campo  ideAnaliseTecnica
	 * @param ideAnaliseTecnica the ideAnaliseTecnica to set
	 */
	public void setIdeAnaliseTecnica(AnaliseTecnica ideAnaliseTecnica) {
		this.ideAnaliseTecnica = ideAnaliseTecnica;
	}


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
	 * Getter do campo condicionanteAnaliseTecnicaPK
	 *	
	 * @return the condicionanteAnaliseTecnicaPK
	 */
	public CondicionanteAnaliseTecnicaPK getCondicionanteAnaliseTecnicaPK() {
		return condicionanteAnaliseTecnicaPK;
	}


	/**
	 * Setter do campo  condicionanteAnaliseTecnicaPK
	 * @param condicionanteAnaliseTecnicaPK the condicionanteAnaliseTecnicaPK to set
	 */
	public void setCondicionanteAnaliseTecnicaPK(CondicionanteAnaliseTecnicaPK condicionanteAnaliseTecnicaPK) {
		this.condicionanteAnaliseTecnicaPK = condicionanteAnaliseTecnicaPK;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((condicionanteAnaliseTecnicaPK == null) ? 0 : condicionanteAnaliseTecnicaPK.hashCode());
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
		CondicionanteAnaliseTecnica other = (CondicionanteAnaliseTecnica) obj;
		if (condicionanteAnaliseTecnicaPK == null) {
			if (other.condicionanteAnaliseTecnicaPK != null)
				return false;
		} else if (!condicionanteAnaliseTecnicaPK.equals(other.condicionanteAnaliseTecnicaPK))
			return false;
		return true;
	}



	
	
	
	
}
