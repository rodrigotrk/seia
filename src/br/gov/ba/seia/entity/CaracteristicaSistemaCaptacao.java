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

@Entity
@Table(name="caracteristica_sistema_captacao")
public class CaracteristicaSistemaCaptacao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CARACTERISTICA_SISTEMA_CAPTACAO_IDE_CARACTERISTICA_SISTEMA_CAPTACAO_SEQ", sequenceName="CARACTERISTICA_SISTEMA_CAPTACAO_IDE_CARACTERISTICA_SISTEMA_CAPTACAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARACTERISTICA_SISTEMA_CAPTACAO_IDE_CARACTERISTICA_SISTEMA_CAPTACAO_SEQ")
	@Column(name="ide_caracteristica_sistema_captacao")
	private Integer ideCaracteristicaSistemaCaptacao;

	@Column(name="dsc_caracteristica_sistema_captacao")
	private String dscCaracteristicaSistemaCaptacao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public CaracteristicaSistemaCaptacao() {
	}

	public CaracteristicaSistemaCaptacao(Integer ideCaracteristicaSistemaCaptacao) {
		super();
		this.ideCaracteristicaSistemaCaptacao = ideCaracteristicaSistemaCaptacao;
	}

	public Integer getIdeCaracteristicaSistemaCaptacao() {
		return this.ideCaracteristicaSistemaCaptacao;
	}

	public void setIdeCaracteristicaSistemaCaptacao(Integer ideCaracteristicaSistemaCaptacao) {
		this.ideCaracteristicaSistemaCaptacao = ideCaracteristicaSistemaCaptacao;
	}

	public String getDscCaracteristicaSistemaCaptacao() {
		return this.dscCaracteristicaSistemaCaptacao;
	}

	public void setDscCaracteristicaSistemaCaptacao(String dscCaracteristicaSistemaCaptacao) {
		this.dscCaracteristicaSistemaCaptacao = dscCaracteristicaSistemaCaptacao;
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
				+ ((ideCaracteristicaSistemaCaptacao == null) ? 0
						: ideCaracteristicaSistemaCaptacao.hashCode());
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
		CaracteristicaSistemaCaptacao other = (CaracteristicaSistemaCaptacao) obj;
		if (ideCaracteristicaSistemaCaptacao == null) {
			if (other.ideCaracteristicaSistemaCaptacao != null) {
				return false;
			}
		} else if (!ideCaracteristicaSistemaCaptacao
				.equals(other.ideCaracteristicaSistemaCaptacao)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideCaracteristicaSistemaCaptacao);
	}

}