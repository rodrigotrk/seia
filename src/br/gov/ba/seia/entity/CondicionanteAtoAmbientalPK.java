/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 12 de set de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: CondicionanteAtoAmbientalPK.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.entity
 * @autor: diegoraian em 12 de set de 2017
 * Objetivo: 	
	
 */
public class CondicionanteAtoAmbientalPK implements Serializable {
	
	@Column(name="ide_ato_ambiental")
	private Integer ideAtoAmbiental;
	
	@Column(name="ide_condicionante")
	private Integer ideCondicionante;

	
	/**
	 * 
	 */
	public CondicionanteAtoAmbientalPK() {
		// Auto-generated constructor stub
	}
	
	
	
	/**
	 * @param ideAtoAmbiental
	 * @param ideCondicionante
	 */
	public CondicionanteAtoAmbientalPK(Integer ideAtoAmbiental, Integer ideCondicionante) {
		super();
		this.ideAtoAmbiental = ideAtoAmbiental;
		this.ideCondicionante = ideCondicionante;
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

	/**
	 * Getter do campo ideAtoAmbiental
	 *	
	 * @return the ideAtoAmbiental
	 */
	public Integer getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	/**
	 * Setter do campo  ideAtoAmbiental
	 * @param ideAtoAmbiental the ideAtoAmbiental to set
	 */
	public void setIdeAtoAmbiental(Integer ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideAtoAmbiental == null) ? 0 : ideAtoAmbiental.hashCode());
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
		CondicionanteAtoAmbientalPK other = (CondicionanteAtoAmbientalPK) obj;
		if (ideAtoAmbiental == null) {
			if (other.ideAtoAmbiental != null)
				return false;
		} else if (!ideAtoAmbiental.equals(other.ideAtoAmbiental))
			return false;
		if (ideCondicionante == null) {
			if (other.ideCondicionante != null)
				return false;
		} else if (!ideCondicionante.equals(other.ideCondicionante))
			return false;
		return true;
	}
	

}
