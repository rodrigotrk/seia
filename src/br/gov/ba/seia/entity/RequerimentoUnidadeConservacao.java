package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
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
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "requerimento_unidade_conservacao")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "RequerimentoUnidadeConservacao.findAll", query = "SELECT r FROM RequerimentoUnidadeConservacao r "),
		@NamedQuery(name = "RequerimentoUnidadeConservacao.findByIdeRequerimentoUnidadeConservacao", query = "SELECT r FROM RequerimentoUnidadeConservacao r WHERE r.ideRequerimentoUnidadeConservacao = :ideRequerimentoUnidadeConservacao"),
		@NamedQuery(name = "RequerimentoUnidadeConservacao.findByIdeRequerimento", query = "SELECT r FROM RequerimentoUnidadeConservacao r WHERE r.ideRequerimento.ideRequerimento = :ideRequerimento"),
		@NamedQuery(name = "RequerimentoUnidadeConservacao.findByIdeUnidadeConservacao", query = "SELECT r FROM RequerimentoUnidadeConservacao r WHERE r.ideUnidadeConservacao.ideUnidadeConservacao = :ideUnidadeConservacao"),
		@NamedQuery(name = "RequerimentoUnidadeConservacao.removerByIdeRequerimentoUnidadeConservacao", query = "delete FROM RequerimentoUnidadeConservacao r WHERE r.ideRequerimentoUnidadeConservacao = :ideRequerimentoUnidadeConservacao")
	})
public class RequerimentoUnidadeConservacao implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@SequenceGenerator(name = "requerimento_unidade_conservacao_generator", sequenceName = "requerimento_unidade_conservacao_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requerimento_unidade_conservacao_generator")
	@Column(name = "ide_requerimento_unidade_conservacao", nullable = false)
	private Integer ideRequerimentoUnidadeConservacao;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
	@ManyToOne(optional = false)
	private Requerimento ideRequerimento;

	@JoinColumn(name = "ide_unidade_conservacao", referencedColumnName = "ide_unidade_conservacao")
	@ManyToOne
	private UnidadeConservacao ideUnidadeConservacao;

	@Column(name = "nom_unidade_conservacao_municipal", length = 50)
	private String nomUnidadeConservacaoMunicipio;

	
	public RequerimentoUnidadeConservacao() {
	}

	public Integer getIdeRequerimentoUnidadeConservacao() {
		return ideRequerimentoUnidadeConservacao;
	}

	public void setIdeRequerimentoUnidadeConservacao(
			Integer ideRequerimentoUnidadeConservacao) {
		this.ideRequerimentoUnidadeConservacao = ideRequerimentoUnidadeConservacao;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}
	
	public UnidadeConservacao getIdeUnidadeConservacao() {
		return ideUnidadeConservacao;
	}

	public void setIdeUnidadeConservacao(UnidadeConservacao ideUnidadeConservacao) {
		this.ideUnidadeConservacao = ideUnidadeConservacao;
	}

	public String getNomUnidadeConservacaoMunicipio() {
		return nomUnidadeConservacaoMunicipio;
	}

	public void setNomUnidadeConservacaoMunicipio(
			String nomUnidadeConservacaoMunicipio) {
		this.nomUnidadeConservacaoMunicipio = nomUnidadeConservacaoMunicipio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideRequerimentoUnidadeConservacao == null) ? 0 : ideRequerimentoUnidadeConservacao.hashCode());
		result = prime * result + ((ideRequerimento == null) ? 0 : ideRequerimento.hashCode());
		result = prime * result + ((ideUnidadeConservacao == null) ? 0 : ideUnidadeConservacao.hashCode());
		result = prime * result + ((nomUnidadeConservacaoMunicipio == null) ? 0 : nomUnidadeConservacaoMunicipio.hashCode());
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
		RequerimentoUnidadeConservacao other = (RequerimentoUnidadeConservacao) obj;
		if (ideRequerimentoUnidadeConservacao == null) {
			if (other.ideRequerimentoUnidadeConservacao != null)
				return false;
		} else if (!ideRequerimentoUnidadeConservacao.equals(other.ideRequerimentoUnidadeConservacao))
			return false;
		if (ideRequerimento == null) {
			if (other.ideRequerimento != null)
				return false;
		} else if (!ideRequerimento.equals(other.ideRequerimento))
			return false;
		if (ideUnidadeConservacao == null) {
			if (other.ideUnidadeConservacao != null)
				return false;
		} else if (!ideUnidadeConservacao.equals(other.ideUnidadeConservacao))
			return false;
		if (nomUnidadeConservacaoMunicipio == null) {
			if (other.nomUnidadeConservacaoMunicipio != null)
				return false;
		} else if (!nomUnidadeConservacaoMunicipio.equals(other.nomUnidadeConservacaoMunicipio))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "entity.RequerimentoUnidadeConservacao[ requerimentoUnidadeConservacaoPK=" + ideRequerimentoUnidadeConservacao + " ]";
	}

}
