package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the classificacao_adubacao database table.
 * 
 */
@Entity
@Table(name="classificacao_adubacao")
public class ClassificacaoAdubacao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classificacao_adubacao_ide_classificacao_adubacao_generator")
	@SequenceGenerator(name = "classificacao_adubacao_ide_classificacao_adubacao_generator", sequenceName = "classificacao_adubacao_ide_classificacao_adubacao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_classificacao_adubacao")
	private Integer ideClassificacaoAdubacao;

	@Size(min = 1, max = 50)
	@Column(name="dsc_classificacao_adubacao" ,nullable = false, length = 50)
	private String dscClassificacaoAdubacao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public ClassificacaoAdubacao() {
	}

	public ClassificacaoAdubacao (String dscClassificacaoAdubacao){
		this.dscClassificacaoAdubacao=dscClassificacaoAdubacao;
	}

	public ClassificacaoAdubacao (Integer ideClassificacaoAdubacao){
		this.ideClassificacaoAdubacao=ideClassificacaoAdubacao;
	}

	public Integer getIdeClassificacaoAdubacao() {
		return this.ideClassificacaoAdubacao;
	}

	public void setIdeClassificacaoAdubacao(Integer ideClassificacaoAdubacao) {
		this.ideClassificacaoAdubacao = ideClassificacaoAdubacao;
	}

	public String getDscClassificacaoAdubacao() {
		return this.dscClassificacaoAdubacao;
	}

	public void setDscClassificacaoAdubacao(String dscClassificacaoAdubacao) {
		this.dscClassificacaoAdubacao = dscClassificacaoAdubacao;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return new Long(this.ideClassificacaoAdubacao);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ideClassificacaoAdubacao == null ? 0 : ideClassificacaoAdubacao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ClassificacaoAdubacao other = (ClassificacaoAdubacao) obj;
		if (ideClassificacaoAdubacao == null) {
			if (other.ideClassificacaoAdubacao != null) {
				return false;
			}
		} else if (!ideClassificacaoAdubacao.equals(other.ideClassificacaoAdubacao)) {
			return false;
		}
		return true;
	}
}