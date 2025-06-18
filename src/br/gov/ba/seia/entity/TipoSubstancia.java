package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the tipo_substancia database table.
 * 
 */
@Entity
@Table(name="tipo_substancia")
public class TipoSubstancia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ide_tipo_substancia")
	private Integer ideTipoSubstancia;

	@Column(name="dsc_substancia")
	private String dscSubstancia;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public TipoSubstancia() {
	}

	public Integer getIdeTipoSubstancia() {
		return this.ideTipoSubstancia;
	}

	public void setIdeTipoSubstancia(Integer ideTipoSubstancia) {
		this.ideTipoSubstancia = ideTipoSubstancia;
	}

	public String getDscSubstancia() {
		return this.dscSubstancia;
	}

	public void setDscSubstancia(String dscSubstancia) {
		this.dscSubstancia = dscSubstancia;
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
				+ ((ideTipoSubstancia == null) ? 0 : ideTipoSubstancia
						.hashCode());
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
		TipoSubstancia other = (TipoSubstancia) obj;
		if (ideTipoSubstancia == null) {
			if (other.ideTipoSubstancia != null) {
				return false;
			}
		} else if (!ideTipoSubstancia.equals(other.ideTipoSubstancia)) {
			return false;
		}
		return true;
	}

}