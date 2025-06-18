package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="dado_origem")
public class DadoOrigem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="dado_origem_seq")    
	@SequenceGenerator(name="dado_origem_seq", sequenceName="dado_origem_seq", allocationSize=1)
	@Column(name="ide_dado_origem", unique=true, nullable=false)
	private Integer ideDadoOrigem;

	@Column(name="nom_dado_origem", length=20)
	private String nomDadoOrigem;
	
	@OneToMany(mappedBy="ideDadoOrigem", fetch=FetchType.LAZY)
	private Collection<Fce> fceCollection;

	public DadoOrigem() {
		
	}
	
	public DadoOrigem(Integer ideDadoOrigem) {
		this.ideDadoOrigem = ideDadoOrigem;
	}

	public Integer getIdeDadoOrigem() {
		return this.ideDadoOrigem;
	}

	public void setIdeDadoOrigem(Integer ideDadoOrigem) {
		this.ideDadoOrigem = ideDadoOrigem;
	}

	public String getNomDadoOrigem() {
		return this.nomDadoOrigem;
	}

	public void setNomDadoOrigem(String nomDadoOrigem) {
		this.nomDadoOrigem = nomDadoOrigem;
	}

	public Collection<Fce> getFceCollection() {
		return fceCollection;
	}
	
	public void setFceCollection(Collection<Fce> fceCollection) {
		this.fceCollection = fceCollection;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideDadoOrigem == null) ? 0 : ideDadoOrigem.hashCode());
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
		DadoOrigem other = (DadoOrigem) obj;
		if (ideDadoOrigem == null) {
			if (other.ideDadoOrigem != null)
				return false;
		} else if (!ideDadoOrigem.equals(other.ideDadoOrigem))
			return false;
		return true;
	}

}