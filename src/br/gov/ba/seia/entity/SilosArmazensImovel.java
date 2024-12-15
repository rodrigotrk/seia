package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the silos_armazens_imovel_rural database table.
 * 
 */
@Entity
@Table(name="silos_armazens_imovel")
@NamedQueries({
	@NamedQuery(name = "SilosArmazensImovel.removerByIdeSilos", query ="DELETE FROM SilosArmazensImovel s WHERE s.silosArmazen = :ideSilosArmazen") })
public class SilosArmazensImovel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SILOS_ARMAZENS_IMOVEL_IDESILOSARMAZENSIMOVEL_GENERATOR", sequenceName="SILOS_ARMAZENS_IMOVEL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SILOS_ARMAZENS_IMOVEL_IDESILOSARMAZENSIMOVEL_GENERATOR")
	@Column(name="ide_silos_armazens_imovel")
	private Integer ideSilosArmazensImovel;

	@ManyToOne
	@JoinColumn(name = "ide_imovel")
	private Imovel ideImovel;

	@Column(name="ind_num_car")
	private Boolean indNumCar;

	//bi-directional many-to-one association to SilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_silos_armazens")
	private SilosArmazen silosArmazen;
	
	@Transient
	private Boolean indExcluirImovel;

	public SilosArmazensImovel() {
	}
	
	public SilosArmazensImovel(SilosArmazen silosArmazen){
		this.silosArmazen = silosArmazen;
	}

	public Integer getIdeSilosArmazensImovel() {
		return this.ideSilosArmazensImovel;
	}

	public void setIdeSilosArmazensImovel(Integer ideSilosArmazensImovel) {
		this.ideSilosArmazensImovel = ideSilosArmazensImovel;
	}

	public Imovel getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(Imovel ideImovel) {
		this.ideImovel = ideImovel;
	}

	public Boolean getIndNumCar() {
		return this.indNumCar;
	}

	public void setIndNumCar(Boolean indNumCar) {
		this.indNumCar = indNumCar;
	}

	public SilosArmazen getSilosArmazen() {
		return this.silosArmazen;
	}

	public void setSilosArmazen(SilosArmazen silosArmazen) {
		this.silosArmazen = silosArmazen;
	}

	public Boolean getIndExcluirImovel() {
		return indExcluirImovel;
	}

	public void setIndExcluirImovel(Boolean indExcluirImovel) {
		this.indExcluirImovel = indExcluirImovel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSilosArmazensImovel == null) ? 0
						: ideSilosArmazensImovel.hashCode());
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
		SilosArmazensImovel other = (SilosArmazensImovel) obj;
		if (ideSilosArmazensImovel == null) {
			if (other.ideSilosArmazensImovel != null)
				return false;
		} else if (!ideSilosArmazensImovel
				.equals(other.ideSilosArmazensImovel))
			return false;
		
		return true;
	}

	
}