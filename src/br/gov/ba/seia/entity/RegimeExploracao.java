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
 * The persistent class for the regime_exploracao database table.
 * 
 */
@Entity
@Table(name = "regime_exploracao")
public class RegimeExploracao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "REGIME_EXPLORACAO_IDEREGIMEEXPLORACAO_GENERATOR", sequenceName = "REGIME_EXPLORACAO_IDE_REGIME_EXPLORACAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REGIME_EXPLORACAO_IDEREGIMEEXPLORACAO_GENERATOR")
	@Column(name = "ide_regime_exploracao")
	private Integer ideRegimeExploracao;

	@Column(name = "nom_regime_exploracao")
	private String nomRegimeExploracao;

	public RegimeExploracao() {
	}

	public Integer getIdeRegimeExploracao() {
		return this.ideRegimeExploracao;
	}

	public void setIdeRegimeExploracao(Integer ideRegimeExploracao) {
		this.ideRegimeExploracao = ideRegimeExploracao;
	}

	public String getNomRegimeExploracao() {
		return this.nomRegimeExploracao;
	}

	public void setNomRegimeExploracao(String nomRegimeExploracao) {
		this.nomRegimeExploracao = nomRegimeExploracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideRegimeExploracao == null) ? 0 : ideRegimeExploracao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		RegimeExploracao other = (RegimeExploracao) obj;
		if(ideRegimeExploracao == null){
			if(other.ideRegimeExploracao != null)
				return false;
		}
		else if(!ideRegimeExploracao.equals(other.ideRegimeExploracao))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideRegimeExploracao);
	}

}