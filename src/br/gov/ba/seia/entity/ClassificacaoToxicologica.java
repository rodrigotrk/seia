package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * The persistent class for the classificacao_toxicologica database table.
 * 
 */
@Entity
@Table(name="classificacao_toxicologica")
public class ClassificacaoToxicologica implements Serializable,BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_classificacao_toxicologica")
	private Integer ideClassificacaoToxicologica;

	@Column(name="dsc_classificacao_toxicologica")
	private String dscClassificacaoToxicologica;

	@Column(name="ide_classificacao_defensivo_agricola")
	private Integer ideClassificacaoDefensivoAgricola;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public ClassificacaoToxicologica() {
	}

	public Integer getIdeClassificacaoToxicologica() {
		return this.ideClassificacaoToxicologica;
	}

	public void setIdeClassificacaoToxicologica(Integer ideClassificacaoToxicologica) {
		this.ideClassificacaoToxicologica = ideClassificacaoToxicologica;
	}

	public String getDscClassificacaoToxicologica() {
		return this.dscClassificacaoToxicologica;
	}

	public void setDscClassificacaoToxicologica(String dscClassificacaoToxicologica) {
		this.dscClassificacaoToxicologica = dscClassificacaoToxicologica;
	}

	public Integer getIdeClassificacaoDefensivoAgricola() {
		return this.ideClassificacaoDefensivoAgricola;
	}

	public void setIdeClassificacaoDefensivoAgricola(Integer ideClassificacaoDefensivoAgricola) {
		this.ideClassificacaoDefensivoAgricola = ideClassificacaoDefensivoAgricola;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return new Long(this.ideClassificacaoToxicologica);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ (ideClassificacaoDefensivoAgricola == null ? 0
						: ideClassificacaoDefensivoAgricola.hashCode());
		result = prime
				* result
				+ (ideClassificacaoToxicologica == null ? 0
						: ideClassificacaoToxicologica.hashCode());
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
		ClassificacaoToxicologica other = (ClassificacaoToxicologica) obj;
		if (ideClassificacaoDefensivoAgricola == null) {
			if (other.ideClassificacaoDefensivoAgricola != null) {
				return false;
			}
		} else if (!ideClassificacaoDefensivoAgricola
				.equals(other.ideClassificacaoDefensivoAgricola)) {
			return false;
		}
		if (ideClassificacaoToxicologica == null) {
			if (other.ideClassificacaoToxicologica != null) {
				return false;
			}
		} else if (!ideClassificacaoToxicologica
				.equals(other.ideClassificacaoToxicologica)) {
			return false;
		}
		return true;
	}
}