package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="gestor_financeiro")
public class GestorFinanceiro extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_gestor_financeiro")
	private Integer ideGestorFinanceiro;

	@Column(name="nom_gestor_financeiro")
	private String nomGestorFinanceiro;

	public GestorFinanceiro() {
	}

	public Integer getIdeGestorFinanceiro() {
		return this.ideGestorFinanceiro;
	}

	public void setIdeGestorFinanceiro(Integer ideGestorFinanceiro) {
		this.ideGestorFinanceiro = ideGestorFinanceiro;
	}

	public String getNomGestorFinanceiro() {
		return this.nomGestorFinanceiro;
	}

	public void setNomGestorFinanceiro(String nomGestorFinanceiro) {
		this.nomGestorFinanceiro = nomGestorFinanceiro;
	}
}