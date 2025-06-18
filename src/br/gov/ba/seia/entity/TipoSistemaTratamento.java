package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;

/**
 * The persistent class for the tipo_sistema_tratamento database table.
 * 
 */
@Entity
@Table(name = "tipo_sistema_tratamento")
public class TipoSistemaTratamento implements Serializable, BaseEntity, Comparable<TipoSistemaTratamento> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ide_tipo_sistema_tratamento")
	private Integer ideTipoSistemaTratamento;

	@Column(name = "dsc_tipo_sistema_tratamento")
	private String dscTipoSistemaTratamento;

	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	public TipoSistemaTratamento() {
	}

	public TipoSistemaTratamento(String string) {
		this.dscTipoSistemaTratamento = string;
	}
	
	public TipoSistemaTratamento(Integer ideTipoSistemaTratamento, String dscTipoSistemaTratamento) {
		this.ideTipoSistemaTratamento = ideTipoSistemaTratamento;
		this.dscTipoSistemaTratamento = dscTipoSistemaTratamento;
	}

	public Integer getIdeTipoSistemaTratamento() {
		return this.ideTipoSistemaTratamento;
	}

	public void setIdeTipoSistemaTratamento(Integer ideTipoSistemaTratamento) {
		this.ideTipoSistemaTratamento = ideTipoSistemaTratamento;
	}

	public String getDscTipoSistemaTratamento() {
		return this.dscTipoSistemaTratamento;
	}

	public void setDscTipoSistemaTratamento(String dscTipoSistemaTratamento) {
		this.dscTipoSistemaTratamento = dscTipoSistemaTratamento;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public boolean isOutros() {
		if(dscTipoSistemaTratamento.compareTo("Outros") == 0){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dscTipoSistemaTratamento == null) ? 0 : dscTipoSistemaTratamento.hashCode());
		result = prime * result + ((ideTipoSistemaTratamento == null) ? 0 : ideTipoSistemaTratamento.hashCode());
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
		TipoSistemaTratamento other = (TipoSistemaTratamento) obj;
		if(dscTipoSistemaTratamento == null){
			if(other.dscTipoSistemaTratamento != null)
				return false;
		}
		else if(!dscTipoSistemaTratamento.equals(other.dscTipoSistemaTratamento))
			return false;
		if(ideTipoSistemaTratamento == null){
			if(other.ideTipoSistemaTratamento != null)
				return false;
		}
		else if(!ideTipoSistemaTratamento.equals(other.ideTipoSistemaTratamento))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoSistemaTratamento);
	}

	@Override
	public int compareTo(TipoSistemaTratamento o) {
		if(Util.isNullOuVazio(dscTipoSistemaTratamento)){
			return 0;
		}
		return this.dscTipoSistemaTratamento.compareTo(o.dscTipoSistemaTratamento);
	}

}