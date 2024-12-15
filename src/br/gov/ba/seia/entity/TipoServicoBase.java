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
 * The persistent class for the tipo_servico_base database table.
 * 
 */
@Entity
@Table(name="tipo_servico_base")
public class TipoServicoBase implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_servico_base_ide_tipo_servico_base_generator")
	@SequenceGenerator(name = "tipo_servico_base_ide_tipo_servico_base_generator", sequenceName = "tipo_servico_base_ide_tipo_servico_base_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_tipo_servico_base", nullable = false)
	private Integer ideTipoServicoBase;
	
	@Basic(optional = true)
	@Size(min = 1, max = 100)
	@Column(name="dsc_tipo_servico_base", nullable = false)
	private String dscTipoServicoBase;

	@Column(name="ind_ativo", nullable = false)
	private Boolean indAtivo;


    public TipoServicoBase() {
    }

	public Integer getIdeTipoServicoBase() {
		return this.ideTipoServicoBase;
	}

	public void setIdeTipoServicoBase(Integer ideTipoServicoBase) {
		this.ideTipoServicoBase = ideTipoServicoBase;
	}

	public String getDscTipoServicoBase() {
		return this.dscTipoServicoBase;
	}

	public void setDscTipoServicoBase(String dscTipoServicoBase) {
		this.dscTipoServicoBase = dscTipoServicoBase;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	

	@Override
	public Long getId() {
		return new Long(ideTipoServicoBase);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoServicoBase == null) ? 0 : ideTipoServicoBase
						.hashCode());
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
		TipoServicoBase other = (TipoServicoBase) obj;
		if (ideTipoServicoBase == null) {
			if (other.ideTipoServicoBase != null)
				return false;
		} else if (!ideTipoServicoBase.equals(other.ideTipoServicoBase))
			return false;
		return true;
	}

}