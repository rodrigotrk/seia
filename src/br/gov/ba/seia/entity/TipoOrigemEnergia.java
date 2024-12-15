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

@Entity
@Table(name="tipo_origem_energia")
public class TipoOrigemEnergia implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ide_tipo_origem_energia")
	private Integer ideTipoOrigemEnergia;

	@Column(name="dsc_tipo_origem_energia")
	private String dscTipoOrigemEnergia;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Transient
	private boolean checked;
	
	public TipoOrigemEnergia() {
	}

	public TipoOrigemEnergia(String dsc) {
		this.dscTipoOrigemEnergia = dsc;
	}

	public TipoOrigemEnergia(Integer ide, String dsc) {
		this.ideTipoOrigemEnergia = ide;
		this.dscTipoOrigemEnergia = dsc;
	}

	public Integer getIdeTipoOrigemEnergia() {
		return this.ideTipoOrigemEnergia;
	}

	public void setIdeTipoOrigemEnergia(Integer ideTipoOrigemEnergia) {
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia;
	}

	public String getDscTipoOrigemEnergia() {
		return this.dscTipoOrigemEnergia;
	}

	public void setDscTipoOrigemEnergia(String dscTipoOrigemEnergia) {
		this.dscTipoOrigemEnergia = dscTipoOrigemEnergia;
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

	public void setChecked(boolean cheked) {
		this.checked = cheked;
	}

	public boolean isOutros(){
		if(dscTipoOrigemEnergia.compareTo("Outros") == 0){
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dscTipoOrigemEnergia == null) ? 0 : dscTipoOrigemEnergia.hashCode());
		result = prime * result + ((ideTipoOrigemEnergia == null) ? 0 : ideTipoOrigemEnergia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		TipoOrigemEnergia other = (TipoOrigemEnergia) obj;
		if(dscTipoOrigemEnergia == null){
			if(other.dscTipoOrigemEnergia != null)
				return false;
		}
		else if(!dscTipoOrigemEnergia.equals(other.dscTipoOrigemEnergia))
			return false;
		if(ideTipoOrigemEnergia == null){
			if(other.ideTipoOrigemEnergia != null)
				return false;
		}
		else if(!ideTipoOrigemEnergia.equals(other.ideTipoOrigemEnergia))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoOrigemEnergia);
	}
}