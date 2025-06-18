package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the familia database table.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 05/11/2014
 */
@Entity
@NamedQuery(name="Familia.findAll", query="SELECT f FROM Familia f")
public class Familia implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ide_familia")
	private Integer ideFamilia;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_familia")
	private String nomFamilia;

	public Familia() {
	}

	public Integer getIdeFamilia() {
		return this.ideFamilia;
	}

	public void setIdeFamilia(Integer ideFamilia) {
		this.ideFamilia = ideFamilia;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomFamilia() {
		return this.nomFamilia;
	}

	public void setNomFamilia(String nomFamilia) {
		this.nomFamilia = nomFamilia;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFamilia == null) ? 0 : ideFamilia.hashCode());
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
		Familia other = (Familia) obj;
		if (ideFamilia == null) {
			if (other.ideFamilia != null) {
				return false;
			}
		} else if (!ideFamilia.equals(other.ideFamilia)) {
			return false;
		}
		return true;
	}
}