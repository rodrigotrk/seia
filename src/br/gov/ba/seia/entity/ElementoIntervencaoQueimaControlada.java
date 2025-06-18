package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name="elemento_intervencao_queima_controlada")
public class ElementoIntervencaoQueimaControlada implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elemento_intervencao_qc_ide_elemento_intervencao_qc_seq")
	@SequenceGenerator(name = "elemento_intervencao_qc_ide_elemento_intervencao_qc_seq", sequenceName = "elemento_intervencao_qc_ide_elemento_intervencao_qc_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_elemento_intervencao_queima_controlada")
	private Integer ideElementoIntervencaoQueimaControlada;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_criacao")
	private Date dtcCriacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_exclusao")
	private Date dtcExclusao;

	@Column(name="ind_excluido")
	private Boolean indExcluido;

	@Column(name="nom_elemento_intervencao", length = 100)
	private String nomElementoIntervencao;

	@OneToMany(mappedBy="ideElementoIntervencaoQueimaControlada", fetch = FetchType.LAZY)
	private List<DeclaracaoQueimaControladaElementoIntervencao> declaracaoQueimaControladaElementoIntervencaoCollection;

	public ElementoIntervencaoQueimaControlada() {
	}

	public ElementoIntervencaoQueimaControlada(String nomElementoIntervencao) {
		this.nomElementoIntervencao = nomElementoIntervencao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideElementoIntervencaoQueimaControlada == null) ? 0 : ideElementoIntervencaoQueimaControlada.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ElementoIntervencaoQueimaControlada other = (ElementoIntervencaoQueimaControlada) obj;
		if (ideElementoIntervencaoQueimaControlada == null) {
			if (other.ideElementoIntervencaoQueimaControlada != null) return false;
		} else if (!ideElementoIntervencaoQueimaControlada.equals(other.ideElementoIntervencaoQueimaControlada)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "ElementoIntervencaoQueimaControlada [ideElementoIntervencaoQueimaControlada=" + ideElementoIntervencaoQueimaControlada + "]";
	}

	@Override
	public Long getId() {
		return ideElementoIntervencaoQueimaControlada.longValue();
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeElementoIntervencaoQueimaControlada() {
		return ideElementoIntervencaoQueimaControlada;
	}

	public void setIdeElementoIntervencaoQueimaControlada(Integer ideElementoIntervencaoQueimaControlada) {
		this.ideElementoIntervencaoQueimaControlada = ideElementoIntervencaoQueimaControlada;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getNomElementoIntervencao() {
		return nomElementoIntervencao;
	}

	public void setNomElementoIntervencao(String nomElementoIntervencao) {
		this.nomElementoIntervencao = nomElementoIntervencao;
	}

	public List<DeclaracaoQueimaControladaElementoIntervencao> getDeclaracaoQueimaControladaElementoIntervencaoCollection() {
		return declaracaoQueimaControladaElementoIntervencaoCollection;
	}

	public void setDeclaracaoQueimaControladaElementoIntervencaoCollection(
			List<DeclaracaoQueimaControladaElementoIntervencao> declaracaoQueimaControladaElementoIntervencaoCollection) {
		this.declaracaoQueimaControladaElementoIntervencaoCollection = declaracaoQueimaControladaElementoIntervencaoCollection;
	}
}