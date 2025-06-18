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
 * The persistent class for the caracterizacao_ambiental_caracterizacao_atmosferica database table.
 * 
 */
@Entity
@Table(name="caracterizacao_ambiental_caracterizacao_atmosferica")
@NamedQueries({
	@NamedQuery(name="CaracterizacaoAmbientalCaracterizacaoAtmosferica.removerCaracterizacaoAtmosferica", query="DELETE FROM CaracterizacaoAmbientalCaracterizacaoAtmosferica s WHERE s.caracterizacaoAmbientalSilosArmazen = :ideCaracterizacaoAmbientalSilosArmazen")	
})
public class CaracterizacaoAmbientalCaracterizacaoAtmosferica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CARACTERIZACAO_AMBIENTAL_CARACTERIZACAO_ATMOSFERICA_IDECARACTERIZACAOAMBIENTALCARACTERIZACAOATMOSFERICA_GENERATOR", sequenceName="CARACTERIZACAO_AMBIENTAL_CARACTERIZACAO_ATMOSFERICA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARACTERIZACAO_AMBIENTAL_CARACTERIZACAO_ATMOSFERICA_IDECARACTERIZACAOAMBIENTALCARACTERIZACAOATMOSFERICA_GENERATOR")
	@Column(name="ide_caracterizacao_ambiental_caracterizacao_atmosferica")
	private Integer ideCaracterizacaoAmbientalCaracterizacaoAtmosferica;

	//bi-directional many-to-one association to CaracterizacaoAmbientalSilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_caracterizacao_ambiental_silos_armazens")
	private CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen;

	//bi-directional many-to-one association to SilosArmazensCaracterizacaoAtmosferica
	@ManyToOne
	@JoinColumn(name="ide_silos_armazens_caracterizacao_atmosferica")
	private SilosArmazensCaracterizacaoAtmosferica silosArmazensCaracterizacaoAtmosferica;

	public CaracterizacaoAmbientalCaracterizacaoAtmosferica() {
	}

	public Integer getIdeCaracterizacaoAmbientalCaracterizacaoAtmosferica() {
		return this.ideCaracterizacaoAmbientalCaracterizacaoAtmosferica;
	}

	public void setIdeCaracterizacaoAmbientalCaracterizacaoAtmosferica(Integer ideCaracterizacaoAmbientalCaracterizacaoAtmosferica) {
		this.ideCaracterizacaoAmbientalCaracterizacaoAtmosferica = ideCaracterizacaoAmbientalCaracterizacaoAtmosferica;
	}

	public CaracterizacaoAmbientalSilosArmazen getCaracterizacaoAmbientalSilosArmazen() {
		return this.caracterizacaoAmbientalSilosArmazen;
	}

	public void setCaracterizacaoAmbientalSilosArmazen(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		this.caracterizacaoAmbientalSilosArmazen = caracterizacaoAmbientalSilosArmazen;
	}

	public SilosArmazensCaracterizacaoAtmosferica getSilosArmazensCaracterizacaoAtmosferica() {
		return this.silosArmazensCaracterizacaoAtmosferica;
	}

	public void setSilosArmazensCaracterizacaoAtmosferica(SilosArmazensCaracterizacaoAtmosferica silosArmazensCaracterizacaoAtmosferica) {
		this.silosArmazensCaracterizacaoAtmosferica = silosArmazensCaracterizacaoAtmosferica;
	}

}