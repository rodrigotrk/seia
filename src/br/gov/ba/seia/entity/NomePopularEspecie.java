package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "nome_popular_especie")
public class NomePopularEspecie implements Serializable, BaseEntity {

	private static final long serialVersionUID = 2987878648181649359L;

	@Id
	@SequenceGenerator(name = "NOME_POPULAR_ESPECIE_GENERATOR", sequenceName = "nome_popular_especie_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOME_POPULAR_ESPECIE_GENERATOR")
	@Column(name = "ide_nome_popular_especie")
	private Integer ideNomePopularEspecie;

	@Column(name = "nom_popular_especie")
	private String nomPopularEspecie;

	@OneToMany
	@JoinColumn(name = "ide_nome_popular_especie", referencedColumnName = "ide_nome_popular_especie")
	private Collection<EspecieSupressaoNomePopularEspecie> especieSupressaoNomePopularEspecieCollection;
	
	@OneToMany
	@JoinColumn(name = "ide_nome_popular_especie", referencedColumnName = "ide_nome_popular_especie")
	private Collection<EspecieFlorestalNomePopularEspecie> especieFlorestalNomePopularEspecieCollection;

	public Integer getIdeNomePopularEspecie() {
		return ideNomePopularEspecie;
	}

	public void setIdeNomePopularEspecie(Integer ideNomePopularEspecie) {
		this.ideNomePopularEspecie = ideNomePopularEspecie;
	}

	public String getNomPopularEspecie() {
		return nomPopularEspecie;
	}

	public void setNomPopularEspecie(String nomPopularEspecie) {
		this.nomPopularEspecie = nomPopularEspecie;
	}

	public Collection<EspecieSupressaoNomePopularEspecie> getEspecieSupressaoNomePopularEspecieCollection() {
		return especieSupressaoNomePopularEspecieCollection;
	}

	public void setEspecieSupressaoNomePopularEspecieCollection(
			Collection<EspecieSupressaoNomePopularEspecie> especieSupressaoNomePopularEspecieCollection) {
		this.especieSupressaoNomePopularEspecieCollection = especieSupressaoNomePopularEspecieCollection;
	}

	public Collection<EspecieFlorestalNomePopularEspecie> getEspecieFlorestalNomePopularEspecieCollection() {
		return especieFlorestalNomePopularEspecieCollection;
	}

	public void setEspecieFlorestalNomePopularEspecieCollection(
			Collection<EspecieFlorestalNomePopularEspecie> especieFlorestalNomePopularEspecieCollection) {
		this.especieFlorestalNomePopularEspecieCollection = especieFlorestalNomePopularEspecieCollection;
	}

	@Override
	public Long getId() {
		return new Long(ideNomePopularEspecie);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideNomePopularEspecie == null) ? 0 : ideNomePopularEspecie
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
		NomePopularEspecie other = (NomePopularEspecie) obj;
		if (ideNomePopularEspecie == null) {
			if (other.ideNomePopularEspecie != null)
				return false;
		} else if (!ideNomePopularEspecie.equals(other.ideNomePopularEspecie))
			return false;
		return true;
	}
}
