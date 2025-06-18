package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_veiculo database table.
 * 
 */
@Entity
@Table(name="tipo_veiculo")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "TipoVeiculo.findAll", query = "SELECT t FROM TipoVeiculo t"),
	@NamedQuery(name = "TipoVeiculo.findByResiduo", query = "SELECT t FROM TipoVeiculo t WHERE t.ideTipoVeiculo IN (3,4)"),
})
public class TipoVeiculo implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_veiculo_ide_tipo_veiculo_generator")
	@SequenceGenerator(name = "tipo_veiculo_ide_tipo_veiculo_generator", sequenceName = "tipo_veiculo_ide_tipo_veiculo_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_tipo_veiculo",nullable = false)
	private Integer ideTipoVeiculo;

	@Basic(optional = false)
	@Size(min = 1, max = 20)
	@Column(name="dsc_tipo_veiculo",nullable = false)
	private String dscTipoVeiculo;

	@Column(name="ind_ativo",nullable = false)
	private Boolean indAtivo;

    public TipoVeiculo() {
    }

	public Integer getIdeTipoVeiculo() {
		return this.ideTipoVeiculo;
	}

	public void setIdeTipoVeiculo(Integer ideTipoVeiculo) {
		this.ideTipoVeiculo = ideTipoVeiculo;
	}

	public String getDscTipoVeiculo() {
		return this.dscTipoVeiculo;
	}

	public void setDscTipoVeiculo(String dscTipoVeiculo) {
		this.dscTipoVeiculo = dscTipoVeiculo;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return new Long(ideTipoVeiculo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoVeiculo == null) ? 0 : ideTipoVeiculo.hashCode());
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
		TipoVeiculo other = (TipoVeiculo) obj;
		if (ideTipoVeiculo == null) {
			if (other.ideTipoVeiculo != null)
				return false;
		} else if (!ideTipoVeiculo.equals(other.ideTipoVeiculo))
			return false;
		return true;
	}
	
	
	
}