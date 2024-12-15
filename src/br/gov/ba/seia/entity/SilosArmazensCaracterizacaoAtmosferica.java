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
 * The persistent class for the silos_armazens_caracterizacao_atmosferica database table.
 * 
 */
@Entity
@Table(name="silos_armazens_caracterizacao_atmosferica")
@NamedQuery(name="SilosArmazensCaracterizacaoAtmosferica.findAll", query="SELECT s FROM SilosArmazensCaracterizacaoAtmosferica s")
public class SilosArmazensCaracterizacaoAtmosferica implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SILOS_ARMAZENS_CARACTERIZACAO_ATMOSFERICA_IDESILOSARMAZENSCARACTERIZACAOATMOSFERICA_GENERATOR", sequenceName="SILOS_ARMAZENS_CARACTERIZACAO_ATMOSFERICA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SILOS_ARMAZENS_CARACTERIZACAO_ATMOSFERICA_IDESILOSARMAZENSCARACTERIZACAOATMOSFERICA_GENERATOR")
	@Column(name="ide_silos_armazens_caracterizacao_atmosferica")
	private Integer ideSilosArmazensCaracterizacaoAtmosferica;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_caracterizacao_atmosferica")
	private String nomCaracterizacaoAtmosferica;

	//bi-directional many-to-one association to CaracterizacaoAmbientalCaracterizacaoAtmosferica
	@OneToMany(mappedBy="silosArmazensCaracterizacaoAtmosferica")
	private List<CaracterizacaoAmbientalCaracterizacaoAtmosferica> caracterizacaoAmbientalCaracterizacaoAtmosfericas;

	public SilosArmazensCaracterizacaoAtmosferica() {
	}

	public Integer getIdeSilosArmazensCaracterizacaoAtmosferica() {
		return this.ideSilosArmazensCaracterizacaoAtmosferica;
	}

	public void setIdeSilosArmazensCaracterizacaoAtmosferica(Integer ideSilosArmazensCaracterizacaoAtmosferica) {
		this.ideSilosArmazensCaracterizacaoAtmosferica = ideSilosArmazensCaracterizacaoAtmosferica;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomCaracterizacaoAtmosferica() {
		return this.nomCaracterizacaoAtmosferica;
	}

	public void setNomCaracterizacaoAtmosferica(String nomCaracterizacaoAtmosferica) {
		this.nomCaracterizacaoAtmosferica = nomCaracterizacaoAtmosferica;
	}

	public List<CaracterizacaoAmbientalCaracterizacaoAtmosferica> getCaracterizacaoAmbientalCaracterizacaoAtmosfericas() {
		return this.caracterizacaoAmbientalCaracterizacaoAtmosfericas;
	}

	public void setCaracterizacaoAmbientalCaracterizacaoAtmosfericas(List<CaracterizacaoAmbientalCaracterizacaoAtmosferica> caracterizacaoAmbientalCaracterizacaoAtmosfericas) {
		this.caracterizacaoAmbientalCaracterizacaoAtmosfericas = caracterizacaoAmbientalCaracterizacaoAtmosfericas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSilosArmazensCaracterizacaoAtmosferica == null) ? 0
						: ideSilosArmazensCaracterizacaoAtmosferica.hashCode());
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
		SilosArmazensCaracterizacaoAtmosferica other = (SilosArmazensCaracterizacaoAtmosferica) obj;
		if (ideSilosArmazensCaracterizacaoAtmosferica == null) {
			if (other.ideSilosArmazensCaracterizacaoAtmosferica != null)
				return false;
		} else if (!ideSilosArmazensCaracterizacaoAtmosferica
				.equals(other.ideSilosArmazensCaracterizacaoAtmosferica))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideSilosArmazensCaracterizacaoAtmosferica);
	}

	
}