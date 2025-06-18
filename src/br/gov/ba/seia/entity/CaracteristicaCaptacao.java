package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name="caracteristica_captacao")
public class CaracteristicaCaptacao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CARACTERISTICA_CAPTACAO_IDE_CARACTERISTICA_CAPTACAO_SEQ", sequenceName="CARACTERISTICA_CAPTACAO_IDE_CARACTERISTICA_CAPTACAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARACTERISTICA_CAPTACAO_IDE_CARACTERISTICA_CAPTACAO_SEQ")
	@Column(name="ide_caracteristica_captacao")
	private Integer ideCaracteristicaCaptacao;

	@Column(name="dsc_caracteristica_captacao")
	private String dscCaracteristicaCaptacao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Transient
	private boolean desabilitado;

	public CaracteristicaCaptacao() {
	}

	public CaracteristicaCaptacao(Integer ideCaracteristicaCaptacao) {
		this.ideCaracteristicaCaptacao = ideCaracteristicaCaptacao;
	}

	public Integer getIdeCaracteristicaCaptacao() {
		return this.ideCaracteristicaCaptacao;
	}

	public void setIdeCaracteristicaCaptacao(Integer ideCaracteristicaCaptacao) {
		this.ideCaracteristicaCaptacao = ideCaracteristicaCaptacao;
	}

	public String getDscCaracteristicaCaptacao() {
		return this.dscCaracteristicaCaptacao;
	}

	public void setDscCaracteristicaCaptacao(String dscCaracteristicaCaptacao) {
		this.dscCaracteristicaCaptacao = dscCaracteristicaCaptacao;
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
				+ ((ideCaracteristicaCaptacao == null) ? 0
						: ideCaracteristicaCaptacao.hashCode());
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
		CaracteristicaCaptacao other = (CaracteristicaCaptacao) obj;
		if (ideCaracteristicaCaptacao == null) {
			if (other.ideCaracteristicaCaptacao != null) {
				return false;
			}
		} else if (!ideCaracteristicaCaptacao
				.equals(other.ideCaracteristicaCaptacao)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideCaracteristicaCaptacao);
	}
}