package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_defensivo_agricola database table.
 * 
 */
@Entity
@Table(name="tipo_defensivo_agricola")
public class TipoDefensivoAgricola implements Serializable,BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_tipo_defensivo_agricola")
	private Integer ideTipoDefensivoAgricola;

	@Column(name="dsc_tipo_defensivo_agricola")
	private String dscTipoDefensivoAgricola;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

    public TipoDefensivoAgricola() {
    }

	public Integer getIdeTipoDefensivoAgricola() {
		return this.ideTipoDefensivoAgricola;
	}

	public void setIdeTipoDefensivoAgricola(Integer ideTipoDefensivoAgricola) {
		this.ideTipoDefensivoAgricola = ideTipoDefensivoAgricola;
	}

	public String getDscTipoDefensivoAgricola() {
		return this.dscTipoDefensivoAgricola;
	}

	public void setDscTipoDefensivoAgricola(String dscTipoDefensivoAgricola) {
		this.dscTipoDefensivoAgricola = dscTipoDefensivoAgricola;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoDefensivoAgricola);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoDefensivoAgricola == null) ? 0
						: ideTipoDefensivoAgricola.hashCode());
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
		TipoDefensivoAgricola other = (TipoDefensivoAgricola) obj;
		if (ideTipoDefensivoAgricola == null) {
			if (other.ideTipoDefensivoAgricola != null)
				return false;
		} else if (!ideTipoDefensivoAgricola
				.equals(other.ideTipoDefensivoAgricola))
			return false;
		return true;
	}
}