package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@Table(name="unidade_geologica_aflorante")
public class UnidadeGeologicaAflorante implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name="ide_unidade_geologica_aflorante")
	private Integer ideUnidadeGeologicaAflorante;
	
	@Column(name="dsc_unidade_geologica_aflorante")
	private String dscUnidadeGeologicaAflorante;
	
	@Column(name="ind_ativo")
	private boolean indAtivo;
	
	public UnidadeGeologicaAflorante(){
		
	}
	
	public Integer getIdeUnidadeGeologicaAflorante() {
		return ideUnidadeGeologicaAflorante;
	}

	public void setIdeUnidadeGeologicaAflorante(Integer ideUnidadeGeologicaAflorante) {
		this.ideUnidadeGeologicaAflorante = ideUnidadeGeologicaAflorante;
	}

	public String getDscUnidadeGeologicaAflorante() {
		return dscUnidadeGeologicaAflorante;
	}

	public void setDscUnidadeGeologicaAflorante(String dscUnidadeGeologicaAflorante) {
		this.dscUnidadeGeologicaAflorante = dscUnidadeGeologicaAflorante;
	}
	
	public boolean getIndAtivo(){
		return indAtivo;
	}
	
	public void setIndAtivo(boolean indAtivo){
		this.indAtivo = indAtivo;
	}
	
	

	public Long getId() {
		return new Long(this.ideUnidadeGeologicaAflorante);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dscUnidadeGeologicaAflorante == null) ? 0 : dscUnidadeGeologicaAflorante.hashCode());
		result = prime * result
				+ ((ideUnidadeGeologicaAflorante == null) ? 0 : ideUnidadeGeologicaAflorante.hashCode());
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
		UnidadeGeologicaAflorante other = (UnidadeGeologicaAflorante) obj;
		if (dscUnidadeGeologicaAflorante == null) {
			if (other.dscUnidadeGeologicaAflorante != null)
				return false;
		} else if (!dscUnidadeGeologicaAflorante.equals(other.dscUnidadeGeologicaAflorante))
			return false;
		if (ideUnidadeGeologicaAflorante == null) {
			if (other.ideUnidadeGeologicaAflorante != null)
				return false;
		} else if (!ideUnidadeGeologicaAflorante.equals(other.ideUnidadeGeologicaAflorante))
			return false;
		return true;
	}

}
