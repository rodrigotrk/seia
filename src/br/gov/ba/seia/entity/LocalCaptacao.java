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
 * The persistent class for the local_captacao database table.
 * 
 */
@Entity
@Table(name="local_captacao")
public class LocalCaptacao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LOCAL_CAPTACAO_IDE_LOCAL_CAPTACAO_SEQ", sequenceName="LOCAL_CAPTACAO_IDE_LOCAL_CAPTACAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOCAL_CAPTACAO_IDE_LOCAL_CAPTACAO_SEQ")
	@Column(name="ide_local_captacao")
	private Integer ideLocalCaptacao;

	@Column(name="dsc_local_captacao")
	private String dscLocalCaptacao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public LocalCaptacao() {
	}

	public Integer getIdeLocalCaptacao() {
		return this.ideLocalCaptacao;
	}

	public void setIdeLocalCaptacao(Integer ideLocalCaptacao) {
		this.ideLocalCaptacao = ideLocalCaptacao;
	}

	public String getDscLocalCaptacao() {
		return this.dscLocalCaptacao;
	}

	public void setDscLocalCaptacao(String dscLocalCaptacao) {
		this.dscLocalCaptacao = dscLocalCaptacao;
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
				+ ((ideLocalCaptacao == null) ? 0 : ideLocalCaptacao.hashCode());
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
		LocalCaptacao other = (LocalCaptacao) obj;
		if (ideLocalCaptacao == null) {
			if (other.ideLocalCaptacao != null) {
				return false;
			}
		} else if (!ideLocalCaptacao.equals(other.ideLocalCaptacao)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideLocalCaptacao);
	}

}