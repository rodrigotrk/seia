package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * The persistent class for the classificacao_rejeito_dnpm database table.
 *
 */
@Entity
@Table(name = "classificacao_rejeito_dnpm")
public class ClassificacaoRejeitoDnpm implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "classificacao_rejeito_dnpm_IDECLASSIFICACAOREJEITODNPM_GENERATOR", sequenceName = "classificacao_rejeito_dnpm_IDE_classificacao_rejeito_dnpm_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classificacao_rejeito_dnpm_IDECLASSIFICACAOREJEITODNPM_GENERATOR")
	@Column(name = "ide_classificacao_rejeito_dnpm")
	private Integer ideClassificacaoRejeitoDnpm;

	@Column(name = "des_classificacao_rejeito_dnpm")
	private String desClassificacaoRejeitoDnpm;

	public ClassificacaoRejeitoDnpm() {
	}

	public Integer getIdeClassificacaoRejeitoDnpm() {
		return this.ideClassificacaoRejeitoDnpm;
	}

	public void setIdeClassificacaoRejeitoDnpm(Integer ideClassificacaoRejeitoDnpm) {
		this.ideClassificacaoRejeitoDnpm = ideClassificacaoRejeitoDnpm;
	}

	public String getDesClassificacaoRejeitoDnpm() {
		return this.desClassificacaoRejeitoDnpm;
	}

	public void setDesClassificacaoRejeitoDnpm(String desClassificacaoRejeitoDnpm) {
		this.desClassificacaoRejeitoDnpm = desClassificacaoRejeitoDnpm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideClassificacaoRejeitoDnpm == null) ? 0 : ideClassificacaoRejeitoDnpm.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		ClassificacaoRejeitoDnpm other = (ClassificacaoRejeitoDnpm) obj;
		if(ideClassificacaoRejeitoDnpm == null){
			if(other.ideClassificacaoRejeitoDnpm != null) {
				return false;
			}
		}
		else if(!ideClassificacaoRejeitoDnpm.equals(other.ideClassificacaoRejeitoDnpm)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideClassificacaoRejeitoDnpm);
	}

}