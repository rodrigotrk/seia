package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * 
 * @author eduardo.fernandes
 * 
 */

@Entity
@Table(name = "unidade_conservacao")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "UnidadeConservacao.findAll", query = "SELECT u FROM UnidadeConservacao u"),
		@NamedQuery(name = "UnidadeConservacao.findByIdeUnidadeConservacao", query = "SELECT u FROM UnidadeConservacao u WHERE u.ideUnidadeConservacao = :ideUnidadeConservacao"),
		@NamedQuery(name = "UnidadeConservacao.findBynomUnidadeConservacao", query = "SELECT u FROM UnidadeConservacao u WHERE u.nomUnidadeConservacao = :nomUnidadeConservacao"),
		@NamedQuery(name = "UnidadeConservacao.findByIdeTipoGestao", query = "SELECT u FROM UnidadeConservacao u WHERE u.ideTipoGestao = :ideTipoGestao"),
		@NamedQuery(name = "UnidadeConservacao.findByIndAtivo", query = "SELECT u FROM UnidadeConservacao u WHERE u.indAtivo = :indAtivo"),
		@NamedQuery(name = "UnidadeConservacao.removeByIdeUnidadeConservacao", query = "DELETE FROM UnidadeConservacao u WHERE u.ideUnidadeConservacao = :ideUnidadeConservacao"), })
public class UnidadeConservacao implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "ide_unidade_conservacao")
	private Integer ideUnidadeConservacao;

	
	@NotNull
	@Column(name = "nom_unidade_conservacao", length = 50)
	private String nomUnidadeConservacao;

	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	@JoinColumn(name = "ide_tipo_gestao", referencedColumnName = "ide_tipo_gestao")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoGestao ideTipoGestao;
	
	@OneToMany(mappedBy = "ideUnidadeConservacao", fetch = FetchType.LAZY)
	private Collection<RequerimentoUnidadeConservacao> requerimentoUnidadeConservacaoCollection;

	public UnidadeConservacao() {

	}

	public UnidadeConservacao(Integer ideUnidadeConservacao) {
		this.ideUnidadeConservacao = ideUnidadeConservacao;
	}

	public Integer getIdeUnidadeConservacao() {
		return ideUnidadeConservacao;
	}

	public void setIdeUnidadeConservacao(Integer ideUnidadeConservacao) {
		this.ideUnidadeConservacao = ideUnidadeConservacao;
	}

	public String getNomUnidadeConservacao() {
		return nomUnidadeConservacao;
	}

	public void setNomUnidadeConservacao(String nomUnidadeConservacao) {
		this.nomUnidadeConservacao = nomUnidadeConservacao;
	}

	public TipoGestao getIdeTipoGestao() {
		return ideTipoGestao;
	}

	public void setIdeTipoGestao(TipoGestao ideTipoGestao) {
		this.ideTipoGestao = ideTipoGestao;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	@XmlTransient
	public Collection<RequerimentoUnidadeConservacao> getRequerimentoUnidadeConservacaoCollection() {
		return requerimentoUnidadeConservacaoCollection;
	}

	public void setRequerimentoUnidadeConservacaoCollection(Collection<RequerimentoUnidadeConservacao> requerimentoUnidadeConservacaoCollection) {
		this.requerimentoUnidadeConservacaoCollection = requerimentoUnidadeConservacaoCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideUnidadeConservacao == null) ? 0 : ideUnidadeConservacao.hashCode());
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
		UnidadeConservacao other = (UnidadeConservacao) obj;
		if (ideUnidadeConservacao == null) {
			if (other.ideUnidadeConservacao != null)
				return false;
		} else if (!ideUnidadeConservacao.equals(other.ideUnidadeConservacao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "entity.UnidadeConservacao[ ideUnidadeConservacao=" + ideUnidadeConservacao + " ]";
	}

	@Override
	public Long getId() {
		return new Long(this.ideUnidadeConservacao);
	}

}