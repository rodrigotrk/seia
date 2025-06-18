package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@Table(name="aquifero")
public class Aquifero implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name="ide_aquifero")
	private Integer ideAquifero;
	
	@Column(name="nom_aquifero")
	private String nomAquifero;
	
	@Column(name="ind_ativo")
	private boolean indAtivo;
	
	public Aquifero(){
		
	}

	public Integer getIdeAquifero() {
		return ideAquifero;
	}


	public void setIdeAquifero(Integer ideAquifero) {
		this.ideAquifero = ideAquifero;
	}


	public String getNomAquifero() {
		return nomAquifero;
	}


	public void setNomAquifero(String nomAquifero) {
		this.nomAquifero = nomAquifero;
	}
	
	public boolean getIndAtivo(){
		return indAtivo;
	}
	
	public void setIndAtivo(boolean indAtivo){
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return new Long(this.ideAquifero);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideAquifero == null) ? 0 : ideAquifero.hashCode());
		result = prime * result + ((nomAquifero == null) ? 0 : nomAquifero.hashCode());
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
		Aquifero other = (Aquifero) obj;
		if (ideAquifero == null) {
			if (other.ideAquifero != null)
				return false;
		} else if (!ideAquifero.equals(other.ideAquifero))
			return false;
		if (nomAquifero == null) {
			if (other.nomAquifero != null)
				return false;
		} else if (!nomAquifero.equals(other.nomAquifero))
			return false;
		return true;
	}
	


}
