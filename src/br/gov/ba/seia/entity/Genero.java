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


/**
 * The persistent class for the genero database table.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 05/11/2014
 */
@Entity
@NamedQuery(name="Genero.findAll", query="SELECT g FROM Genero g")
public class Genero implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ide_genero")
	private Integer ideGenero;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_genero")
	private String nomGenero;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_familia")
	private Familia ideFamilia;

	public Genero() {
	}

	public Integer getIdeGenero() {
		return this.ideGenero;
	}

	public void setIdeGenero(Integer ideGenero) {
		this.ideGenero = ideGenero;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomGenero() {
		return this.nomGenero;
	}

	public void setNomGenero(String nomGenero) {
		this.nomGenero = nomGenero;
	}

	public Familia getIdeFamilia() {
		return this.ideFamilia;
	}

	public void setIdeFamilia(Familia familia) {
		this.ideFamilia = familia;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideGenero == null) ? 0 : ideGenero.hashCode());
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
		Genero other = (Genero) obj;
		if (ideGenero == null) {
			if (other.ideGenero != null) {
				return false;
			}
		} else if (!ideGenero.equals(other.ideGenero)) {
			return false;
		}
		return true;
	}

}