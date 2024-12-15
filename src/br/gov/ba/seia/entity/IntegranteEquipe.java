package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="integrante_equipe")
public class IntegranteEquipe implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="integrante_equipe_seq")    
	@SequenceGenerator(name="integrante_equipe_seq", sequenceName="integrante_equipe_seq", allocationSize=1)
	@Column(name="ide_integrante_equipe", unique=true, nullable=false)
	private Integer ideIntegranteEquipe;

	@Column(name="ind_lider_equipe", nullable=false)
	private Boolean indLiderEquipe;

	@JoinColumn(name = "ide_area", referencedColumnName = "ide_area", nullable = false)
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private Area ideArea;
	
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = false)
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private Funcionario idePessoaFisica;

	@JoinColumn(name = "ide_equipe", referencedColumnName = "ide_equipe", nullable = false)
	@ManyToOne(optional=false,fetch=FetchType.EAGER)
	private Equipe ideEquipe;

	@OneToMany(mappedBy="ideIntegranteEquipe", fetch=FetchType.LAZY)
	private Collection<ProcessoAtoIntegranteEquipe> processoAtoIntegranteEquipeCollection;
	
	@Transient
	private Collection<String> listaAto;

	public IntegranteEquipe () {
		
	}
	
	public Integer getIdeIntegranteEquipe() {
		return ideIntegranteEquipe;
	}

	public void setIdeIntegranteEquipe(Integer ideIntegranteEquipe) {
		this.ideIntegranteEquipe = ideIntegranteEquipe;
	}

	public Boolean getIndLiderEquipe() {
		return indLiderEquipe;
	}

	public void setIndLiderEquipe(Boolean indLiderEquipe) {
		this.indLiderEquipe = indLiderEquipe;
	}

	public Area getIdeArea() {
		return ideArea;
	}

	public void setIdeArea(Area ideArea) {
		this.ideArea = ideArea;
	}

	public Funcionario getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Funcionario idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public Equipe getIdeEquipe() {
		return ideEquipe;
	}

	public void setIdeEquipe(Equipe ideEquipe) {
		this.ideEquipe = ideEquipe;
	}

	public Collection<ProcessoAtoIntegranteEquipe> getProcessoAtoIntegranteEquipeCollection() {
		return processoAtoIntegranteEquipeCollection;
	}

	public void setProcessoAtoIntegranteEquipeCollection(
			Collection<ProcessoAtoIntegranteEquipe> processoAtoIntegranteEquipeCollection) {
		this.processoAtoIntegranteEquipeCollection = processoAtoIntegranteEquipeCollection;
	}

	public Collection<String> getListaAto() {
		return listaAto;
	}

	public void setListaAto(Collection<String> listaAto) {
		this.listaAto = listaAto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideIntegranteEquipe == null) ? 0 : ideIntegranteEquipe.hashCode());
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
		IntegranteEquipe other = (IntegranteEquipe) obj;
		if (ideIntegranteEquipe == null) {
			if (other.ideIntegranteEquipe != null)
				return false;
		} else if (!ideIntegranteEquipe.equals(other.ideIntegranteEquipe))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideIntegranteEquipe);
	}
}