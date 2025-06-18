package br.gov.ba.seia.entity;


import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * The persistent class for the substancia_mineral database table.
 * 
 */
@Entity
@Table(name = "substancia_mineral")
public class SubstanciaMineral implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SUBSTANCIA_MINERAL_IDESUBSTANCIAMINERAL_GENERATOR", sequenceName = "SUBSTANCIA_MINERAL_IDE_SUBSTANCIA_MINERAL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBSTANCIA_MINERAL_IDESUBSTANCIAMINERAL_GENERATOR")
	@Column(name = "ide_substancia_mineral")
	private Integer ideSubstanciaMineral;

	@Column(name = "nom_substancia_mineral")
	private String nomSubstanciaMineral;
	
	@JoinTable(name = "substancia_mineral_tipologia", joinColumns = { @JoinColumn(name = "ide_substancia_mineral", referencedColumnName = "ide_substancia_mineral", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia", nullable = false) })
	@ManyToMany(fetch = FetchType.LAZY)
	private Collection<Tipologia> tipologiaCollection;
	

	public SubstanciaMineral() {
	}

	public SubstanciaMineral(Integer ideSubstanciaMineral) {
		this.ideSubstanciaMineral = ideSubstanciaMineral;
	}

	public Integer getIdeSubstanciaMineral() {
		return ideSubstanciaMineral;
	}

	public void setIdeSubstanciaMineral(Integer ideSubstanciaMineral) {
		this.ideSubstanciaMineral = ideSubstanciaMineral;
	}

	public String getNomSubstanciaMineral() {
		return nomSubstanciaMineral;
	}

	public void setNomSubstanciaMineral(String nomSubstanciaMineral) {
		this.nomSubstanciaMineral = nomSubstanciaMineral;
	}

	public boolean isOutros(){
		return nomSubstanciaMineral.compareTo("Outros") == 0;
	}

	@Override
	public Long getId() {
		return new Long(this.ideSubstanciaMineral);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSubstanciaMineral == null) ? 0 : ideSubstanciaMineral
						.hashCode());
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
		SubstanciaMineral other = (SubstanciaMineral) obj;
		if (ideSubstanciaMineral == null) {
			if (other.ideSubstanciaMineral != null)
				return false;
		} else if (!ideSubstanciaMineral.equals(other.ideSubstanciaMineral))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SubstanciaMineral =" + nomSubstanciaMineral;
	}

}