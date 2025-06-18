package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the silos_armazens_sistema_seguranca database table.
 * 
 */
@Entity
@Table(name="silos_armazens_sistema_seguranca")
@NamedQuery(name="SilosArmazensSistemaSeguranca.findAll", query="SELECT s FROM SilosArmazensSistemaSeguranca s")
public class SilosArmazensSistemaSeguranca implements Serializable,BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SILOS_ARMAZENS_SISTEMA_SEGURANCA_IDESILOSARMAZENSSISTEMASEGURANCA_GENERATOR", sequenceName="SILOS_ARMAZENS_SISTEMA_SEGURANCA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SILOS_ARMAZENS_SISTEMA_SEGURANCA_IDESILOSARMAZENSSISTEMASEGURANCA_GENERATOR")
	@Column(name="ide_silos_armazens_sistema_seguranca")
	private Integer ideSilosArmazensSistemaSeguranca;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_sistema_segurana")
	private String nomSistemaSegurana;

	//bi-directional many-to-one association to SistemaSegurancaSilosArmazen
	@OneToMany(mappedBy="silosArmazensSistemaSeguranca")
	private List<SistemaSegurancaSilosArmazen> sistemaSegurancaSilosArmazens;

	public SilosArmazensSistemaSeguranca() {
	}

	public Integer getIdeSilosArmazensSistemaSeguranca() {
		return this.ideSilosArmazensSistemaSeguranca;
	}

	public void setIdeSilosArmazensSistemaSeguranca(Integer ideSilosArmazensSistemaSeguranca) {
		this.ideSilosArmazensSistemaSeguranca = ideSilosArmazensSistemaSeguranca;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomSistemaSegurana() {
		return this.nomSistemaSegurana;
	}

	public void setNomSistemaSegurana(String nomSistemaSegurana) {
		this.nomSistemaSegurana = nomSistemaSegurana;
	}

	public List<SistemaSegurancaSilosArmazen> getSistemaSegurancaSilosArmazens() {
		return this.sistemaSegurancaSilosArmazens;
	}

	public void setSistemaSegurancaSilosArmazens(List<SistemaSegurancaSilosArmazen> sistemaSegurancaSilosArmazens) {
		this.sistemaSegurancaSilosArmazens = sistemaSegurancaSilosArmazens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSilosArmazensSistemaSeguranca == null) ? 0
						: ideSilosArmazensSistemaSeguranca.hashCode());
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
		SilosArmazensSistemaSeguranca other = (SilosArmazensSistemaSeguranca) obj;
		if (ideSilosArmazensSistemaSeguranca == null) {
			if (other.ideSilosArmazensSistemaSeguranca != null)
				return false;
		} else if (!ideSilosArmazensSistemaSeguranca
				.equals(other.ideSilosArmazensSistemaSeguranca))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideSilosArmazensSistemaSeguranca);
	}

	
}