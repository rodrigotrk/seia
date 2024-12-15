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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the fce_aquicultura_especie database table.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 05/11/2014
 */
@Entity
@Table(name="fce_aquicultura_especie")
@NamedQueries({
	@NamedQuery(name="FceAquiculturaEspecie.findAll", query="SELECT f FROM FceAquiculturaEspecie f"),
	@NamedQuery(name = "FceAquiculturaEspecie.removeByIdeFceAquicultura", query = "DELETE FROM FceAquiculturaEspecie f WHERE f.ideFceAquicultura.ideFceAquicultura = :ideFceAquicultura")})
public class FceAquiculturaEspecie implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_AQUICULTURA_ESPECIE_IDEFCEAQUICULTURAESPECIE_GENERATOR", sequenceName="FCE_AQUICULTURA_ESPECIE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_AQUICULTURA_ESPECIE_IDEFCEAQUICULTURAESPECIE_GENERATOR")
	@Column(name="ide_fce_aquicultura_especie")
	private Integer ideFceAquiculturaEspecie;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_especie_aquicultura_tipo_atividade", referencedColumnName="ide_especie_aquicultura_tipo_atividade")
	private EspecieAquiculturaTipoAtividade ideEspecieAquiculturaTipoAtividade;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_aquicultura", referencedColumnName="ide_fce_aquicultura")
	private FceAquicultura ideFceAquicultura;

	public FceAquiculturaEspecie() {
	}

	public FceAquiculturaEspecie(FceAquicultura ideFceAquicultura) {
		this.ideFceAquicultura = ideFceAquicultura;
	}

	public FceAquiculturaEspecie(EspecieAquiculturaTipoAtividade ideEspecieAquiculturaTipoAtividade, FceAquicultura ideFceAquicultura) {
		this.ideEspecieAquiculturaTipoAtividade = ideEspecieAquiculturaTipoAtividade;
		this.ideFceAquicultura = ideFceAquicultura;
	}

	public Integer getIdeFceAquiculturaEspecie() {
		return this.ideFceAquiculturaEspecie;
	}

	public void setIdeFceAquiculturaEspecie(Integer ideFceAquiculturaEspecie) {
		this.ideFceAquiculturaEspecie = ideFceAquiculturaEspecie;
	}

	public EspecieAquiculturaTipoAtividade getIdeEspecieAquiculturaTipoAtividade() {
		return this.ideEspecieAquiculturaTipoAtividade;
	}

	public void setIdeEspecieAquiculturaTipoAtividade(EspecieAquiculturaTipoAtividade especieAquiculturaTipoAtividade) {
		this.ideEspecieAquiculturaTipoAtividade = especieAquiculturaTipoAtividade;
	}

	public FceAquicultura getIdeFceAquicultura() {
		return this.ideFceAquicultura;
	}

	public void setIdeFceAquicultura(FceAquicultura fceAquicultura) {
		this.ideFceAquicultura = fceAquicultura;
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
				+ ((ideEspecieAquiculturaTipoAtividade == null) ? 0
						: ideEspecieAquiculturaTipoAtividade.hashCode());
		result = prime
				* result
				+ ((ideFceAquicultura == null) ? 0 : ideFceAquicultura
						.hashCode());
		result = prime
				* result
				+ ((ideFceAquiculturaEspecie == null) ? 0
						: ideFceAquiculturaEspecie.hashCode());
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
		FceAquiculturaEspecie other = (FceAquiculturaEspecie) obj;
		if (ideEspecieAquiculturaTipoAtividade == null) {
			if (other.ideEspecieAquiculturaTipoAtividade != null) {
				return false;
			}
		} else if (!ideEspecieAquiculturaTipoAtividade
				.equals(other.ideEspecieAquiculturaTipoAtividade)) {
			return false;
		}
		if (ideFceAquicultura == null) {
			if (other.ideFceAquicultura != null) {
				return false;
			}
		} else if (!ideFceAquicultura.equals(other.ideFceAquicultura)) {
			return false;
		}
		if (ideFceAquiculturaEspecie == null) {
			if (other.ideFceAquiculturaEspecie != null) {
				return false;
			}
		} else if (!ideFceAquiculturaEspecie
				.equals(other.ideFceAquiculturaEspecie)) {
			return false;
		}
		return true;
	}
	
	@Override
	public FceAquiculturaEspecie clone() throws CloneNotSupportedException{
		return (FceAquiculturaEspecie) super.clone();
	}
}