package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;


/**
 * The persistent class for the classificacao_vegetacao database table.
 * 
 */
@Entity
@Table(name="classificacao_vegetacao")
public class ClassificacaoVegetacao implements Serializable, Comparable<ClassificacaoVegetacao> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classificacao_vegetacao_ide_classificacao_vegetacao_generator")
	@SequenceGenerator(name = "classificacao_vegetacao_ide_classificacao_vegetacao_generator", sequenceName = "classificacao_vegetacao_ide_classificacao_vegetacao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_classificacao_vegetacao")
	private Integer ideClassificacaoVegetacao;

	@Size(min = 1, max = 100)
	@Column(name="dsc_classificacao_vegetacao" ,nullable = true, length = 100)
	private String dscClassificacaoVegetacao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public ClassificacaoVegetacao() {
	}

	public ClassificacaoVegetacao(Integer ideClassificacaoVegetacao) {
		this.ideClassificacaoVegetacao = ideClassificacaoVegetacao;
	}

	public ClassificacaoVegetacao(String dscClassficacaoVegetacao){
		this.dscClassificacaoVegetacao = dscClassficacaoVegetacao;
	}

	public Integer getIdeClassificacaoVegetacao() {
		return this.ideClassificacaoVegetacao;
	}

	public void setIdeClassificacaoVegetacao(Integer ideClassificacaoVegetacao) {
		this.ideClassificacaoVegetacao = ideClassificacaoVegetacao;
	}

	public String getDscClassificacaoVegetacao() {
		return this.dscClassificacaoVegetacao;
	}

	public void setDscClassificacaoVegetacao(String dscClassificacaoVegetacao) {
		this.dscClassificacaoVegetacao = dscClassificacaoVegetacao;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ (ideClassificacaoVegetacao == null ? 0
						: ideClassificacaoVegetacao.hashCode());
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
		ClassificacaoVegetacao other = (ClassificacaoVegetacao) obj;
		if (ideClassificacaoVegetacao == null) {
			if (other.ideClassificacaoVegetacao != null) {
				return false;
			}
		} else if (!ideClassificacaoVegetacao
				.equals(other.ideClassificacaoVegetacao)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(ClassificacaoVegetacao o) {
		Collator collator = Collator.getInstance (new Locale ("pt", "BR"));
		collator.setStrength(Collator.PRIMARY);
		return  collator.compare(dscClassificacaoVegetacao, o.dscClassificacaoVegetacao);
	}



}