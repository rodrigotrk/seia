package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;


/**
 * The persistent class for the especie database table.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 05/11/2014
 */
@Entity
@NamedQuery(name="Especie.findAll", query="SELECT e FROM Especie e")
public class Especie implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ESPECIE_IDEESPECIE_GENERATOR", sequenceName="ESPECIE_IDE_ESPECIE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ESPECIE_IDEESPECIE_GENERATOR")
	@Column(name="ide_especie")
	private Integer ideEspecie;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_especie")
	private String nomEspecie;

	@Column(name="nom_popular_especie")
	private String nomPopularEspecie;

	//bi-directional many-to-one association to Genero
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_genero")
	private Genero ideGenero;
	
	public Especie() {
	}

	public Especie(Integer ideEspecie) {
		this.ideEspecie = ideEspecie;
	}

	public Integer getIdeEspecie() {
		return this.ideEspecie;
	}

	public void setIdeEspecie(Integer ideEspecie) {
		this.ideEspecie = ideEspecie;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomEspecie() {
		return this.nomEspecie;
	}

	public void setNomEspecie(String nomEspecie) {
		this.nomEspecie = nomEspecie;
	}

	public Genero getIdeGenero() {
		return this.ideGenero;
	}

	public void setIdeGenero(Genero genero) {
		this.ideGenero = genero;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideEspecie == null) ? 0 : ideEspecie.hashCode());
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
		Especie other = (Especie) obj;
		if (ideEspecie == null) {
			if (other.ideEspecie != null) {
				return false;
			}
		} else if (!ideEspecie.equals(other.ideEspecie)) {
			return false;
		}
		return true;
	}

	public String getNomPopularEspecie() {
		return nomPopularEspecie;
	}

	public void setNomPopularEspecie(String nomPopularEspecie) {
		this.nomPopularEspecie = nomPopularEspecie;
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.entity.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		return new Long(this.ideEspecie);
	}

	public boolean isOutros() {
		return this.nomEspecie.compareTo("Outros") == 0;
	}

	public String getNomeToLabel() {
		if(isExisteNomPopularEspecie()) {
			return this.nomEspecie + " - " + this.nomPopularEspecie;
		} else {
			return this.nomEspecie;
		}
	}

	public boolean isExisteNomPopularEspecie(){
		return !Util.isNullOuVazio(nomPopularEspecie);
	}
}