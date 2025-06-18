/**
 * 
 */
package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.SimpleEntityImpl;

/**
 * @author lesantos
 *
 */
@Entity
@Table(name="obras_infra_complementares")
@XmlRootElement
public class ObrasInfraComplementares extends SimpleEntityImpl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="ide_obras_infra_complementares_seq", sequenceName="ide_obras_infra_complementares_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ide_obras_infra_complementares_seq")
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_obras_infra_complementares")
	private Integer ideObrasInfraComplementares;

	@NotNull
	@Column(name = "nom_obras_infra_complementares")
	private String nome;
	
	@NotNull
	@Column(name = "ind_ativo")
	private boolean ativo;
	
	@Override
	public Long getId() {
		return new Long(this.ideObrasInfraComplementares);
	}

	/**
	 * @return the ideObrasInfraComplementares
	 */
	public Integer getIdeObrasInfraComplementares() {
		return ideObrasInfraComplementares;
	}

	/**
	 * @param ideObrasInfraComplementares the ideObrasInfraComplementares to set
	 */
	public void setIdeObrasInfraComplementares(Integer ideObrasInfraComplementares) {
		this.ideObrasInfraComplementares = ideObrasInfraComplementares;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ObrasInfraComplementares other = (ObrasInfraComplementares) obj;
		if (ideObrasInfraComplementares == null) {
			if (other.ideObrasInfraComplementares != null) {
				return false;
			}
		} else if (!ideObrasInfraComplementares.equals(other.ideObrasInfraComplementares)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideObrasInfraComplementares  == null) ? 0 : ideObrasInfraComplementares.hashCode());
		return result;
	}
}
