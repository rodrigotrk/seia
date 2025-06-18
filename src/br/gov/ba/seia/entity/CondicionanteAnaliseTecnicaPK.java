/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 14 de set de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: CondicionanteAnaliseTecnicaPK.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 14 de set de 2017
 * Objetivo: 	
	
 */
public class CondicionanteAnaliseTecnicaPK  implements Serializable{

	
	/**
	 * Propriedade: serialVersionUID
	 * @type: long
	 */
	private static final long serialVersionUID = -3434182224322016679L;

	/**
	 * Propriedade: ideAnaliseTecnica
	 * @type: Integer
	 */
	@Column(name="ide_analise_tecnica")
	private Integer ideAnaliseTecnica;
	
	/**
	 * Propriedade: ideCondicionante
	 * @type: Integer
	 */
	@Column(name="ide_condicionante")
	private Integer ideCondicionante;
	
	
	public CondicionanteAnaliseTecnicaPK(Integer ideAnaliseTecnica, Integer ideCondicionante){
		this.ideAnaliseTecnica =  ideAnaliseTecnica;
		this.ideCondicionante =  ideCondicionante;
	}

	/**
	 * Getter do campo ideAnaliseTecnica
	 *	
	 * @return the ideAnaliseTecnica
	 */
	public Integer getIdeAnaliseTecnica() {
		return ideAnaliseTecnica;
	}

	/**
	 * Setter do campo  ideAnaliseTecnica
	 * @param ideAnaliseTecnica the ideAnaliseTecnica to set
	 */
	public void setIdeAnaliseTecnica(Integer ideAnaliseTecnica) {
		this.ideAnaliseTecnica = ideAnaliseTecnica;
	}

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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideAnaliseTecnica == null) ? 0 : ideAnaliseTecnica.hashCode());
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
		CondicionanteAnaliseTecnicaPK other = (CondicionanteAnaliseTecnicaPK) obj;
		if (ideAnaliseTecnica == null) {
			if (other.ideAnaliseTecnica != null)
				return false;
		} else if (!ideAnaliseTecnica.equals(other.ideAnaliseTecnica))
			return false;
		if (ideCondicionante == null) {
			if (other.ideCondicionante != null)
				return false;
		} else if (!ideCondicionante.equals(other.ideCondicionante))
			return false;
		return true;
	}
	
	
	
	
}
