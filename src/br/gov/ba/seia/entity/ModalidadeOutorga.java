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
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * 
 * @author Lucas Reis
 * 
 **/
@Entity
@Table(name = "modalidade_outorga")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ModalidadeOutorga.findAll", query = "SELECT m FROM ModalidadeOutorga m"),
		@NamedQuery(name = "ModalidadeOutorga.findByIdeModalidadeOutorga", query = "SELECT m FROM ModalidadeOutorga m WHERE m.ideModalidadeOutorga = :ideModalidadeOutorga"),
		@NamedQuery(name = "ModalidadeOutorga.findByNomModalidadeOutorga", query = "SELECT m FROM ModalidadeOutorga m WHERE m.nomModalidadeOutorga = :nomModalidadeOutorga") })
public class ModalidadeOutorga implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "modalidade_outorga_ide_modalidade_outorga_generator")
	@SequenceGenerator(name = "modalidade_outorga_ide_modalidade_outorga_generator", sequenceName = "modalidade_outorga_ide_modalidade_outorga_seq", allocationSize = 1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_modalidade_outorga", nullable = false)
	private Integer ideModalidadeOutorga;

	@Basic(optional = false)
	@NotNull
	@Column(name = "nom_modalidade_outorga", nullable = false, length = 50)
	private String nomModalidadeOutorga;

	public ModalidadeOutorga() {
	}

	public ModalidadeOutorga(Integer ideModalidadeOutorga) {
		this.ideModalidadeOutorga = ideModalidadeOutorga;
	}

	public ModalidadeOutorga(Integer ideModalidadeOutorga, String nomModalidadeOutorga) {
		this.ideModalidadeOutorga = ideModalidadeOutorga;
		this.nomModalidadeOutorga = nomModalidadeOutorga;
	}

	public Integer getIdeModalidadeOutorga() {
		return ideModalidadeOutorga;
	}

	public void setIdeModalidadeOutorga(Integer ideModalidadeOutorga) {
		this.ideModalidadeOutorga = ideModalidadeOutorga;
	}

	public String getNomModalidadeOutorga() {
		return nomModalidadeOutorga;
	}

	public void setNomModalidadeOutorga(String nomModalidadeOutorga) {
		this.nomModalidadeOutorga = nomModalidadeOutorga;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideModalidadeOutorga);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideModalidadeOutorga == null) ? 0 : ideModalidadeOutorga.hashCode());
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
		ModalidadeOutorga other = (ModalidadeOutorga) obj;
		if (ideModalidadeOutorga == null) {
			if (other.ideModalidadeOutorga != null)
				return false;
		} else if (!ideModalidadeOutorga.equals(other.ideModalidadeOutorga))
			return false;
		return true;
	}
	
	
	
	
}
