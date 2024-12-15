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
 * The persistent class for the sistema_tratamento_agua database table.
 * 
 */
@Entity
@Table(name="sistema_tratamento_agua")
@NamedQuery(name="SistemaTratamentoAgua.findAll", query="SELECT s FROM SistemaTratamentoAgua s")
public class SistemaTratamentoAgua implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SISTEMA_TRATAMENTO_AGUA_IDESISTEMATRATAMENTOAGUA_GENERATOR", sequenceName="SISTEMA_TRATAMENTO_AGUA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SISTEMA_TRATAMENTO_AGUA_IDESISTEMATRATAMENTOAGUA_GENERATOR")
	@Column(name="ide_sistema_tratamento_agua")
	private Integer ideSistemaTratamentoAgua;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_sistema_tratamento_agua")
	private String nomSistemaTratamentoAgua;

	//bi-directional many-to-one association to SilosArmazensSistemaTratamentoAgua
	@OneToMany(mappedBy="sistemaTratamentoAgua")
	private List<SilosArmazensSistemaTratamentoAgua> silosArmazensSistemaTratamentoAguas;

	public SistemaTratamentoAgua() {
	}

	public Integer getIdeSistemaTratamentoAgua() {
		return this.ideSistemaTratamentoAgua;
	}

	public void setIdeSistemaTratamentoAgua(Integer ideSistemaTratamentoAgua) {
		this.ideSistemaTratamentoAgua = ideSistemaTratamentoAgua;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomSistemaTratamentoAgua() {
		return this.nomSistemaTratamentoAgua;
	}

	public void setNomSistemaTratamentoAgua(String nomSistemaTratamentoAgua) {
		this.nomSistemaTratamentoAgua = nomSistemaTratamentoAgua;
	}

	public List<SilosArmazensSistemaTratamentoAgua> getSilosArmazensSistemaTratamentoAguas() {
		return this.silosArmazensSistemaTratamentoAguas;
	}

	public void setSilosArmazensSistemaTratamentoAguas(List<SilosArmazensSistemaTratamentoAgua> silosArmazensSistemaTratamentoAguas) {
		this.silosArmazensSistemaTratamentoAguas = silosArmazensSistemaTratamentoAguas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSistemaTratamentoAgua == null) ? 0
						: ideSistemaTratamentoAgua.hashCode());
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
		SistemaTratamentoAgua other = (SistemaTratamentoAgua) obj;
		if (ideSistemaTratamentoAgua == null) {
			if (other.ideSistemaTratamentoAgua != null)
				return false;
		} else if (!ideSistemaTratamentoAgua
				.equals(other.ideSistemaTratamentoAgua))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideSistemaTratamentoAgua);
	}

	
}