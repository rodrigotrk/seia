package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the classificacao_residuo_caracterizacao_ambiental database table.
 * 
 */
@Entity
@Table(name="classificacao_residuo_caracterizacao_ambiental")
@NamedQueries({
	@NamedQuery(name="ClassificacaoResiduoCaracterizacaoAmbiental.removerClassificacaoResiduo", query="DELETE FROM ClassificacaoResiduoCaracterizacaoAmbiental s WHERE s.caracterizacaoAmbientalSilosArmazen = :ideCaracterizacaoAmbientalSilosArmazen")	
})
public class ClassificacaoResiduoCaracterizacaoAmbiental implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CLASSIFICACAO_RESIDUO_CARACTERIZACAO_AMBIENTAL_IDECLASSIFICACAORESIDUOCARACTERIZACAOAMBIENTAL_GENERATOR", sequenceName="CLASSIFICACAO_RESIDUO_CARACTERIZACAO_AMBIENTAL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CLASSIFICACAO_RESIDUO_CARACTERIZACAO_AMBIENTAL_IDECLASSIFICACAORESIDUOCARACTERIZACAOAMBIENTAL_GENERATOR")
	@Column(name="ide_classificacao_residuo_caracterizacao_ambiental")
	private Integer ideClassificacaoResiduoCaracterizacaoAmbiental;

	//bi-directional many-to-one association to CaracterizacaoAmbientalSilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_caracterizacao_ambiental_silos_armazens")
	private CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen;

	//bi-directional many-to-one association to ClassificacaoResiduo
	@ManyToOne
	@JoinColumn(name="ide_classificacao_residuo")
	private ClassificacaoResiduo classificacaoResiduo;

	public ClassificacaoResiduoCaracterizacaoAmbiental() {
	}

	public Integer getIdeClassificacaoResiduoCaracterizacaoAmbiental() {
		return this.ideClassificacaoResiduoCaracterizacaoAmbiental;
	}

	public void setIdeClassificacaoResiduoCaracterizacaoAmbiental(Integer ideClassificacaoResiduoCaracterizacaoAmbiental) {
		this.ideClassificacaoResiduoCaracterizacaoAmbiental = ideClassificacaoResiduoCaracterizacaoAmbiental;
	}

	public CaracterizacaoAmbientalSilosArmazen getCaracterizacaoAmbientalSilosArmazen() {
		return this.caracterizacaoAmbientalSilosArmazen;
	}

	public void setCaracterizacaoAmbientalSilosArmazen(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		this.caracterizacaoAmbientalSilosArmazen = caracterizacaoAmbientalSilosArmazen;
	}

	public ClassificacaoResiduo getClassificacaoResiduo() {
		return this.classificacaoResiduo;
	}

	public void setClassificacaoResiduo(ClassificacaoResiduo classificacaoResiduo) {
		this.classificacaoResiduo = classificacaoResiduo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideClassificacaoResiduoCaracterizacaoAmbiental == null) ? 0
						: ideClassificacaoResiduoCaracterizacaoAmbiental
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
		ClassificacaoResiduoCaracterizacaoAmbiental other = (ClassificacaoResiduoCaracterizacaoAmbiental) obj;
		if (ideClassificacaoResiduoCaracterizacaoAmbiental == null) {
			if (other.ideClassificacaoResiduoCaracterizacaoAmbiental != null)
				return false;
		} else if (!ideClassificacaoResiduoCaracterizacaoAmbiental
				.equals(other.ideClassificacaoResiduoCaracterizacaoAmbiental))
			return false;
		return true;
	}

	
}