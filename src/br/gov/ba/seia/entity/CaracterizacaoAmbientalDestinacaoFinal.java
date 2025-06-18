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
 * The persistent class for the caracterizacao_ambiental_destinacao_final database table.
 * 
 */
@Entity
@Table(name="caracterizacao_ambiental_destinacao_final")
@NamedQueries({
	@NamedQuery(name="CaracterizacaoAmbientalDestinacaoFinal.removerDestinacaoFinal", query="DELETE FROM CaracterizacaoAmbientalDestinacaoFinal s WHERE s.caracterizacaoAmbientalSilosArmazen = :ideCaracterizacaoAmbientalSilosArmazen")	
})
public class CaracterizacaoAmbientalDestinacaoFinal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CARACTERIZACAO_AMBIENTAL_DESTINACAO_FINAL_IDECARACTERIZACAOAMBIENTALDESTINACAOFINAL_GENERATOR", sequenceName="CARACTERIZACAO_AMBIENTAL_DESTINACAO_FINAL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARACTERIZACAO_AMBIENTAL_DESTINACAO_FINAL_IDECARACTERIZACAOAMBIENTALDESTINACAOFINAL_GENERATOR")
	@Column(name="ide_caracterizacao_ambiental_destinacao_final")
	private Integer ideCaracterizacaoAmbientalDestinacaoFinal;

	//bi-directional many-to-one association to CaracterizacaoAmbientalSilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_caracterizacao_ambiental_silos_armazens")
	private CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen;

	//bi-directional many-to-one association to DestinacaoFinal
	@ManyToOne
	@JoinColumn(name="ide_destinacao_final")
	private DestinacaoFinal destinacaoFinal;

	public CaracterizacaoAmbientalDestinacaoFinal() {
	}

	public Integer getIdeCaracterizacaoAmbientalDestinacaoFinal() {
		return this.ideCaracterizacaoAmbientalDestinacaoFinal;
	}

	public void setIdeCaracterizacaoAmbientalDestinacaoFinal(Integer ideCaracterizacaoAmbientalDestinacaoFinal) {
		this.ideCaracterizacaoAmbientalDestinacaoFinal = ideCaracterizacaoAmbientalDestinacaoFinal;
	}

	public CaracterizacaoAmbientalSilosArmazen getCaracterizacaoAmbientalSilosArmazen() {
		return this.caracterizacaoAmbientalSilosArmazen;
	}

	public void setCaracterizacaoAmbientalSilosArmazen(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		this.caracterizacaoAmbientalSilosArmazen = caracterizacaoAmbientalSilosArmazen;
	}

	public DestinacaoFinal getDestinacaoFinal() {
		return this.destinacaoFinal;
	}

	public void setDestinacaoFinal(DestinacaoFinal destinacaoFinal) {
		this.destinacaoFinal = destinacaoFinal;
	}

}