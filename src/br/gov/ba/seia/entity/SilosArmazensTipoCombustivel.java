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
 * The persistent class for the silos_armazens_tipo_combustivel database table.
 * 
 */
@Entity
@Table(name="silos_armazens_tipo_combustivel")
@NamedQueries({
	@NamedQuery(name = "SilosArmazensTipoCombustivel.removerByIdeSilos", query ="DELETE FROM SilosArmazensTipoCombustivel s WHERE s.silosArmazen = :ideSilosArmazen") })
public class SilosArmazensTipoCombustivel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SILOS_ARMAZENS_TIPO_COMBUSTIVEL_IDESILOSARMAZENSTIPOCOMBUSTIVEL_GENERATOR", sequenceName="SILOS_ARMAZENS_TIPO_COMBUSTIVEL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SILOS_ARMAZENS_TIPO_COMBUSTIVEL_IDESILOSARMAZENSTIPOCOMBUSTIVEL_GENERATOR")
	@Column(name="ide_silos_armazens_tipo_combustivel")
	private Integer ideSilosArmazensTipoCombustivel;

	@Column(name="num_raf")
	private String numRaf;

	//bi-directional many-to-one association to SilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_silos_armazens")
	private SilosArmazen silosArmazen;

	//bi-directional many-to-one association to TipoCombustivelSiloArmazen
	@ManyToOne
	@JoinColumn(name="ide_tipo_combustivel_silo_armazens")
	private TipoCombustivelSiloArmazen tipoCombustivelSiloArmazen;

	public SilosArmazensTipoCombustivel() {
	}

	public Integer getIdeSilosArmazensTipoCombustivel() {
		return this.ideSilosArmazensTipoCombustivel;
	}

	public void setIdeSilosArmazensTipoCombustivel(Integer ideSilosArmazensTipoCombustivel) {
		this.ideSilosArmazensTipoCombustivel = ideSilosArmazensTipoCombustivel;
	}

	public String getNumRaf() {
		return this.numRaf;
	}

	public void setNumRaf(String numRaf) {
		this.numRaf = numRaf;
	}

	public SilosArmazen getSilosArmazen() {
		return this.silosArmazen;
	}

	public void setSilosArmazen(SilosArmazen silosArmazen) {
		this.silosArmazen = silosArmazen;
	}

	public TipoCombustivelSiloArmazen getTipoCombustivelSiloArmazen() {
		return this.tipoCombustivelSiloArmazen;
	}

	public void setTipoCombustivelSiloArmazen(TipoCombustivelSiloArmazen tipoCombustivelSiloArmazen) {
		this.tipoCombustivelSiloArmazen = tipoCombustivelSiloArmazen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSilosArmazensTipoCombustivel == null) ? 0
						: ideSilosArmazensTipoCombustivel.hashCode());
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
		SilosArmazensTipoCombustivel other = (SilosArmazensTipoCombustivel) obj;
		if (ideSilosArmazensTipoCombustivel == null) {
			if (other.ideSilosArmazensTipoCombustivel != null)
				return false;
		} else if (!ideSilosArmazensTipoCombustivel
				.equals(other.ideSilosArmazensTipoCombustivel))
			return false;
		return true;
	}

	
}