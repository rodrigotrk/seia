package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_controle_ambiental database table.
 * 
 */
@Entity
@Table(name="tipo_controle_ambiental")
public class TipoControleAmbiental implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_controle_ambiental_ide_tipo_controle_ambiental_generator")
	@SequenceGenerator(name = "tipo_controle_ambiental_ide_tipo_controle_ambiental_generator", sequenceName = "tipo_controle_ambiental_ide_tipo_controle_ambiental_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_tipo_controle_ambiental")
	private Integer ideTipoControleAmbiental;
	
	@Size(min = 1, max = 50)
	@Column(name="dsc_controle_ambiental")
	private String dscControleAmbiental;

	@Column(name="ind_ativo",nullable = false)
	private Boolean indAtivo;


    public TipoControleAmbiental() {
    }
    
	public TipoControleAmbiental(Integer ideTipoControleAmbiental) {
		this.ideTipoControleAmbiental = ideTipoControleAmbiental;
	}

	public Integer getIdeTipoControleAmbiental() {
		return this.ideTipoControleAmbiental;
	}

	public void setIdeTipoControleAmbiental(Integer ideTipoControleAmbiental) {
		this.ideTipoControleAmbiental = ideTipoControleAmbiental;
	}

	public String getDscControleAmbiental() {
		return this.dscControleAmbiental;
	}

	public void setDscControleAmbiental(String dscControleAmbiental) {
		this.dscControleAmbiental = dscControleAmbiental;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	@Override
	public Long getId() {
		return new Long(ideTipoControleAmbiental);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoControleAmbiental == null) ? 0
						: ideTipoControleAmbiental.hashCode());
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
		TipoControleAmbiental other = (TipoControleAmbiental) obj;
		if (ideTipoControleAmbiental == null) {
			if (other.ideTipoControleAmbiental != null)
				return false;
		} else if (!ideTipoControleAmbiental
				.equals(other.ideTipoControleAmbiental))
			return false;
		return true;
	}	
}