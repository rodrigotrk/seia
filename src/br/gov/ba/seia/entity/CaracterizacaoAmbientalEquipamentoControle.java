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
 * The persistent class for the caracterizacao_ambiental_equipamento_controle database table.
 * 
 */
@Entity
@Table(name="caracterizacao_ambiental_equipamento_controle")
@NamedQueries({
	@NamedQuery(name="CaracterizacaoAmbientalEquipamentoControle.removerCaracterizacaoEquipamentoControle", query="DELETE FROM CaracterizacaoAmbientalEquipamentoControle s WHERE s.caracterizacaoAmbientalSilosArmazen = :ideCaracterizacaoAmbientalSilosArmazen")	
})
public class CaracterizacaoAmbientalEquipamentoControle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CARACTERIZACAO_AMBIENTAL_EQUIPAMENTO_CONTROLE_IDECARACTERIZACAOAMBIENTALEQUIPAMENTOCONTROLE_GENERATOR", sequenceName="CARACTERIZACAO_AMBIENTAL_EQUIPAMENTO_CONTROLE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARACTERIZACAO_AMBIENTAL_EQUIPAMENTO_CONTROLE_IDECARACTERIZACAOAMBIENTALEQUIPAMENTOCONTROLE_GENERATOR")
	@Column(name="ide_caracterizacao_ambiental_equipamento_controle")
	private Integer ideCaracterizacaoAmbientalEquipamentoControle;

	//bi-directional many-to-one association to CaracterizacaoAmbientalSilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_caracterizacao_ambiental_silos_armazens")
	private CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen;

	//bi-directional many-to-one association to EquipamentoControle
	@ManyToOne
	@JoinColumn(name="ide_equipamento_controle")
	private EquipamentoControle equipamentoControle;

	public CaracterizacaoAmbientalEquipamentoControle() {
	}

	public Integer getIdeCaracterizacaoAmbientalEquipamentoControle() {
		return this.ideCaracterizacaoAmbientalEquipamentoControle;
	}

	public void setIdeCaracterizacaoAmbientalEquipamentoControle(Integer ideCaracterizacaoAmbientalEquipamentoControle) {
		this.ideCaracterizacaoAmbientalEquipamentoControle = ideCaracterizacaoAmbientalEquipamentoControle;
	}

	public CaracterizacaoAmbientalSilosArmazen getCaracterizacaoAmbientalSilosArmazen() {
		return this.caracterizacaoAmbientalSilosArmazen;
	}

	public void setCaracterizacaoAmbientalSilosArmazen(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		this.caracterizacaoAmbientalSilosArmazen = caracterizacaoAmbientalSilosArmazen;
	}

	public EquipamentoControle getEquipamentoControle() {
		return this.equipamentoControle;
	}

	public void setEquipamentoControle(EquipamentoControle equipamentoControle) {
		this.equipamentoControle = equipamentoControle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCaracterizacaoAmbientalEquipamentoControle == null) ? 0
						: ideCaracterizacaoAmbientalEquipamentoControle
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
		CaracterizacaoAmbientalEquipamentoControle other = (CaracterizacaoAmbientalEquipamentoControle) obj;
		if (ideCaracterizacaoAmbientalEquipamentoControle == null) {
			if (other.ideCaracterizacaoAmbientalEquipamentoControle != null)
				return false;
		} else if (!ideCaracterizacaoAmbientalEquipamentoControle
				.equals(other.ideCaracterizacaoAmbientalEquipamentoControle))
			return false;
		return true;
	}

	
}