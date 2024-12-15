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
 * The persistent class for the silos_armazens_sistema_tratamento_agua database table.
 * 
 */
@Entity
@Table(name="silos_armazens_sistema_tratamento_agua")
@NamedQueries({
	@NamedQuery(name="SilosArmazensSistemaTratamentoAgua.removerTratamentoAgua", query="DELETE FROM SilosArmazensSistemaTratamentoAgua s WHERE s.caracterizacaoAmbientalSilosArmazen = :ideCaracterizacaoAmbientalSilosArmazen")	
})
public class SilosArmazensSistemaTratamentoAgua implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SILOS_ARMAZENS_SISTEMA_TRATAMENTO_AGUA_IDESILOSARMAZENSSISTEMATRATAMENTOAGUA_GENERATOR", sequenceName="SILOS_ARMAZENS_SISTEMA_TRATAMENTO_AGUA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SILOS_ARMAZENS_SISTEMA_TRATAMENTO_AGUA_IDESILOSARMAZENSSISTEMATRATAMENTOAGUA_GENERATOR")
	@Column(name="ide_silos_armazens_sistema_tratamento_agua")
	private Integer ideSilosArmazensSistemaTratamentoAgua;

	//bi-directional many-to-one association to CaracterizacaoAmbientalSilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_caracterizacao_ambiental_silos_armazens")
	private CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen;

	//bi-directional many-to-one association to SistemaTratamentoAgua
	@ManyToOne
	@JoinColumn(name="ide_sistema_tratamento_agua")
	private SistemaTratamentoAgua sistemaTratamentoAgua;

	public SilosArmazensSistemaTratamentoAgua() {
	}

	public Integer getIdeSilosArmazensSistemaTratamentoAgua() {
		return this.ideSilosArmazensSistemaTratamentoAgua;
	}

	public void setIdeSilosArmazensSistemaTratamentoAgua(Integer ideSilosArmazensSistemaTratamentoAgua) {
		this.ideSilosArmazensSistemaTratamentoAgua = ideSilosArmazensSistemaTratamentoAgua;
	}

	public CaracterizacaoAmbientalSilosArmazen getCaracterizacaoAmbientalSilosArmazen() {
		return this.caracterizacaoAmbientalSilosArmazen;
	}

	public void setCaracterizacaoAmbientalSilosArmazen(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		this.caracterizacaoAmbientalSilosArmazen = caracterizacaoAmbientalSilosArmazen;
	}

	public SistemaTratamentoAgua getSistemaTratamentoAgua() {
		return this.sistemaTratamentoAgua;
	}

	public void setSistemaTratamentoAgua(SistemaTratamentoAgua sistemaTratamentoAgua) {
		this.sistemaTratamentoAgua = sistemaTratamentoAgua;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSilosArmazensSistemaTratamentoAgua == null) ? 0
						: ideSilosArmazensSistemaTratamentoAgua.hashCode());
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
		SilosArmazensSistemaTratamentoAgua other = (SilosArmazensSistemaTratamentoAgua) obj;
		if (ideSilosArmazensSistemaTratamentoAgua == null) {
			if (other.ideSilosArmazensSistemaTratamentoAgua != null)
				return false;
		} else if (!ideSilosArmazensSistemaTratamentoAgua
				.equals(other.ideSilosArmazensSistemaTratamentoAgua))
			return false;
		return true;
	}

	
}