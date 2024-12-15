package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="equipe")
public class Equipe implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="equipe_seq")    
	@SequenceGenerator(name="equipe_seq", sequenceName="equipe_seq", allocationSize=1)
	@Column(name="ide_equipe", unique=true, nullable=false)
	private Integer ideEquipe;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_exclusao_equipe")
	private Date dtcExclusaoEquipe;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_formacao_equipe")
	private Date dtcFormacaoEquipe;

	@JoinColumn(name = "ide_area", referencedColumnName = "ide_area", nullable = false)
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private Area ideArea;

	@JoinColumn(name = "ide_area_responsavel", referencedColumnName = "ide_area", nullable = false)
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private Area ideAreaResponsavel;

	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Processo ideProcesso;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@OneToMany(mappedBy="ideEquipe", fetch = FetchType.LAZY)
	private Collection<IntegranteEquipe> integranteEquipeCollection;
	
	public Equipe() {
		
	}

	public Integer getIdeEquipe() {
		return ideEquipe;
	}

	public void setIdeEquipe(Integer ideEquipe) {
		this.ideEquipe = ideEquipe;
	}

	public Date getDtcExclusaoEquipe() {
		return dtcExclusaoEquipe;
	}

	public void setDtcExclusaoEquipe(Date dtcExclusaoEquipe) {
		this.dtcExclusaoEquipe = dtcExclusaoEquipe;
	}

	public Date getDtcFormacaoEquipe() {
		return dtcFormacaoEquipe;
	}

	public void setDtcFormacaoEquipe(Date dtcFormacaoEquipe) {
		this.dtcFormacaoEquipe = dtcFormacaoEquipe;
	}

	public Area getIdeArea() {
		return ideArea;
	}

	public void setIdeArea(Area ideArea) {
		this.ideArea = ideArea;
	}

	public Area getIdeAreaResponsavel() {
		return ideAreaResponsavel;
	}

	public void setIdeAreaResponsavel(Area ideAreaResponsavel) {
		this.ideAreaResponsavel = ideAreaResponsavel;
	}

	public Processo getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Collection<IntegranteEquipe> getIntegranteEquipeCollection() {
		return integranteEquipeCollection;
	}

	public void setIntegranteEquipeCollection(
			Collection<IntegranteEquipe> integranteEquipeCollection) {
		this.integranteEquipeCollection = integranteEquipeCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideEquipe == null) ? 0 : ideEquipe.hashCode());
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
		Equipe other = (Equipe) obj;
		if (ideEquipe == null) {
			if (other.ideEquipe != null)
				return false;
		} else if (!ideEquipe.equals(other.ideEquipe))
			return false;
		return true;
	}	

}