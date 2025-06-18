package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the justificativa_supressao database table.
 * 
 */
@Entity
@Table(name="justificativa_supressao")
public class JustificativaSupressao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_justificativa_supressao")
	private Integer ideJustificativaSupressao;

	@Column(name="dsc_justificativa_supressao")
	private String dscJustificativaSupressao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public JustificativaSupressao() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideJustificativaSupressao == null) ? 0 : ideJustificativaSupressao.hashCode());
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
		JustificativaSupressao other = (JustificativaSupressao) obj;
		if (ideJustificativaSupressao == null) {
			if (other.ideJustificativaSupressao != null) {
				return false;
			}
		} else if (!ideJustificativaSupressao.equals(other.ideJustificativaSupressao)) {
			return false;
		}
		return true;
	}

	public Integer getIdeJustificativaSupressao() {
		return this.ideJustificativaSupressao;
	}

	public void setIdeJustificativaSupressao(Integer ideJustificativaSupressao) {
		this.ideJustificativaSupressao = ideJustificativaSupressao;
	}

	public String getDscJustificativaSupressao() {
		return this.dscJustificativaSupressao;
	}

	public void setDscJustificativaSupressao(String dscJustificativaSupressao) {
		this.dscJustificativaSupressao = dscJustificativaSupressao;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return new Long(ideJustificativaSupressao);
	}
}