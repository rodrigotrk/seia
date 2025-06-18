package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_residuo_gerado database table.
 * 
 */
@Entity
@Table(name="tipo_residuo_gerado")
public class TipoResiduoGerado implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ide_tipo_residuo_gerado")
	private Integer ideTipoResiduoGerado;

	@Column(name="dsc_tipo_residuo_gerado")
	private String dscTipoResiduoGerado;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Transient
	private boolean checked;
	
	public TipoResiduoGerado() {
	}

	public Integer getIdeTipoResiduoGerado() {
		return this.ideTipoResiduoGerado;
	}

	public void setIdeTipoResiduoGerado(Integer ideTipoResiduoGerado) {
		this.ideTipoResiduoGerado = ideTipoResiduoGerado;
	}

	public String getDscTipoResiduoGerado() {
		return this.dscTipoResiduoGerado;
	}

	public void setDscTipoResiduoGerado(String dscTipoResiduoGerado) {
		this.dscTipoResiduoGerado = dscTipoResiduoGerado;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoResiduoGerado == null) ? 0 : ideTipoResiduoGerado
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
		TipoResiduoGerado other = (TipoResiduoGerado) obj;
		if (ideTipoResiduoGerado == null) {
			if (other.ideTipoResiduoGerado != null) {
				return false;
			}
		} else if (!ideTipoResiduoGerado.equals(other.ideTipoResiduoGerado)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoResiduoGerado);
	}

}