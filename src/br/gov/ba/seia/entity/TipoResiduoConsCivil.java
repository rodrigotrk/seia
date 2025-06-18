package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_residuo_cons_civil database table.
 * 
 */
@Entity
@Table(name="tipo_residuo_cons_civil")
public class TipoResiduoConsCivil implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ide_tipo_residuo_cons_civil")
	private Integer ideTipoResiduoConsCivil;

	@Column(name="dsc_tipo_residuo_cons_civil")
	private String dscTipoResiduoConsCivil;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public TipoResiduoConsCivil() {
	}

	public Integer getIdeTipoResiduoConsCivil() {
		return this.ideTipoResiduoConsCivil;
	}

	public void setIdeTipoResiduoConsCivil(Integer ideTipoResiduoConsCivil) {
		this.ideTipoResiduoConsCivil = ideTipoResiduoConsCivil;
	}

	public String getDscTipoResiduoConsCivil() {
		return this.dscTipoResiduoConsCivil;
	}

	public void setDscTipoResiduoConsCivil(String dscTipoResiduoConsCivil) {
		this.dscTipoResiduoConsCivil = dscTipoResiduoConsCivil;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoResiduoConsCivil == null) ? 0
						: ideTipoResiduoConsCivil.hashCode());
		return result;
	}

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
		TipoResiduoConsCivil other = (TipoResiduoConsCivil) obj;
		if (ideTipoResiduoConsCivil == null) {
			if (other.ideTipoResiduoConsCivil != null) {
				return false;
			}
		} else if (!ideTipoResiduoConsCivil
				.equals(other.ideTipoResiduoConsCivil)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoResiduoConsCivil);
	}

}