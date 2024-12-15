package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: Manejo
 *
 */
@Entity
@Table(name = "manejo", uniqueConstraints = {@UniqueConstraint(columnNames = {"ide_manejo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Manejo.findAll", query = "SELECT t FROM Manejo t"),
    @NamedQuery(name = "Manejo.findByIdeManejo", query = "SELECT m FROM Manejo m WHERE m.ideManejo = :ideManejo"),
    @NamedQuery(name = "Manejo.findByDscManejo", query = "SELECT m FROM Manejo m WHERE m.dscManejo = :dscManejo")
})
public class Manejo implements Serializable, Comparable<Manejo>{
	
	private static final long serialVersionUID = 1L;
	  
	@Id
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_manejo", nullable = false)
	private Integer ideManejo;
	
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_manejo", nullable = false, length = 100)
	private String dscManejo;
	
    @OneToOne(mappedBy = "ideManejo")
    private AreaProdutivaTipologiaAtividadeAnimal ideAreaProdutivaTipologiaAtividadeAnimal;

    public Manejo() {
	}	
	
	public Manejo(Integer ideManejo) {
		this.ideManejo = ideManejo;
	}


	public Manejo(Integer ideManejo, String dscManejo) {
		this.ideManejo = ideManejo;
		this.dscManejo = dscManejo;
	}


	public Integer getIdeManejo() {
		return this.ideManejo;
	}

	public void setIdeManejo(Integer ideManejo) {
		this.ideManejo = ideManejo;
	}   
	public String getDscManejo() {
		return this.dscManejo;
	}

	public void setDscManejo(String dscManejo) {
		this.dscManejo = dscManejo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideManejo == null) ? 0 : ideManejo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Manejo))
			return false;
		Manejo other = (Manejo) obj;
		if (ideManejo == null) {
			if (other.ideManejo != null)
				return false;
		} else if (!ideManejo.equals(other.ideManejo))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "" + ideManejo;
	}

	public AreaProdutivaTipologiaAtividadeAnimal getIdeAreaProdutivaTipologiaAtividadeAnimal() {
		return ideAreaProdutivaTipologiaAtividadeAnimal;
	}

	public void setIdeAreaProdutivaTipologiaAtividadeAnimal(
			AreaProdutivaTipologiaAtividadeAnimal ideAreaProdutivaTipologiaAtividadeAnimal) {
		this.ideAreaProdutivaTipologiaAtividadeAnimal = ideAreaProdutivaTipologiaAtividadeAnimal;
	}

	@Override
	public int compareTo(Manejo o) {
		if (this.getIdeManejo() > o.getIdeManejo()) {
			return 1;
		} else{
			if (this.getIdeManejo() < o.getIdeManejo()) 
			{
				return -1;
			}
		}
		return 0;
	}
   
	
}
