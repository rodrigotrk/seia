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
 * The persistent class for the caracterizacao_ambiental_medida_controle_emissao database table.
 * 
 */
@Entity
@Table(name="caracterizacao_ambiental_medida_controle_emissao")
@NamedQueries({
	@NamedQuery(name="CaracterizacaoAmbientalMedidaControleEmissao.removerCaracterizacaoEquipamentoControle", query="DELETE FROM CaracterizacaoAmbientalMedidaControleEmissao s WHERE s.caracterizacaoAmbientalSilosArmazen = :ideCaracterizacaoAmbientalSilosArmazen")	
})
public class CaracterizacaoAmbientalMedidaControleEmissao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CARACTERIZACAO_AMBIENTAL_MEDIDA_CONTROLE_EMISSAO_IDECARACTERIZACAOAMBIENTALMEDIDACONTROLEEMISSAO_GENERATOR", sequenceName="CARACTERIZACAO_AMBIENTAL_MEDIDA_CONTROLE_EMISSAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARACTERIZACAO_AMBIENTAL_MEDIDA_CONTROLE_EMISSAO_IDECARACTERIZACAOAMBIENTALMEDIDACONTROLEEMISSAO_GENERATOR")
	@Column(name="ide_caracterizacao_ambiental_medida_controle_emissao")
	private Integer ideCaracterizacaoAmbientalMedidaControleEmissao;

	//bi-directional many-to-one association to CaracterizacaoAmbientalSilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_caracterizacao_ambiental_silos_armazens")
	private CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen;

	//bi-directional many-to-one association to MedidaControleEmissao
	@ManyToOne
	@JoinColumn(name="ide_medida_controle_emissao")
	private MedidaControleEmissao medidaControleEmissao;

	public CaracterizacaoAmbientalMedidaControleEmissao() {
	}

	public Integer getIdeCaracterizacaoAmbientalMedidaControleEmissao() {
		return this.ideCaracterizacaoAmbientalMedidaControleEmissao;
	}

	public void setIdeCaracterizacaoAmbientalMedidaControleEmissao(Integer ideCaracterizacaoAmbientalMedidaControleEmissao) {
		this.ideCaracterizacaoAmbientalMedidaControleEmissao = ideCaracterizacaoAmbientalMedidaControleEmissao;
	}

	public CaracterizacaoAmbientalSilosArmazen getCaracterizacaoAmbientalSilosArmazen() {
		return this.caracterizacaoAmbientalSilosArmazen;
	}

	public void setCaracterizacaoAmbientalSilosArmazen(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		this.caracterizacaoAmbientalSilosArmazen = caracterizacaoAmbientalSilosArmazen;
	}

	public MedidaControleEmissao getMedidaControleEmissao() {
		return this.medidaControleEmissao;
	}

	public void setMedidaControleEmissao(MedidaControleEmissao medidaControleEmissao) {
		this.medidaControleEmissao = medidaControleEmissao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCaracterizacaoAmbientalMedidaControleEmissao == null) ? 0
						: ideCaracterizacaoAmbientalMedidaControleEmissao
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
		CaracterizacaoAmbientalMedidaControleEmissao other = (CaracterizacaoAmbientalMedidaControleEmissao) obj;
		if (ideCaracterizacaoAmbientalMedidaControleEmissao == null) {
			if (other.ideCaracterizacaoAmbientalMedidaControleEmissao != null)
				return false;
		} else if (!ideCaracterizacaoAmbientalMedidaControleEmissao
				.equals(other.ideCaracterizacaoAmbientalMedidaControleEmissao))
			return false;
		return true;
	}

	
}