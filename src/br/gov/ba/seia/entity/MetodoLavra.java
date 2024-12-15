package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.MetodoLavraEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * The persistent class for the metodo_lavra database table.
 * 
 */
@Entity
@Table(name = "metodo_lavra")
public class MetodoLavra implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "METODO_LAVRA_IDEMETODOLAVRA_GENERATOR", sequenceName = "METODO_LAVRA_IDE_METODO_LAVRA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "METODO_LAVRA_IDEMETODOLAVRA_GENERATOR")
	@Column(name = "ide_metodo_lavra")
	private Integer ideMetodoLavra;

	@Column(name = "nom_metodo_lavra")
	private String nomMetodoLavra;

	public MetodoLavra() {
	}

	public MetodoLavra(MetodoLavraEnum ceuAberto) {
		this.ideMetodoLavra = ceuAberto.getId();
	}

	public Integer getIdeMetodoLavra() {
		return this.ideMetodoLavra;
	}

	public void setIdeMetodoLavra(Integer ideMetodoLavra) {
		this.ideMetodoLavra = ideMetodoLavra;
	}

	public String getNomMetodoLavra() {
		return this.nomMetodoLavra;
	}

	public void setNomMetodoLavra(String nomMetodoLavra) {
		this.nomMetodoLavra = nomMetodoLavra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideMetodoLavra == null) ? 0 : ideMetodoLavra.hashCode());
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
		MetodoLavra other = (MetodoLavra) obj;
		if(ideMetodoLavra == null){
			if(other.ideMetodoLavra != null)
				return false;
		}
		else if(!ideMetodoLavra.equals(other.ideMetodoLavra))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideMetodoLavra);
	}

}