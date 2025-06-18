package br.gov.ba.seia.entity;

import java.io.Serializable;

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

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_energia")
@NamedQueries({@NamedQuery(name = "TipoEnergia.findAll", query = "Select te FROM TipoEnergia te")})
public class TipoEnergia implements Serializable, BaseEntity{

	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "ide_tipo_energia")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ide_tipo_energia_seq")
	@SequenceGenerator(name = "ide_tipo_energia_seq", sequenceName = "ide_tipo_energia_seq", allocationSize = 1)
	private Integer ideTipoEnergia;
	
	@NotNull
	@Size(max = 35)
	@Column(name = "dsc_tipo_energia")
	private String dscTipoEnergia;
	
	@NotNull
	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	public TipoEnergia() {
	}

	public Integer getIdeTipoEnergia() {
		return ideTipoEnergia;
	}

	public void setIdeTipoEnergia(Integer ideTipoEnergia) {
		this.ideTipoEnergia = ideTipoEnergia;
	}

	public String getDscTipoEnergia() {
		return dscTipoEnergia;
	}

	public void setDscTipoEnergia(String dscTipoEnergia) {
		this.dscTipoEnergia = dscTipoEnergia;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoEnergia == null) ? 0 : ideTipoEnergia.hashCode());
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
		TipoEnergia other = (TipoEnergia) obj;
		if (ideTipoEnergia == null) {
			if (other.ideTipoEnergia != null)
				return false;
		} else if (!ideTipoEnergia.equals(other.ideTipoEnergia))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return Long.valueOf(this.ideTipoEnergia);
	}
	
	
}
