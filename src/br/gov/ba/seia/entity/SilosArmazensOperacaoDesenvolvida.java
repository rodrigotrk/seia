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
 * The persistent class for the silos_armazens_operacao_desenvolvida database table.
 * 
 */
@Entity
@Table(name="silos_armazens_operacao_desenvolvida")
@NamedQueries({
	@NamedQuery(name = "SilosArmazensOperacaoDesenvolvida.removerByIdeSilos", query ="DELETE FROM SilosArmazensOperacaoDesenvolvida s WHERE s.silosArmazen = :ideSilosArmazen") })
public class SilosArmazensOperacaoDesenvolvida implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SILOS_ARMAZENS_OPERACAO_DESENVOLVIDA_IDESILOSARMAZENSOPERACAODESENVOLVIDA_GENERATOR", sequenceName="SILOS_ARMAZENS_OPERACAO_DESENVOLVIDA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SILOS_ARMAZENS_OPERACAO_DESENVOLVIDA_IDESILOSARMAZENSOPERACAODESENVOLVIDA_GENERATOR")
	@Column(name="ide_silos_armazens_operacao_desenvolvida")
	private Integer ideSilosArmazensOperacaoDesenvolvida;

	//bi-directional many-to-one association to OperacaoDesenvolvidaSilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_operacao_desenvolvida_silos_armazens")
	private OperacaoDesenvolvidaSilosArmazen operacaoDesenvolvidaSilosArmazen;

	//bi-directional many-to-one association to SilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_silos_armazens")
	private SilosArmazen silosArmazen;

	public SilosArmazensOperacaoDesenvolvida() {
	}

	public Integer getIdeSilosArmazensOperacaoDesenvolvida() {
		return this.ideSilosArmazensOperacaoDesenvolvida;
	}

	public void setIdeSilosArmazensOperacaoDesenvolvida(Integer ideSilosArmazensOperacaoDesenvolvida) {
		this.ideSilosArmazensOperacaoDesenvolvida = ideSilosArmazensOperacaoDesenvolvida;
	}

	public OperacaoDesenvolvidaSilosArmazen getOperacaoDesenvolvidaSilosArmazen() {
		return this.operacaoDesenvolvidaSilosArmazen;
	}

	public void setOperacaoDesenvolvidaSilosArmazen(OperacaoDesenvolvidaSilosArmazen operacaoDesenvolvidaSilosArmazen) {
		this.operacaoDesenvolvidaSilosArmazen = operacaoDesenvolvidaSilosArmazen;
	}

	public SilosArmazen getSilosArmazen() {
		return this.silosArmazen;
	}

	public void setSilosArmazen(SilosArmazen silosArmazen) {
		this.silosArmazen = silosArmazen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSilosArmazensOperacaoDesenvolvida == null) ? 0
						: ideSilosArmazensOperacaoDesenvolvida.hashCode());
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
		SilosArmazensOperacaoDesenvolvida other = (SilosArmazensOperacaoDesenvolvida) obj;
		if (ideSilosArmazensOperacaoDesenvolvida == null) {
			if (other.ideSilosArmazensOperacaoDesenvolvida != null)
				return false;
		} else if (!ideSilosArmazensOperacaoDesenvolvida
				.equals(other.ideSilosArmazensOperacaoDesenvolvida))
			return false;
		return true;
	}

	
}