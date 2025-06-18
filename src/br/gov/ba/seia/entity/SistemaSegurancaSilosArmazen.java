package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.Transient;


/**
 * The persistent class for the sistema_seguranca_silos_armazens database table.
 * 
 */
@Entity
@Table(name="sistema_seguranca_silos_armazens")
@NamedQueries({
	@NamedQuery(name = "SistemaSegurancaSilosArmazen.removerByIdeSilos", query ="DELETE FROM SistemaSegurancaSilosArmazen s WHERE s.silosArmazen = :ideSilosArmazen") })
public class SistemaSegurancaSilosArmazen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SISTEMA_SEGURANCA_SILOS_ARMAZENS_IDESISTEMASEGURANCASILOSARMAZENS_GENERATOR", sequenceName="SISTEMA_SEGURANCA_SILOS_ARMAZENS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SISTEMA_SEGURANCA_SILOS_ARMAZENS_IDESISTEMASEGURANCASILOSARMAZENS_GENERATOR")
	@Column(name="ide_sistema_seguranca_silos_armazens")
	private Integer ideSistemaSegurancaSilosArmazens;

	//bi-directional many-to-one association to SilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_silos_armazens")
	private SilosArmazen silosArmazen;

	//bi-directional many-to-one association to SilosArmazensSistemaSeguranca
	@ManyToOne
	@JoinColumn(name="ide_silos_armazens_sistema_seguranca")
	private SilosArmazensSistemaSeguranca silosArmazensSistemaSeguranca;

	@Transient
	private List<SilosArmazensSistemaSeguranca> silosArmazensSistemaSegurancas;
	
	public SistemaSegurancaSilosArmazen() {
	}

	public Integer getIdeSistemaSegurancaSilosArmazens() {
		return this.ideSistemaSegurancaSilosArmazens;
	}

	public void setIdeSistemaSegurancaSilosArmazens(Integer ideSistemaSegurancaSilosArmazens) {
		this.ideSistemaSegurancaSilosArmazens = ideSistemaSegurancaSilosArmazens;
	}

	public SilosArmazen getSilosArmazen() {
		return this.silosArmazen;
	}

	public void setSilosArmazen(SilosArmazen silosArmazen) {
		this.silosArmazen = silosArmazen;
	}

	public SilosArmazensSistemaSeguranca getSilosArmazensSistemaSeguranca() {
		return this.silosArmazensSistemaSeguranca;
	}

	public void setSilosArmazensSistemaSeguranca(SilosArmazensSistemaSeguranca silosArmazensSistemaSeguranca) {
		this.silosArmazensSistemaSeguranca = silosArmazensSistemaSeguranca;
	}

	public List<SilosArmazensSistemaSeguranca> getSilosArmazensSistemaSegurancas() {
		return silosArmazensSistemaSegurancas;
	}

	public void setSilosArmazensSistemaSegurancas(
			List<SilosArmazensSistemaSeguranca> silosArmazensSistemaSegurancas) {
		this.silosArmazensSistemaSegurancas = silosArmazensSistemaSegurancas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSistemaSegurancaSilosArmazens == null) ? 0
						: ideSistemaSegurancaSilosArmazens.hashCode());
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
		SistemaSegurancaSilosArmazen other = (SistemaSegurancaSilosArmazen) obj;
		if (ideSistemaSegurancaSilosArmazens == null) {
			if (other.ideSistemaSegurancaSilosArmazens != null)
				return false;
		} else if (!ideSistemaSegurancaSilosArmazens
				.equals(other.ideSistemaSegurancaSilosArmazens))
			return false;
		return true;
	}

	
}