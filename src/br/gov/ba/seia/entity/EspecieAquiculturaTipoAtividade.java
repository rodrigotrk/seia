package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the especie_aquicultura_tipo_atividade database table.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 05/11/2014
 */
@Entity
@Table(name="especie_aquicultura_tipo_atividade")
@NamedQuery(name="EspecieAquiculturaTipoAtividade.findAll", query="SELECT e FROM EspecieAquiculturaTipoAtividade e")
public class EspecieAquiculturaTipoAtividade implements Serializable, BaseEntity, Comparable<EspecieAquiculturaTipoAtividade> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ide_especie_aquicultura_tipo_atividade")
	private Integer ideEspecieAquiculturaTipoAtividade;

	//bi-directional many-to-one association to AquiculturaTipoAtividade
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_aquicultura_tipo_atividade")
	private AquiculturaTipoAtividade ideAquiculturaTipoAtividade;

	//bi-directional many-to-one association to Especie
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_especie")
	private Especie ideEspecie;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="ideEspecieAquiculturaTipoAtividade")
	private Collection<FceAquiculturaEspecie> fceAquiculturaEspecieCollection;
	
	public EspecieAquiculturaTipoAtividade() {
	}

	public EspecieAquiculturaTipoAtividade(Integer ideEspecieAquiculturaTipoAtividade) {
		this.ideEspecieAquiculturaTipoAtividade = ideEspecieAquiculturaTipoAtividade;
	}

	public EspecieAquiculturaTipoAtividade(Integer ideEspecieAquiculturaTipoAtividade, Especie especie) {
		this.ideEspecieAquiculturaTipoAtividade = ideEspecieAquiculturaTipoAtividade;
		this.ideEspecie = especie;
	}

	public EspecieAquiculturaTipoAtividade(AquiculturaTipoAtividade ideAquiculturaTipoAtividade) {
		this.ideAquiculturaTipoAtividade = ideAquiculturaTipoAtividade;
	}

	public Integer getIdeEspecieAquiculturaTipoAtividade() {
		return this.ideEspecieAquiculturaTipoAtividade;
	}

	public void setIdeEspecieAquiculturaTipoAtividade(Integer ideEspecieAquiculturaTipoAtividade) {
		this.ideEspecieAquiculturaTipoAtividade = ideEspecieAquiculturaTipoAtividade;
	}

	public AquiculturaTipoAtividade getIdeAquiculturaTipoAtividade() {
		return this.ideAquiculturaTipoAtividade;
	}

	public void setIdeAquiculturaTipoAtividade(AquiculturaTipoAtividade aquiculturaTipoAtividade) {
		this.ideAquiculturaTipoAtividade = aquiculturaTipoAtividade;
	}

	public Especie getIdeEspecie() {
		return this.ideEspecie;
	}

	public void setIdeEspecie(Especie especie) {
		this.ideEspecie = especie;
	}

	public boolean isOutrasEspecies() {
		return this.ideEspecie.isOutros();
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.entity.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		return new Long(this.ideEspecieAquiculturaTipoAtividade);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideAquiculturaTipoAtividade == null) ? 0
						: ideAquiculturaTipoAtividade.hashCode());
		result = prime * result
				+ ((ideEspecie == null) ? 0 : ideEspecie.hashCode());
		result = prime
				* result
				+ ((ideEspecieAquiculturaTipoAtividade == null) ? 0
						: ideEspecieAquiculturaTipoAtividade.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EspecieAquiculturaTipoAtividade other = (EspecieAquiculturaTipoAtividade) obj;
		if (ideAquiculturaTipoAtividade == null) {
			if (other.ideAquiculturaTipoAtividade != null) {
				return false;
			}
		} else if (!ideAquiculturaTipoAtividade
				.equals(other.ideAquiculturaTipoAtividade)) {
			return false;
		}
		if (ideEspecie == null) {
			if (other.ideEspecie != null) {
				return false;
			}
		} else if (!ideEspecie.equals(other.ideEspecie)) {
			return false;
		}
		if (ideEspecieAquiculturaTipoAtividade == null) {
			if (other.ideEspecieAquiculturaTipoAtividade != null) {
				return false;
			}
		} else if (!ideEspecieAquiculturaTipoAtividade
				.equals(other.ideEspecieAquiculturaTipoAtividade)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(EspecieAquiculturaTipoAtividade o) {
		return getIdeEspecieAquiculturaTipoAtividade().compareTo(o.getIdeEspecieAquiculturaTipoAtividade());
	}

	public Collection<FceAquiculturaEspecie> getFceAquiculturaEspecieCollection() {
		return fceAquiculturaEspecieCollection;
	}

	public void setFceAquiculturaEspecieCollection(
			Collection<FceAquiculturaEspecie> fceAquiculturaEspecieCollection) {
		this.fceAquiculturaEspecieCollection = fceAquiculturaEspecieCollection;
	}

}